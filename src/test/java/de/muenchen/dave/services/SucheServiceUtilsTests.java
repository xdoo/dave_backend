package de.muenchen.dave.services;

import de.muenchen.dave.domain.elasticsearch.Zaehlung;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Slf4j
public class SucheServiceUtilsTests {

    @Test
    public void testJahreszeitenDetector() {
        LocalDate w1 = LocalDate.of(2019, 1, 1);
        LocalDate w2 = LocalDate.of(2019, 12, 1);
        LocalDate f = LocalDate.of(2019, 3, 1);
        LocalDate s = LocalDate.of(2019, 6, 1);
        LocalDate h = LocalDate.of(2019, 9, 1);

        assertThat(SucheServiceUtils.jahreszeitenDetector(w1), is(equalTo(SucheServiceUtils.WINTER)));
        assertThat(SucheServiceUtils.jahreszeitenDetector(w2), is(equalTo(SucheServiceUtils.WINTER)));
        assertThat(SucheServiceUtils.jahreszeitenDetector(f), is(equalTo(SucheServiceUtils.FRUEHLING)));
        assertThat(SucheServiceUtils.jahreszeitenDetector(s), is(equalTo(SucheServiceUtils.SOMMER)));
        assertThat(SucheServiceUtils.jahreszeitenDetector(h), is(equalTo(SucheServiceUtils.HERBST)));

    }

    @Test
    public void testGetZaehljahre() {
        Zaehlung z1 = new Zaehlung();
        z1.setJahr(2010);

        Zaehlung z2 = new Zaehlung();
        z2.setJahr(2015);

        Zaehlung z3 = new Zaehlung();
        z3.setJahr(2020);

        assertThat(SucheServiceUtils.getZaehljahre(Lists.newArrayList(z1, z2, z3)), containsInAnyOrder(2010, 2015, 2020));
    }

    @Test
    public void testGetLetzteZaehlung() {
        Zaehlung z1 = new Zaehlung();
        z1.setDatum(LocalDate.of(2010, 1, 1));

        Zaehlung z2 = new Zaehlung();
        z2.setDatum(LocalDate.of(2015, 1, 1));

        Zaehlung z3 = new Zaehlung();
        z3.setDatum(LocalDate.of(2020, 1, 1));

        assertThat(SucheServiceUtils.getLetzteZaehlung(Lists.newArrayList(z1, z2, z3)), is(equalTo(z3)));
    }

}
