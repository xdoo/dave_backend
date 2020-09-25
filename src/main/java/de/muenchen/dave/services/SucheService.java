package de.muenchen.dave.services;

import com.google.common.collect.Lists;
import de.muenchen.dave.domain.dtos.SucheComplexSuggestsDTO;
import de.muenchen.dave.domain.dtos.SucheZaehlstelleSuggestDTO;
import de.muenchen.dave.domain.dtos.SucheZaehlungSuggestDTO;
import de.muenchen.dave.domain.elasticsearch.Zaehlstelle;
import de.muenchen.dave.domain.elasticsearch.Zaehlung;
import de.muenchen.dave.domain.mapper.ZaehlstelleMapper;
import de.muenchen.dave.domain.mapper.ZaehlungMapper;
import de.muenchen.dave.repositories.elasticsearch.ZaehlstelleIndex;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SucheService {

    private static final Pattern DE_DATE = Pattern.compile("\\d{1,2}[.]\\d{0,2}[.]{0,1}\\d{0,4}");
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private final ZaehlstelleIndex zaehlstelleIndex;
    private final ZaehlstelleMapper zaehlstelleMapper;
    private final ZaehlungMapper zaehlungMapper;

    public SucheService(ZaehlstelleIndex zaehlstelleIndex, ZaehlstelleMapper zaehlstelleMapper, ZaehlungMapper zaehlungMapper) {
        this.zaehlstelleIndex = zaehlstelleIndex;
        this.zaehlstelleMapper = zaehlstelleMapper;
        this.zaehlungMapper = zaehlungMapper;
    }

    /**
     * Erstellt eine Vorschlagsliste für die "search as you type" Suche. Diese
     * besteht aus Suchvorschlägen, Vorschläge für eine bestimmte Suchstelle und
     * Vorschläge für eine bestimmte Zählung.
     *
     * @param query
     */
    public SucheComplexSuggestsDTO complexSuggest(String query) {
        String q = this.createQueryString(query);
        log.info("query '{}'", q);

        SucheComplexSuggestsDTO dto = new SucheComplexSuggestsDTO();

        // die Zählstellen
        Page<Zaehlstelle> zaehlstellen = this.zaehlstelleIndex.suggestSearch(q, PageRequest.of(0, 3));
        List<SucheZaehlstelleSuggestDTO> sucheZaehlstelleSuggestDTOS = zaehlstellen.get()
                .map(z -> this.zaehlstelleMapper.bean2SucheCounterSuggestDto(z))
                .collect(Collectors.toList());
        dto.setZaehlstellenVorschlaege(sucheZaehlstelleSuggestDTOS);

        // Aus den Zählstellen werden ggf. noch Zählungen extrahiert.
        List<Zaehlung> zaehlungen = findZaehlung(zaehlstellen, query);
        List<SucheZaehlungSuggestDTO> sucheZaehlungSuggestDtos = zaehlungen.stream()
                .map(z -> this.zaehlungMapper.bean2SucheZaehlungSuggestDto(z))
                .collect(Collectors.toList());
        dto.setZaehlungenVorschlaege(sucheZaehlungSuggestDtos);

        // die Suggestions (TODO)
        return dto;
    }

    /**
     * Findet alle Zählungen, die zur Suchanfrage passen könnten.
     *
     * @param pz
     * @param query
     * @return
     */
    public List<Zaehlung>  findZaehlung(Page<Zaehlstelle> pz, String query) {
        List<Zaehlung> zs = pz.get()
                .map(zst -> this.checkZaehlstelleForZaehlung(zst, query))
                .filter(oz -> oz.isPresent())
                .map(oz -> oz.get())
                .collect(Collectors.toList());
        return  zs;
    }

    /**
     * Check die Zählstelle, ob sich darin Zählungen befinden, die direkt angezeigt werden
     * können. Im ersten Schritt werden hierfür das Datum und der Projektname hergenommen.
     * Bei Bedarf können diese zwei Parameter auch durch weitere Attrubute erweitert werden.
     *
     * @param zaehlstelle
     * @param query
     * @return
     */
    public Optional<Zaehlung> checkZaehlstelleForZaehlung(Zaehlstelle zaehlstelle, String query) {
        Optional<Zaehlung> optionalZaehlung;
        if(!zaehlstelle.getZaehlungen().isEmpty()) {
            List<String> words = Lists.newArrayList(query.split(" "));
            optionalZaehlung = zaehlstelle.getZaehlungen().stream()
                    .filter(z -> this.filterZaehlung(words, z))
                    .findAny();
        } else {
            optionalZaehlung = Optional.ofNullable(null);
        }
        return optionalZaehlung;
    }

    /**
     * Prüft, ob eines der Suchworte auf die angegebenen Attribute
     * einer Zählung passt.
     *
     * @param words
     * @param z
     * @return
     */
    public boolean filterZaehlung(List<String> words, Zaehlung z) {
        Optional<String> finding = words.stream()
                .filter(
                        w ->    z.getDatum().format(DATE_TIME_FORMATTER).startsWith(this.cleanseDate(w)) ||
                                z.getProjektName().startsWith(w)
                )
                .findAny();
        return finding.isPresent();
    }

    /**
     * Erstellt den Query String mit suffix Wildcards. Dadurch muss der
     * Anwender sich gar keine Gedanken machen, ob er jetzt eine Wildcard
     * benötigt, oder nicht.
     *
     * @param query
     * @return
     */
    public String createQueryString(String query) {
        StringBuilder queryBuilder = new StringBuilder();
        String[] words = query.split(" ");
        for(int i = 0; i < words.length; i++) {
            // es wird jedes Wort geprüft, ob es ein Datum ist
            // und dann entsprechend aufbereitet, dass damit
            // gesucht werden kann.
            String word = this.cleanseDate(words[i]);

            // Wildcard für jedes Suchwort.
            queryBuilder.append(word).append("* ");
        }
        return queryBuilder.toString().trim();
    }

    /**
     * Checkt, ob ein Suchstring ein Datum ist.
     *
     * @param x
     * @return
     */
    public boolean isDate(String x) {
        return DE_DATE.matcher(x).matches();
    }

    /**
     * Fügt in ein Datum führende Nullen ein und ergänzt das
     * Jahr ggf. um die Tausender. Wenn es sich nicht um ein Datum handelt,
     * dann wird einfach der String wieder zurück gegeben.
     *
     * @param word
     * @return
     */
    public String cleanseDate(String word) {

        if(this.isDate(word)) {
            String[] x = word.split("\\.");
            StringBuilder result = new StringBuilder();

            if (x.length > 0) {
                String d = x[0].length() < 2 ? 0 + x[0] : x[0];
                result.append(d).append(".");
            }

            if(x.length > 1) {
                String m = x[1].length() < 2 ? 0 + x[1] : x[1];
                result.append(m).append(".");
            }

            if(x.length > 2) {
                String y = x[2].length() < 4 ? 20 + x[2] : x[2];
                result.append(y);
            }
            return result.toString();
        } else {
            return word;
        }
    }

}
