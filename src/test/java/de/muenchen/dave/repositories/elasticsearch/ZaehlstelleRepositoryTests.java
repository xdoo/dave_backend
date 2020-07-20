package de.muenchen.dave.repositories.elasticsearch;

import com.google.common.collect.Lists;
import de.muenchen.dave.domain.elasticsearch.Zaehlstelle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
public class ZaehlstelleRepositoryTests {

    @Autowired ZaehlstelleRepository repo;

    private final static String Z_ID = "Z4711";
    private final static String Z_STADTBEZIRK = "Moosach";
    private final static String Z_GRUND = "Umbau der Trambahn";

    @BeforeEach
    public void clearRepo() {
        this.repo.deleteAll();
    }

    @Test
    public void testCrud() {

        // save
        this.repo.save(createDefaultZaehlstelle());
        assertThat(this.repo.count(), is(equalTo(1L)));
        assertThat(this.repo.existsById(Z_ID), is(true));

        // read
        Optional<Zaehlstelle> oz1 = this.repo.findById(Z_ID);
        assertThat(oz1.isPresent(), is(true));
        assertThat(oz1.get().getStadtbezirk(), is(equalTo(Z_STADTBEZIRK)));

        // update
        String grund = "Änderung der Ampelschaltung";
        Zaehlstelle z1 = oz1.get();
        z1.setGrundLetzteZaehlung(grund);
        this.repo.save(z1);
        Optional<Zaehlstelle> oz2 = this.repo.findById(Z_ID);
        assertThat(oz2.isPresent(), is(true));
        assertThat(oz2.get().getGrundLetzteZaehlung(), is(equalTo(grund)));

        // delete
        this.repo.deleteById(Z_ID);
        assertThat(this.repo.existsById(Z_ID), is(false));
    }

    public static Zaehlstelle createDefaultZaehlstelle() {
        return createZaehlstelle(
                Z_ID,
                "10234X3",
                "Pelkovenstrasse",
                10,
                Z_STADTBEZIRK,
                new GeoPoint(48.18116, 11.51150),
                11,
                "November",
                Lists.newArrayList(1999, 2004, 2006, 2010, 2013, 2018, 2019),
                Z_GRUND,
                Lists.newArrayList("Pelkovenstrasse", "Dachauer Straße", "Baubergerstraße", "Bunzlauer Strasse"),
                Lists.newArrayList("Tram"),
                "Bla Blub, Blobber"
        );
    }


    public static Zaehlstelle createZaehlstelle(
            String id,
            String nummer,
            String name,
            int stadtbezirkNummer,
            String stadtbezirk,
            GeoPoint punkt,
            int monatNummer,
            String monat,
            List<Integer> zaehljahre,
            String grund,
            List<String> strassen,
            List<String> geographie,
            String suchwoerter
    ) {
        Zaehlstelle z = new Zaehlstelle();

        z.setId(id);
        z.setNummer(nummer);
        z.setName(name);
        z.setStadtbezirkNummer(stadtbezirkNummer);
        z.setStadtbezirk(stadtbezirk);
        z.setPunkt(punkt);
        z.setLetzteZaehlungMonatNummer(monatNummer);
        z.setLetzteZaehlungMonat(monat);
        z.setZeahljahre(zaehljahre);
        z.setGrundLetzteZaehlung(grund);
        z.setStrassen(strassen);
        z.setGeographie(geographie);
        z.setSuchwoerter(suchwoerter);

        return z;
    }

}
