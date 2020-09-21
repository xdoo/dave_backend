package de.muenchen.dave.services;

import de.muenchen.dave.repositories.elasticsearch.ZaehlstelleIndex;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class SucheServiceSpringTests {

    @Autowired
    ZaehlstelleIndex repo;

    @Autowired
    SucheService service;

    @Test
    public void testComplexSuggest() {
        this.service.complexSuggest("Nym");
        this.service.complexSuggest("Nym Heid");
        this.service.complexSuggest("Nym Heid Deva");
    }


}
