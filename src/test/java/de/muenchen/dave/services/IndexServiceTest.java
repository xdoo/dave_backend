package de.muenchen.dave.services;

import de.muenchen.dave.domain.dtos.BearbeiteZaehlstelleDTO;
import de.muenchen.dave.domain.dtos.BearbeiteZaehlstelleDTORandomFactory;
import de.muenchen.dave.domain.elasticsearch.Zaehlstelle;
import de.muenchen.dave.domain.elasticsearch.Zaehlung;
import org.junit.jupiter.api.Test;

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
        Zaehlung zl = new Zaehlung();

        //TODO
    }

}
