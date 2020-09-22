package de.muenchen.dave.services;

import de.muenchen.dave.domain.elasticsearch.Zaehlstelle;
import de.muenchen.dave.repositories.elasticsearch.ZaehlstelleIndex;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

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
        this.service.complexSuggest("Nym");
        this.service.complexSuggest("Nym Heid");
        this.service.complexSuggest("Nym Heid Deva");
    }

    List<Zaehlstelle> createSampleData() {
        Zaehlstelle z1 = new Zaehlstelle();
        z1.setId("01");
        z1.setNummer("Z01");
        z1.setName("");
    }

}
