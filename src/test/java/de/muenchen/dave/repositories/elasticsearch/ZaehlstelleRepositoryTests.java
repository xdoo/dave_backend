package de.muenchen.dave.repositories.elasticsearch;

import com.google.common.collect.Lists;
import de.muenchen.dave.domain.elasticsearch.Zaehlstelle;
import de.muenchen.dave.domain.elasticsearch.Zaehlung;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

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
                "Bla Blub, Blobber",
                Lists.newArrayList(createDefaultZaehlung())
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
            String suchwoerter,
            List<Zaehlung> zaehlungen
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
        z.setZeahlungen(zaehlungen);

        return z;
    }

    public static Zaehlung createDefaultZaehlung () {
        return createZaehlung(
                "Z__1",
                LocalDate.parse("2019-11-20"),
                2019,
                "November",
                Lists.newArrayList("PKW", "LKW", "Fahrrad"),
                "Änderung Ampelschlatung",
                "sonnig",
                "24h",
                "Werktag",
                "Herbst",
                "Schule",
                ""

        );
    }

    public static Zaehlung createZaehlung (
            String id,
            LocalDate datum,
            int jahr,
            String monat,
            List<String> kategorien,
            String grund,
            String wetter,
            String artDerZaehlung,
            String tagesTyp,
            String jahreszeit,
            String schulzeiten,
            String suchwoerter
    ) {
        Zaehlung z = new Zaehlung();

        z.setId(id);
        z.setDatum(datum);
        z.setJahr(jahr);
        z.setMonat(monat);
        z.setKategorien(kategorien);
        z.setGrund(grund);
        z.setWetter(wetter);
        z.setArtDerZaehlung(artDerZaehlung);
        z.setTagesTyp(tagesTyp);
        z.setJahreszeit(jahreszeit);
        z.setSchulZeiten(schulzeiten);
        z.setSuchwörter(suchwoerter);

        return z;
    }

}
