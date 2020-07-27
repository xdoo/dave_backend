package de.muenchen.dave.services;

import de.muenchen.dave.domain.dtos.BearbeiteZaehlstelleDTO;
import de.muenchen.dave.domain.dtos.BearbeiteZaehlstelleDTORandomFactory;
import de.muenchen.dave.domain.elasticsearch.Zaehlstelle;
import de.muenchen.dave.exceptions.BrokenInfrastructureException;
import de.muenchen.dave.repositories.elasticsearch.ZaehlstelleIndex;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@Slf4j
public class IndexServiceSpringTest {

    @Autowired IndexService service;
    @Autowired ZaehlstelleIndex index;

    @Test
    public void testErstelleZaehlstelle() throws BrokenInfrastructureException {
        BearbeiteZaehlstelleDTO dto = BearbeiteZaehlstelleDTORandomFactory.getOne();
        String id = this.service.erstelleZaehlstelle(dto);

        assertThat(id, is(notNullValue()));
        Optional<Zaehlstelle> oz = this.index.findById(id);
        assertThat(oz.isPresent(), is(true));
        assertThat(oz.get().getNummer(), is(equalTo(dto.getNummer())));
    }

}
