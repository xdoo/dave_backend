package de.muenchen.dave.domain.mapper;

import de.muenchen.dave.domain.dtos.BearbeiteZaehlstelleDTO;
import de.muenchen.dave.domain.elasticsearch.Zaehlstelle;
import org.mapstruct.*;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

@Mapper(componentModel = "spring")
public interface ZaehlstelleAndBearbeiteZaehlstelleMapper {

    BearbeiteZaehlstelleDTO bean2dto(Zaehlstelle bean);
    Zaehlstelle dto2bean(BearbeiteZaehlstelleDTO dto);

    @AfterMapping
    default void setPunkt(@MappingTarget Zaehlstelle z, BearbeiteZaehlstelleDTO dto) {
        z.setPunkt(new GeoPoint(dto.getLat(), dto.getLng()));
    }

    @AfterMapping
    default  void setLatLng(@MappingTarget BearbeiteZaehlstelleDTO dto, Zaehlstelle z) {
        dto.setLat(z.getPunkt().getLat());
        dto.setLng(z.getPunkt().getLon());
    }

}
