package de.muenchen.dave.services;

import com.google.common.collect.Lists;
import de.muenchen.dave.domain.elasticsearch.Zaehlstelle;
import de.muenchen.dave.domain.elasticsearch.Zaehlung;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Slf4j
public class SucheServiceTests {

    private SucheService service = new SucheService(null);

    @Test
    public void testIsDate() {
        assertThat(this.service.isDate("12.12.2011"), is(true));
        assertThat(this.service.isDate("1.1.2011"), is(true));
        assertThat(this.service.isDate("1.12.11"), is(true));
        assertThat(this.service.isDate("Datum"), is(false));
    }

    @Test
    public void testRewriteDate() {
        assertThat(this.service.rewriteDate("11.12.2011"), is(equalTo("20111211")));
        assertThat(this.service.rewriteDate("1.2.2011"), is(equalTo("20110201")));
        assertThat(this.service.rewriteDate("01.04.11"), is(equalTo("20110401")));
    }

    @Test
    public void testCreateQueryString() {
        assertThat(this.service.createQueryString("foo bar"), is(equalTo("foo* bar*")));
        assertThat(this.service.createQueryString("foo bar 1.12.17"), is(equalTo("foo* bar* 20171201")));
    }

    @Test
    public void testExtractDate() {
        Optional<String> od = this.service.extractDate("Nymphenburg 12.1.16 Foo");
        assertThat(od.isPresent(), is(true));
        assertThat(od.get(), is(equalTo("12.01.2016")));
    }

    @Test
    public void testCheckZaehlstelleForZaehlung() {
        Zaehlstelle zs1 = new Zaehlstelle();
        Zaehlung z1 = new Zaehlung();
        z1.setDatum(LocalDate.parse("2017-04-03"));
        z1.setProjektName("Foo");
        Zaehlung z2 = new Zaehlung();
        z2.setDatum(LocalDate.parse("2014-07-20"));
        z2.setProjektName("foo");
        Zaehlung z3 = new Zaehlung();
        z3.setDatum(LocalDate.parse("2019-08-04"));
        z3.setProjektName("bar");
        zs1.setZaehlungen(Lists.newArrayList(z1, z2, z3));

        // Test mit Datum
        Optional<Zaehlung> optionalZaehlung1 = this.service.checkZaehlstelleForZaehlung(zs1, "Nymphenburg 20.7.14 bla");
        assertThat(optionalZaehlung1.isPresent(), is(true));
        assertThat(optionalZaehlung1.get(), is(equalTo(z2)));

        // Test mit falschem Datum
        Optional<Zaehlung> optionalZaehlung2 = this.service.checkZaehlstelleForZaehlung(zs1, "Nymphenburg 27.03.2018");
        assertThat(optionalZaehlung2.isPresent(), is(false));

        // Test mit "Foo" (groß)
        Optional<Zaehlung> optionalZaehlung3 = this.service.checkZaehlstelleForZaehlung(zs1, "Nymphenburg Foo Bla");
        assertThat(optionalZaehlung3.isPresent(), is(true));
        assertThat(optionalZaehlung3.get(), is(equalTo(z1)));

        // Test mit "foo" (klein)
        Optional<Zaehlung> optionalZaehlung4 = this.service.checkZaehlstelleForZaehlung(zs1, "Nymphenburg foo Bla");
        assertThat(optionalZaehlung4.isPresent(), is(true));
        assertThat(optionalZaehlung4.get(), is(equalTo(z2)));

        // Test mit falschem Text
        Optional<Zaehlung> optionalZaehlung5 = this.service.checkZaehlstelleForZaehlung(zs1, "Nymphenburg Foobar Bla");
        assertThat(optionalZaehlung5.isPresent(), is(false));
    }

}
