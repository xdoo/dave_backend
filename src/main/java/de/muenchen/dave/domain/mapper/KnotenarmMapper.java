package de.muenchen.dave.domain.mapper;

import de.muenchen.dave.domain.dtos.BearbeiteKnotenarmDTO;
import de.muenchen.dave.domain.elasticsearch.Knotenarm;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface KnotenarmMapper {

    /**
     * bearbeiten auf bean
     *
     * @param dto
     * @return
     */
    Knotenarm bearbeitenDto2bean(BearbeiteKnotenarmDTO dto);

    /**
     * bean auf bearbeiten
     *
     * @param bean
     * @return
     */
    BearbeiteKnotenarmDTO bean2bearbeitenDto(Knotenarm bean);
}
