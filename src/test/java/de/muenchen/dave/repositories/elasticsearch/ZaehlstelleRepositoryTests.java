package de.muenchen.dave.repositories.elasticsearch;

import com.google.common.collect.Lists;
import de.muenchen.dave.domain.elasticsearch.Zaehlstelle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

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
        this.repo.save(this.createZaehlstelle());
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

    private Zaehlstelle createZaehlstelle() {
        Zaehlstelle z = new Zaehlstelle();

        z.setId(Z_ID);
        z.setNummer("10234X3");
        z.setName("Pelkovenstrasse");
        z.setStadtbezirkNummer(10);
        z.setStadtbezirk(Z_STADTBEZIRK);
        z.setPunkt(new GeoPoint(48.18116, 11.51150));
        z.setLetzteZaehlungMonatNummer(11);
        z.setLetzteZaehlungMonat("November");
        z.setZeahljahre(Lists.newArrayList(1999, 2004, 2006, 2010, 2013, 2018, 2019));
        z.setGrundLetzteZaehlung(Z_GRUND);
        z.setStrassen(Lists.newArrayList("Pelkovenstrasse", "Dachauer Straße", "Baubergerstraße", "Bunzlauer Strasse"));
        z.setGeographie(Lists.newArrayList("Tram"));
        z.setSuchwoerter("Bla Blub, Blobber");

        return z;
    }

}
