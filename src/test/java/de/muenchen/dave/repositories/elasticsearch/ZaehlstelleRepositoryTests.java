package de.muenchen.dave.repositories.elasticsearch;

import com.github.javafaker.Faker;
import com.google.common.collect.Lists;
import de.muenchen.dave.domain.elasticsearch.Zaehlstelle;
import de.muenchen.dave.domain.elasticsearch.ZaehlstelleRandomFactory;
import de.muenchen.dave.domain.elasticsearch.Zaehlung;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@Slf4j
public class ZaehlstelleRepositoryTests {

    @Autowired ZaehlstelleRepository repo;

    // Zaehlstelle
    private final static String Z_ID = "Z4711";
    private final static String Z_STADTBEZIRK = "Moosach";
    private final static String Z_GRUND = "Umbau der Trambahn";

    // Zaehlung
    private final static String Z__ID = "Z00001";

    @BeforeEach
    public void clearRepo() {
        this.repo.deleteAll();
    }

    @Test
    public void testCrud () {

        Zaehlstelle z = ZaehlstelleRandomFactory.getOne();
        String id = z.getId();

        // save
        this.repo.save(z);
        assertThat(this.repo.count(), is(equalTo(1L)));
        assertThat(this.repo.existsById(id), is(true));

        // read
        Optional<Zaehlstelle> oz1 = this.repo.findById(id);
        assertThat(oz1.isPresent(), is(true));
        assertThat(oz1.get().getStadtbezirk(), is(equalTo(z.getStadtbezirk())));

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
