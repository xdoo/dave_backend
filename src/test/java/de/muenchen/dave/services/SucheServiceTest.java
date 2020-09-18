package de.muenchen.dave.services;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class SucheServiceTest {

    private SucheService service = new SucheService(zaehlstelleIndex);

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

}
