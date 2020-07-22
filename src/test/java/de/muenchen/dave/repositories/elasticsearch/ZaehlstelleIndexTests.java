package de.muenchen.dave.repositories.elasticsearch;

import de.muenchen.dave.domain.elasticsearch.Zaehlstelle;
import de.muenchen.dave.domain.elasticsearch.ZaehlstelleRandomFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@Slf4j
public class ZaehlstelleIndexTests {

    @Autowired
    ZaehlstelleIndex repo;

    @BeforeEach
    public void clearRepo() {
        this.repo.deleteAll();
    }

    @Test
    public void testCrud () {

        Zaehlstelle z = ZaehlstelleRandomFactory.getOne();
        String id = z.getId();

        // save
        try {
            this.repo.save(z);
        } catch (Exception e) {
            log.error("Fehler - " + e.toString());
        }

        assertThat(this.repo.count(), is(equalTo(1L)));
        assertThat(this.repo.existsById(id), is(true));

        // read
        Optional<Zaehlstelle> oz1 = this.repo.findById(id);
        assertThat(oz1.isPresent(), is(true));
        assertThat(oz1.get().getStadtbezirk(), is(equalTo(z.getStadtbezirk())));
        assertThat(oz1.get().getZaehlungen().size(), is(equalTo(z.getZaehlungen().size())));

        // update
        oz1.get().setName(z.getName() + "_TEST");
        this.repo.save(oz1.get());
        Optional<Zaehlstelle> oz2 = this.repo.findById(id);
        assertThat(oz2.isPresent(), is(true));
        assertThat(oz2.get().getName(), is(equalTo(z.getName()+ "_TEST")));

        // delete
        this.repo.deleteById(id);
        assertThat(this.repo.existsById(id), is(false));
    }
}
