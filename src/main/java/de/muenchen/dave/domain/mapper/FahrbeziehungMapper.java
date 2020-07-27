package de.muenchen.dave.domain.mapper;

import de.muenchen.dave.domain.dtos.BearbeiteFahrbeziehungKreuzungDTO;
import de.muenchen.dave.domain.elasticsearch.FahrbeziehungKreuzung;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FahrbeziehungMapper {

    /**
     * bearbeite auf bean (für Kreuzung)
     *
     * @param dto
     * @return
     */
    FahrbeziehungKreuzung bearbeiteFahrbeziehungKreuzungdto2bean(BearbeiteFahrbeziehungKreuzungDTO dto);

    /**
     * bean auf bearbeite (für Kreuzung)
     *
     * @param bean
     * @return
     */
    BearbeiteFahrbeziehungKreuzungDTO bean2bearbeiteFahrbeziehungKreuzungDto(FahrbeziehungKreuzung bean);
}
