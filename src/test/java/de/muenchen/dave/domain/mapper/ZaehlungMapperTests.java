package de.muenchen.dave.domain.mapper;
import de.muenchen.dave.domain.dtos.BearbeiteZaehlungDTO;
import de.muenchen.dave.domain.dtos.BearbeiteZaehlungDTORandomFactory;
import de.muenchen.dave.domain.elasticsearch.Zaehlung;
import de.muenchen.dave.domain.elasticsearch.ZaehlungRandomFactory;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.common.Strings;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@Slf4j
public class ZaehlungMapperTests {

    @Autowired ZaehlungMapper mapper;

    @Test
    public void testBearbeiteDto2bean() {
        BearbeiteZaehlungDTO dto = BearbeiteZaehlungDTORandomFactory.getOne();
        Zaehlung bean = this.mapper.bearbeiteDto2bean(dto);

        assertThat(bean, hasProperty("datum", equalTo(dto.getDatum())));
        assertThat(bean, hasProperty("tagesTyp", equalTo(dto.getTagesTyp())));
        assertThat(bean, hasProperty("projektNummer", equalTo(dto.getProjektNummer())));
        assertThat(bean, hasProperty("projektName", equalTo(dto.getProjektName())));
        assertThat(bean, hasProperty("kategorien", containsInAnyOrder(Strings.toStringArray(dto.getKategorien())) ));
        assertThat(bean, hasProperty("zaehlsituation", equalTo(dto.getZaehlsituation())));
        assertThat(bean, hasProperty("zaehlsituationErweitert", equalTo(dto.getZaehlsituationErweitert())));
        assertThat(bean, hasProperty("zaehlIntervall", equalTo(dto.getZaehlIntervall())));
        assertThat(bean, hasProperty("wetter", equalTo(dto.getWetter())));
        assertThat(bean, hasProperty("artDerZaehlung", equalTo(dto.getArtDerZaehlung())));
        assertThat(bean, hasProperty("zaehldauer", equalTo(dto.getZaehldauer())));
        assertThat(bean, hasProperty("schulZeiten", equalTo(dto.getSchulZeiten())));
        assertThat(bean, hasProperty("suchwoerter", equalTo(dto.getSuchwoerter())));

        // Datum mappings (TODO)
        assertThat(bean, hasProperty("jahr", equalTo(dto.getDatum().getYear())));
        assertThat(bean, hasProperty("monat", notNullValue()));
        assertThat(bean, hasProperty("jahreszeit", notNullValue()));

        // Sonderzählung mapping
        assertThat(bean, hasProperty("sonderzaehlung", equalTo(ZaehlungMapper.SONDERZAEHLUNG)));

    }

    @Test
    public void testBean2BearbeiteDto() {
        Zaehlung bean = ZaehlungRandomFactory.getOne();
        BearbeiteZaehlungDTO dto = this.mapper.bean2BearbeiteDto(bean);

        assertThat(dto, hasProperty("datum", equalTo(bean.getDatum())));
        assertThat(dto, hasProperty("tagesTyp", equalTo(bean.getTagesTyp())));
        assertThat(dto, hasProperty("projektNummer", equalTo(bean.getProjektNummer())));
        assertThat(dto, hasProperty("projektName", equalTo(bean.getProjektName())));
        assertThat(dto, hasProperty("kategorien", containsInAnyOrder(Strings.toStringArray(bean.getKategorien())) ));
        assertThat(dto, hasProperty("zaehlsituation", equalTo(bean.getZaehlsituation())));
        assertThat(dto, hasProperty("zaehlsituationErweitert", equalTo(bean.getZaehlsituationErweitert())));
        assertThat(dto, hasProperty("zaehlIntervall", equalTo(bean.getZaehlIntervall())));
        assertThat(dto, hasProperty("wetter", equalTo(bean.getWetter())));
        assertThat(dto, hasProperty("artDerZaehlung", equalTo(bean.getArtDerZaehlung())));
        assertThat(dto, hasProperty("zaehldauer", equalTo(bean.getZaehldauer())));
        assertThat(dto, hasProperty("schulZeiten", equalTo(bean.getSchulZeiten())));
        assertThat(dto, hasProperty("suchwoerter", equalTo(bean.getSuchwoerter())));

        assertThat(dto, hasProperty("sonderzaehlung", is(bean.getSonderzaehlung().equals(ZaehlungMapper.SONDERZAEHLUNG))));
    }
}
