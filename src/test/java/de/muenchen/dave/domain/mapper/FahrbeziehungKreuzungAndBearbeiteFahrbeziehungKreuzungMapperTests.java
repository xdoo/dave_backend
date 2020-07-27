package de.muenchen.dave.domain.mapper;

import de.muenchen.dave.domain.dtos.BearbeiteFahrbeziehungKreuzungDTO;
import de.muenchen.dave.domain.dtos.BearbeiteFahrbeziehungKreuzungDTORandomFactory;
import de.muenchen.dave.domain.elasticsearch.FahrbeziehungKreuzung;
import de.muenchen.dave.domain.elasticsearch.FahrbeziehungKreuzungRandomFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@Slf4j
public class FahrbeziehungKreuzungAndBearbeiteFahrbeziehungKreuzungMapperTests {

    @Autowired
    FahrbeziehungKreuzungAndBearbeiteFahrbeziehungKreuzungMapper mapper;

    @Test
    public void testDto2bean() {
        BearbeiteFahrbeziehungKreuzungDTO dto = BearbeiteFahrbeziehungKreuzungDTORandomFactory.getOne();
        FahrbeziehungKreuzung bean = this.mapper.dto2bean(dto);

        assertThat(bean.getVon(), is(equalTo(dto.getVon())));
        assertThat(bean.getNach(), is(equalTo(dto.getNach())));

    }

    @Test
    public void testBean2dto() {
        FahrbeziehungKreuzung bean = FahrbeziehungKreuzungRandomFactory.getOne();
        BearbeiteFahrbeziehungKreuzungDTO dto = this.mapper.bean2dto(bean);

        assertThat(dto.getVon(), is(equalTo(bean.getVon())));
        assertThat(dto.getNach(), is(equalTo(bean.getNach())));
    }
}
