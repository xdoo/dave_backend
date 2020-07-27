package de.muenchen.dave.domain.mapper;

import de.muenchen.dave.domain.dtos.BearbeiteFahrbeziehungKreuzungDTO;
import de.muenchen.dave.domain.elasticsearch.FahrbeziehungKreuzung;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FahrbeziehungKreuzungAndBearbeiteFahrbeziehungKreuzungMapper {

    FahrbeziehungKreuzung dto2bean(BearbeiteFahrbeziehungKreuzungDTO dto);
    BearbeiteFahrbeziehungKreuzungDTO bean2dto(FahrbeziehungKreuzung bean);
}
