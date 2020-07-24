package de.muenchen.dave.services;

import de.muenchen.dave.exceptions.BrokenInfrastructureException;
import de.muenchen.dave.exceptions.ResourceNotFoundException;
import de.muenchen.dave.domain.dtos.BearbeiteZaehlstelleDTO;
import de.muenchen.dave.domain.dtos.BearbeiteZaehlungDTO;
import de.muenchen.dave.domain.elasticsearch.Zaehlstelle;
import de.muenchen.dave.domain.elasticsearch.Zaehlung;
import de.muenchen.dave.repositories.elasticsearch.ZaehlstelleIndex;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.stereotype.Service;

import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Optional;

@Service
@Slf4j
public class IndexService {

    @Value(value = "${elasticsearch.host}")
    private String elasticsearchHost;

    @Value(value = "${elasticsearch.port}")
    private String elasticsearchPort;

    private final ZaehlstelleIndex index;

    public IndexService(ZaehlstelleIndex index) {
        this.index = index;
    }

    /**
     * Zählstelle im Index speichern. Sollte Elasticsearch nicht
     * erreichbar sein, dann wird eine entsprechende Exception
     * geworfen und eine Fehlermeldung geloggt.
     *
     * @param z
     * @throws BrokenInfrastructureException
     */
    public void speichereIndex(Zaehlstelle z) throws BrokenInfrastructureException {
        try {
            Zaehlstelle save = this.index.save(z);
        } catch (DataAccessResourceFailureException e) {
            log.error("cannot access elasticsearch index on {}:{}", this.elasticsearchHost, this.elasticsearchPort);
            throw new BrokenInfrastructureException();
        }
    }

    /**
     * Erstellt eine neue Zählstelle.
     *
     * @param zdto
     * @param id
     * @throws BrokenInfrastructureException
     */
    public void erstelleZaehlstelle(BearbeiteZaehlstelleDTO zdto, String id) throws BrokenInfrastructureException {
        Zaehlstelle z = new Zaehlstelle();
        
        z.setId(id);
        this.mapZaehlstelleDtoToBean(zdto, z);
        this.speichereIndex(z);

    }

    /**
     * Speichert die Daten zur Zählstelle, wenn diese erneuert wurden.
     *
     * @param zdto
     * @param id
     * @throws ResourceNotFoundException
     * @throws BrokenInfrastructureException
     */
    public void erneuereZaehlstelle(BearbeiteZaehlstelleDTO zdto, String id) throws ResourceNotFoundException, BrokenInfrastructureException {
        Optional<Zaehlstelle> zsto = this.index.findById(id);
        if(zsto.isPresent()) {
            Zaehlstelle z = zsto.get();
            this.mapZaehlstelleDtoToBean(zdto, z);
            this.speichereIndex(z);
        } else {
            log.error("Keine Zählstelle zur id {} gefunden.", id);
            throw new ResourceNotFoundException();
        }
    }

    /**
     * Erstellt eine neue Zählung, updatet die entsprechende Zählstelle und
     * speichert alles im Index.
     *
     * @param zdto
     * @param zaehlungId
     * @param zaehlstelleId
     * @throws ResourceNotFoundException
     * @throws BrokenInfrastructureException
     */
    public void erstelleZaehlung(BearbeiteZaehlungDTO zdto, String zaehlungId, String zaehlstelleId) throws ResourceNotFoundException, BrokenInfrastructureException {
        Zaehlung z = new Zaehlung();

        z.setId(zaehlungId);
        this.mapZaehlungDtoToBean(zdto, z);

        // Zählstelle erneuern
        Optional<Zaehlstelle> zsto = this.index.findById(zaehlstelleId);
        if(zsto.isPresent()) {
            Zaehlstelle zst = zsto.get();
            this.updateZaehlstelleWithZaehlung(zst, z);
            this.speichereIndex(zst);
        } else {
            log.error("Keine Zählstelle zur id {} gefunden.", zaehlstelleId);
            throw new ResourceNotFoundException();
        }
    }

    /**
     * Erneuert die Parameter einer Zählstelle, die von der letzten Zählung
     * abhängig sind. Dabei ist es irrelevant, ob die übergebene Zählung die
     * letzte Zählung ist.
     *
     * @param zs
     * @param zl
     */
    public void updateZaehlstelleWithZaehlung(Zaehlstelle zs, Zaehlung zl) {
        zs.getZaehlungen().add(zl);

        Zaehlung letzteZaehlung = IndexServiceUtils.getLetzteZaehlung(zs.getZaehlungen());

        zs.setLetzteZaehlungMonat(letzteZaehlung.getMonat());
        zs.setLetzteZaehlungMonatNummer(letzteZaehlung.getDatum().getMonthValue());
        zs.setGrundLetzteZaehlung(letzteZaehlung.getZaehlsituation());
        zs.setLetzteZaehlungJahr(letzteZaehlung.getJahr());
    }

    /**
     * Mappt die Felder vom DTO auf das Zählstellen Objekt.
     *
     * @param zdto
     * @param z
     */
    public void mapZaehlstelleDtoToBean(BearbeiteZaehlstelleDTO zdto, Zaehlstelle z) {
        z.setNummer(zdto.getNummer());
        z.setName(zdto.getName());
        z.setStadtbezirkNummer(zdto.getStadtbezirkNummer());
        z.setStadtbezirk(IndexServiceUtils.leseStadtbezirk(zdto.getStadtbezirkNummer()));
        z.setPunkt(new GeoPoint(zdto.getLat(), zdto.getLng()));
        z.setZaehljahre(new ArrayList<>());
        z.setStrassen(IndexServiceUtils.splitStrings(zdto.getStrassen()));
        z.setGeographie(IndexServiceUtils.splitStrings(zdto.getGeographie()));
        z.setSuchwoerter(zdto.getSuchwoerter());
        z.setZaehlungen(new ArrayList<>());
    }

    /**
     * Mappt die Felder vom DTO auf das Zählungs Objekt.
     *
     * @param zdto
     * @param z
     */
    public void mapZaehlungDtoToBean(BearbeiteZaehlungDTO zdto, Zaehlung z) {
        // date stuff
        z.setDatum(zdto.getDatum());
        z.setJahr(zdto.getDatum().getYear());
        z.setMonat(zdto.getDatum().getMonth().getDisplayName(TextStyle.FULL, Locale.GERMANY));
        z.setJahreszeit(IndexServiceUtils.jahreszeitenDetector(zdto.getDatum()));

        z.setTagesTyp(zdto.getTagesTyp());
        z.setKategorien(IndexServiceUtils.splitStrings(zdto.getKategorien()));
        z.setZaehlsituation(zdto.getGrund());
        z.setWetter(zdto.getWetter());
        z.setArtDerZaehlung(zdto.getArtDerZaehlung());
        z.setZaehldauer(zdto.getZaehlZeit());
        z.setSchulZeiten(zdto.getSchulZeiten());
        z.setSuchwoerter(zdto.getSuchwoerter());
    }


}
