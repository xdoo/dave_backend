package de.muenchen.dave.services;

import com.google.common.collect.Lists;
import de.muenchen.dave.domain.dtos.SucheComplexSuggestsDTO;
import de.muenchen.dave.domain.dtos.SucheZaehlstelleSuggestDTO;
import de.muenchen.dave.domain.elasticsearch.Zaehlstelle;
import de.muenchen.dave.domain.elasticsearch.Zaehlung;
import de.muenchen.dave.repositories.elasticsearch.ZaehlstelleIndex;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@Slf4j
public class SucheServiceSpringTests {

    @Autowired
    ZaehlstelleIndex repo;

    @Autowired
    SucheService service;

    @BeforeEach
    public void clearRepo() {
        this.repo.deleteAll();
    }

    @Test
    public void testComplexSuggest() {
        this.repo.saveAll(this.createSampleData());

        SucheComplexSuggestsDTO dto1 = this.service.complexSuggest("Moo");
        assertThat(dto1.getZaehlstellenVorschlaege(), is(not(empty())));
        assertThat(dto1.getZaehlstellenVorschlaege(), containsInAnyOrder(
                Matchers.<SucheZaehlstelleSuggestDTO>hasProperty("id", is("01")),
                Matchers.<SucheZaehlstelleSuggestDTO>hasProperty("id", is("05"))
        ));
    }

    List<Zaehlstelle> createSampleData() {
        // z1
        Zaehlstelle z1 = new Zaehlstelle();
        z1.setId("01");
        z1.setNummer("Z01");
        z1.setName("Z1");
        z1.setStadtbezirk("Moosach");

        Zaehlung z1_1 = new Zaehlung();
        z1_1.setProjektName("Projektz11");
        z1_1.setDatum(LocalDate.parse("2016-05-07"));

        Zaehlung z1_2 = new Zaehlung();
        z1_2.setProjektName("Projektz12");
        z1_2.setDatum(LocalDate.parse("2014-12-10"));

        z1.setZaehlungen(Lists.newArrayList(z1_1, z1_2));

        // z2
        Zaehlstelle z2 = new Zaehlstelle();
        z2.setId("02");
        z2.setNummer("Z02");
        z2.setName("Z2");
        z2.setStadtbezirk("Sendling");

        Zaehlung z2_1 = new Zaehlung();
        z2_1.setProjektName("Projektz21");
        z2_1.setDatum(LocalDate.parse("2003-10-20"));

        z2.setZaehlungen(Lists.newArrayList(z2_1));

        // z3
        Zaehlstelle z3 = new Zaehlstelle();
        z3.setId("03");
        z3.setNummer("Z03");
        z3.setName("Z3");
        z3.setStadtbezirk("Schwabing");

        Zaehlung z3_1 = new Zaehlung();
        z3_1.setProjektName("Projektz31");
        z3_1.setDatum(LocalDate.parse("2008-02-07"));

        Zaehlung z3_2 = new Zaehlung();
        z3_2.setProjektName("Projektz32");
        z3_2.setDatum(LocalDate.parse("2014-06-18"));

        z3.setZaehlungen(Lists.newArrayList(z3_1, z3_2));

        // z4
        Zaehlstelle z4 = new Zaehlstelle();
        z4.setId("04");
        z4.setNummer("Z04");
        z4.setName("Z4");
        z4.setStadtbezirk("Bogenhausen");

        Zaehlung z4_1 = new Zaehlung();
        z4_1.setProjektName("Projektz41");
        z4_1.setDatum(LocalDate.parse("2009-03-07"));

        Zaehlung z4_2 = new Zaehlung();
        z4_2.setProjektName("Projektz42");
        z4_2.setDatum(LocalDate.parse("2016-08-13"));

        Zaehlung z4_3 = new Zaehlung();
        z4_3.setProjektName("Projektz43");
        z4_3.setDatum(LocalDate.parse("2019-11-09"));

        z4.setZaehlungen(Lists.newArrayList(z4_1, z4_2, z4_3));

        // z5
        Zaehlstelle z5 = new Zaehlstelle();
        z5.setId("05");
        z5.setNummer("Z05");
        z5.setName("Z5");
        z5.setStadtbezirk("Moosach");

        return Lists.newArrayList(z1, z2, z3, z4, z5);
    }

}
