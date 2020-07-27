package de.muenchen.dave.domain.mapper;

import de.muenchen.dave.domain.dtos.BearbeiteKnotenarmDTO;
import de.muenchen.dave.domain.elasticsearch.Knotenarm;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface KnotenarmAndBearbeiteKnotenarmMapper {

    Knotenarm dto2bean(BearbeiteKnotenarmDTO dto);
    BearbeiteKnotenarmDTO bean2dto(Knotenarm bean);
}
