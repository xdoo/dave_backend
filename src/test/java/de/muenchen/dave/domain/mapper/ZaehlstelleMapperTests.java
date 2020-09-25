package de.muenchen.dave.domain.mapper;

import de.muenchen.dave.domain.dtos.BearbeiteZaehlstelleDTO;
import de.muenchen.dave.domain.dtos.BearbeiteZaehlstelleDTORandomFactory;
import de.muenchen.dave.domain.dtos.SucheZaehlstelleSuggestDTO;
import de.muenchen.dave.domain.elasticsearch.Zaehlstelle;
import de.muenchen.dave.domain.elasticsearch.ZaehlstelleRandomFactory;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.common.Strings;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@Slf4j
public class ZaehlstelleMapperTests {

    @Autowired
    ZaehlstelleMapper mapper;

    @Test
    public void testDto2bean() {
        BearbeiteZaehlstelleDTO dto = BearbeiteZaehlstelleDTORandomFactory.getOne();
        Zaehlstelle bean = this.mapper.bearbeiteDto2bean(dto);

        assertThat(bean, hasProperty("nummer", equalTo(dto.getNummer())));
        assertThat(bean, hasProperty("name", equalTo(dto.getName())));
        assertThat(bean, hasProperty("stadtbezirkNummer", equalTo(dto.getStadtbezirkNummer())));
        assertThat(bean, hasProperty("strassen", containsInAnyOrder(Strings.toStringArray(dto.getStrassen()))));
        assertThat(bean, hasProperty("geographie", containsInAnyOrder(Strings.toStringArray(dto.getGeographie()))));
        assertThat(bean, hasProperty("suchwoerter", equalTo(dto.getSuchwoerter())));

        assertThat(bean, hasProperty("punkt", notNullValue()));
        assertThat(bean.getPunkt().getLat(), is(equalTo(dto.getLat())));
        assertThat(bean.getPunkt().getLon(), is(equalTo(dto.getLng())));
    }

    @Test
    public void testBean2Dto() {
        Zaehlstelle bean = ZaehlstelleRandomFactory.getOne();
        BearbeiteZaehlstelleDTO dto = this.mapper.bearbeiteDto(bean);

        assertThat(dto, hasProperty("nummer", equalTo(bean.getNummer())));
        assertThat(dto, hasProperty("name", equalTo(bean.getName())));
        assertThat(dto, hasProperty("stadtbezirkNummer", equalTo(bean.getStadtbezirkNummer())));
        assertThat(dto, hasProperty("strassen", containsInAnyOrder(Strings.toStringArray(bean.getStrassen()))));
        assertThat(dto, hasProperty("geographie", containsInAnyOrder(Strings.toStringArray(bean.getGeographie()))));
        assertThat(dto, hasProperty("suchwoerter", equalTo(bean.getSuchwoerter())));

        assertThat(dto, hasProperty("lat", equalTo(bean.getPunkt().getLat())));
        assertThat(dto, hasProperty("lng", equalTo(bean.getPunkt().getLon())));

    }

    @Test
    public void testToSucheCounterSuggestDto () {
        Zaehlstelle bean = ZaehlstelleRandomFactory.getOne();
        SucheZaehlstelleSuggestDTO dto = this.mapper.bean2SucheCounterSuggestDto(bean);

        assertThat(dto, hasProperty("id", equalTo(bean.getId())));
        assertThat(dto, hasProperty("text", equalTo(bean.getNummer() + " " + bean.getName())));
    }
}
