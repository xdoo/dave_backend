package de.muenchen.dave.domain.mapper;

import de.muenchen.dave.domain.dtos.BearbeiteHochrechnungsfaktorDTO;
import de.muenchen.dave.domain.elasticsearch.Hochrechnungsfaktor;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HochrechnungsfaktorMapper {

    /**
     * bearbeite auf bean
     *
     * @param dto
     * @return
     */
    public Hochrechnungsfaktor bearbeiteDto2bean(BearbeiteHochrechnungsfaktorDTO dto);

    /**
     * bean auf bearbeite
     *
     * @param bean
     * @return
     */
    public BearbeiteHochrechnungsfaktorDTO bean2bearbeiteDto(Hochrechnungsfaktor bean);
}
