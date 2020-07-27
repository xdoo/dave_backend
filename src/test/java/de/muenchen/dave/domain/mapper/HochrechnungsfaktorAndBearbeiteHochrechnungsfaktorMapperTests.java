package de.muenchen.dave.domain.mapper;

import de.muenchen.dave.domain.dtos.BearbeiteHochrechnungsfaktorDTO;
import de.muenchen.dave.domain.dtos.BearbeiteHochrechnungsfaktorDTORandomFactory;
import de.muenchen.dave.domain.elasticsearch.Hochrechnungsfaktor;
import de.muenchen.dave.domain.elasticsearch.HochrechnungsfaktorRandomFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@Slf4j
public class HochrechnungsfaktorAndBearbeiteHochrechnungsfaktorMapperTests {

    @Autowired HochrechnungsfaktorAndBearbeiteHochrechnungsfaktorMapper mapper;

    @Test
    public void testBean2dto() {
        Hochrechnungsfaktor bean = HochrechnungsfaktorRandomFactory.getOne();
        BearbeiteHochrechnungsfaktorDTO dto = this.mapper.bean2dto(bean);

        assertThat(dto, hasProperty("kfz", equalTo(bean.getKfz())));
        assertThat(dto, hasProperty("sv", equalTo(bean.getSv())));
        assertThat(dto, hasProperty("gv", equalTo(bean.getGv())));
    }

    @Test
    public void testDto2bean() {
        BearbeiteHochrechnungsfaktorDTO dto = BearbeiteHochrechnungsfaktorDTORandomFactory.getOne();
        Hochrechnungsfaktor bean = this.mapper.dto2bean(dto);

        assertThat(bean, hasProperty("kfz", equalTo(dto.getKfz())));
        assertThat(bean, hasProperty("sv", equalTo(dto.getSv())));
        assertThat(bean, hasProperty("gv", equalTo(dto.getGv())));

    }
}
