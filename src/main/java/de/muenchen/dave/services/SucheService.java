package de.muenchen.dave.services;

import com.google.common.collect.Lists;
import de.muenchen.dave.domain.dtos.SucheComplexSuggestsDTO;
import de.muenchen.dave.domain.dtos.SucheCountSuggestDTO;
import de.muenchen.dave.domain.dtos.SucheCounterSuggestDTO;
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

    private static final Pattern DE_DATE = Pattern.compile("\\d{1,2}.\\d{1,2}.\\d{2,4}");
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
    public void complexSuggest(String query) {
        String q = this.createQueryString(query);
        log.info("query '{}'", q);

        SucheComplexSuggestsDTO dto = new SucheComplexSuggestsDTO();

        // die Zählstellen
        Page<Zaehlstelle> zaehlstellen = this.zaehlstelleIndex.suggestSearch(q, PageRequest.of(0, 3));
        List<SucheCounterSuggestDTO> sucheCounterSuggestDTOS = zaehlstellen.get()
                .map(z -> this.zaehlstelleMapper.sucheCounterSuggestDto(z))
                .collect(Collectors.toList());
        dto.setZaehlstellenVorschlaege(sucheCounterSuggestDTOS);

        // Aus den Zählstellen werden ggf. noch Zählungen extrahiert.
        List<Zaehlung> zaehlungen = findZaehlung(zaehlstellen, query);
        List<SucheCountSuggestDTO> sucheCountSuggestDtos = zaehlungen.stream()
                .map(z -> this.zaehlungMapper.bean2SucheCountSuggestDto(z))
                .collect(Collectors.toList());
        dto.setZaehlungenVorschlaege(sucheCountSuggestDtos);

        // die Suggestions (TODO)
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
        Optional<String> optionalDate = this.extractDate(query);
        Optional<Zaehlung> optionalZaehlung;
        if(optionalDate.isPresent()) {
            optionalZaehlung = zaehlstelle.getZaehlungen().stream()
                    .filter(z -> z.getDatum().format(DATE_TIME_FORMATTER).equals(optionalDate.get()))
                    .findAny();
        } else {
            List<String> words = Lists.newArrayList(query.split(" "));
            optionalZaehlung = zaehlstelle.getZaehlungen().stream()
                    .filter(z -> words.contains(z.getProjektName()))
                    .findAny();
        }
        return optionalZaehlung;
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
            if(this.isDate(words[i])){
                queryBuilder.append(this.rewriteDate(words[i])).append(" ");
            } else {
                queryBuilder.append(words[i]).append("* ");
            }
        }
        return queryBuilder.toString().trim();
    }

    /**
     * Prüft und extrahiert das Datum als String, wenn die
     * Suchanfrage ein Datum enthält.
     *
     * @param query
     * @return
     */
    public Optional<String> extractDate(String query) {
        Matcher matcher = DE_DATE.matcher(query);
        String date = null;
        if(matcher.find()) {
            date = matcher.group();
            date = this.cleanseDate(date);
        }
        return Optional.ofNullable(date);
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
     * Jahr ggf. um die Tausender.
     *
     * @param date
     * @return
     */
    public String cleanseDate(String date) {
        String[] x = date.split("\\.");
        String d = x[0].length() < 2 ? 0 + x[0] : x[0];
        String m = x[1].length() < 2 ? 0 + x[1] : x[1];
        String y = x[2].length() < 4 ? 20 + x[2] : x[2];
        return d+"."+m+"."+y;
    }

    /**
     * Wandelt das deutsche Datum in einen Datumsstring um
     * der von ES interpretiert werden kann.
     *
     * @param date
     * @return
     */
    public String rewriteDate(String date) {
        String[] x = date.split("\\.");
        String d = x[0].length() < 2 ? 0 + x[0] : x[0];
        String m = x[1].length() < 2 ? 0 + x[1] : x[1];
        String y = x[2].length() < 4 ? 20 + x[2] : x[2];
        return y+m+d;
    }

}
