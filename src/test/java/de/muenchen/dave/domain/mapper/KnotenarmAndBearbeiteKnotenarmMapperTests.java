package de.muenchen.dave.domain.mapper;

import de.muenchen.dave.domain.dtos.BearbeiteKnotenarmDTO;
import de.muenchen.dave.domain.dtos.BearbeiteKnotenarmDTORandomFactory;
import de.muenchen.dave.domain.elasticsearch.Knotenarm;
import de.muenchen.dave.domain.elasticsearch.KnotenarmRandomFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@Slf4j
public class KnotenarmAndBearbeiteKnotenarmMapperTests {

    @Autowired
    KnotenarmMapper mapper;

    @Test
    public void testDto2bean() {
        BearbeiteKnotenarmDTO dto = BearbeiteKnotenarmDTORandomFactory.getOne();
        Knotenarm bean = this.mapper.bearbeitenDto2bean(dto);

        assertThat(bean, hasProperty("nummer", equalTo(dto.getNummer())));
        assertThat(bean, hasProperty("strassenname", equalTo(dto.getStrassenname())));
    }

    @Test
    public void testBean2dto() {
        Knotenarm bean = KnotenarmRandomFactory.getOne();
        BearbeiteKnotenarmDTO dto = this.mapper.bean2bearbeitenDto(bean);

        assertThat(dto, hasProperty("nummer", equalTo(bean.getNummer())));
        assertThat(dto, hasProperty("strassenname", equalTo(bean.getStrassenname())));
    }
}
