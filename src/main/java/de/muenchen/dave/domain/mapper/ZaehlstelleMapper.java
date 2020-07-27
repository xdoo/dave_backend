package de.muenchen.dave.domain.mapper;

import de.muenchen.dave.domain.dtos.BearbeiteZaehlstelleDTO;
import de.muenchen.dave.domain.elasticsearch.Zaehlstelle;
import org.mapstruct.*;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

@Mapper(componentModel = "spring")
public interface ZaehlstelleMapper {

    /**
     * bean auf bearbeiten
     *
     * @param bean
     * @return
     */
    BearbeiteZaehlstelleDTO BearbeiteDto(Zaehlstelle bean);

    /**
     * bearbeiten auf bean
     *
     * @param dto
     * @return
     */
    Zaehlstelle bearbeiteDto2bean(BearbeiteZaehlstelleDTO dto);

    @AfterMapping
    default void toZaehlstelle(@MappingTarget Zaehlstelle z, BearbeiteZaehlstelleDTO dto) {
        z.setPunkt(new GeoPoint(dto.getLat(), dto.getLng()));
    }

    @AfterMapping
    default  void toBearbeiteZaehlstelleDTO(@MappingTarget BearbeiteZaehlstelleDTO dto, Zaehlstelle z) {
        dto.setLat(z.getPunkt().getLat());
        dto.setLng(z.getPunkt().getLon());
    }

}
