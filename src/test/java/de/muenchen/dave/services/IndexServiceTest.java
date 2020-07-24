package de.muenchen.dave.services;

import de.muenchen.dave.domain.dtos.BearbeiteZaehlstelleDTO;
import de.muenchen.dave.domain.dtos.BearbeiteZaehlstelleDTORandomFactory;
import de.muenchen.dave.domain.elasticsearch.Zaehlstelle;
import de.muenchen.dave.domain.elasticsearch.Zaehlung;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class IndexServiceTest {

    private IndexService service = new IndexService(null);

    @Test
    public void testMapZaehlstelleDtoToBean() {

        BearbeiteZaehlstelleDTO dto = BearbeiteZaehlstelleDTORandomFactory.getOne();
        Zaehlstelle z = new Zaehlstelle();

        this.service.mapZaehlstelleDtoToBean(dto, z);

        assertThat(z.getNummer(), is(equalTo(dto.getNummer())));
        assertThat(z.getName(), is(equalTo(dto.getName())));
        assertThat(z.getStadtbezirkNummer(), is(equalTo(dto.getStadtbezirkNummer())));
        assertThat(z.getStadtbezirk(), is(equalTo(IndexServiceUtils.leseStadtbezirk(z.getStadtbezirkNummer()))));
        assertThat(z.getPunkt(), is(notNullValue()));
        assertThat(z.getZaehljahre(), emptyIterable());
        assertThat(z.getStrassen().isEmpty(), is(false));
        assertThat(z.getGeographie().isEmpty(), is(notNullValue()));
        assertThat(z.getSuchwoerter(), is(equalTo(dto.getSuchwoerter())));
        assertThat(z.getZaehlungen(), emptyIterable());
    }

    @Test
    public void testUpdateZaehlstelleWithZaehlung() {

        Zaehlstelle zs = new Zaehlstelle();
        zs.setZaehlungen(new ArrayList<>());

        Zaehlung z1 = new Zaehlung();
        z1.setDatum(LocalDate.of(2010, 1, 1));

        Zaehlung z2 = new Zaehlung();
        z2.setDatum(LocalDate.of(2015, 5, 1));
        z2.setJahr(2015);

        Zaehlung z3 = new Zaehlung();
        z3.setDatum(LocalDate.of(2020, 7, 1));
        z3.setJahr(2020);

        // letzte hinzugefügte Zählung ist älter als die vorhande
        this.service.updateZaehlstelleWithZaehlung(zs, z2);
        this.service.updateZaehlstelleWithZaehlung(zs, z1);
        assertThat(zs.getLetzteZaehlungJahr(), is(equalTo(2015)));
        assertThat(zs.getLetzteZaehlungMonatNummer(), is(equalTo(5)));

        // letztes hinzugefügtes Zählung ist die Jüngste
        this.service.updateZaehlstelleWithZaehlung(zs, z3);
        assertThat(zs.getLetzteZaehlungJahr(), is(equalTo(2020)));
        assertThat(zs.getLetzteZaehlungMonatNummer(), is(equalTo(7)));

    }

}
