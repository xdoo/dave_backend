package de.muenchen.dave.domain.mapper;

import de.muenchen.dave.domain.dtos.BearbeiteKnotenarmDTO;
import de.muenchen.dave.domain.dtos.BearbeiteKnotenarmDTORandomFactory;
import de.muenchen.dave.domain.elasticsearch.Knotenarm;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@Slf4j
public class KnotenarmAndBearbeiteKnotenarmMapperTests {

    @Autowired KnotenarmAndBearbeiteKnotenarmMapper mapper;

    @Test
    public void testDto2bean() {
        BearbeiteKnotenarmDTO dto = BearbeiteKnotenarmDTORandomFactory.getOne();
        Knotenarm bean = this.mapper.dto2bean(dto);

        assertThat(bean, hasProperty("nummer", equalTo(dto.getNummer())));
        assertThat(bean, hasProperty("strassenname", equalTo(dto.getStrassenname())));
    }

    @Test
    public void testBean2dto() {

    }
}
