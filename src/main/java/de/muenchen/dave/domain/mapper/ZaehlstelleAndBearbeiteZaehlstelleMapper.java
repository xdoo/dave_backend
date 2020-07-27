package de.muenchen.dave.domain.mapper;

import de.muenchen.dave.domain.dtos.BearbeiteZaehlstelleDTO;
import de.muenchen.dave.domain.elasticsearch.Zaehlstelle;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ZaehlstelleAndBearbeiteZaehlstelleMapper {

    BearbeiteZaehlstelleDTO bean2dto(Zaehlstelle bean);
    Zaehlstelle dto2bean(BearbeiteZaehlstelleDTO dto);

    @AfterMapping
    default void setPunkt(@MappingTarget Zaehlstelle z, BearbeiteZaehlstelleDTO dto) {
        ZaehlstelleMappingUtil.setPunkt(z,dto);
    }

    @AfterMapping
    default  void setLatLng(@MappingTarget BearbeiteZaehlstelleDTO dto, Zaehlstelle z) {
        ZaehlstelleMappingUtil.setLatLng(dto, z);
    }

}
