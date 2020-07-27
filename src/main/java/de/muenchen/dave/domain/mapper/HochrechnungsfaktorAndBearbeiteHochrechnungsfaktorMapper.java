package de.muenchen.dave.domain.mapper;

import de.muenchen.dave.domain.dtos.BearbeiteHochrechnungsfaktorDTO;
import de.muenchen.dave.domain.elasticsearch.Hochrechnungsfaktor;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HochrechnungsfaktorAndBearbeiteHochrechnungsfaktorMapper {

    public Hochrechnungsfaktor dto2bean(BearbeiteHochrechnungsfaktorDTO dto);
    public BearbeiteHochrechnungsfaktorDTO bean2dto(Hochrechnungsfaktor bean);
}
