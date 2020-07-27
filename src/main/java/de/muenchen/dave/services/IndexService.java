package de.muenchen.dave.services;

import de.muenchen.dave.domain.mapper.ZaehlstelleMapper;
import de.muenchen.dave.domain.mapper.ZaehlungMapper;
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
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class IndexService {

    @Value(value = "${elasticsearch.host}")
    private String elasticsearchHost;

    @Value(value = "${elasticsearch.port}")
    private String elasticsearchPort;

    private final ZaehlstelleIndex index;
    private final ZaehlungMapper zaehlungMapper;
    private final ZaehlstelleMapper zaehlstelleMapper;

    public IndexService(ZaehlstelleIndex index, ZaehlungMapper zaehlungMapper, ZaehlstelleMapper zaehlstelleMapper) {
        this.index = index;
        this.zaehlungMapper = zaehlungMapper;
        this.zaehlstelleMapper = zaehlstelleMapper;
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
        Zaehlstelle z = this.zaehlstelleMapper.dto2bean(zdto);
        z.setId(id);
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
            Zaehlstelle z = this.zaehlstelleMapper.dto2bean(zdto);
            z.setId(id);
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
        Zaehlung z = this.zaehlungMapper.bearbeiteDto2bean(zdto);
        z.setId(UUID.randomUUID().toString());

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


}
