package de.muenchen.dave.domain.mapper;

import de.muenchen.dave.domain.dtos.BearbeiteZaehlstelleDTO;
import de.muenchen.dave.domain.dtos.SucheZaehlstelleSuggestDTO;
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
    BearbeiteZaehlstelleDTO bearbeiteDto(Zaehlstelle bean);

    /**
     * bearbeiten auf bean
     *
     * @param dto
     * @return
     */
    Zaehlstelle bearbeiteDto2bean(BearbeiteZaehlstelleDTO dto);

    @AfterMapping
    default void toZaehlstelle(@MappingTarget Zaehlstelle bean, BearbeiteZaehlstelleDTO dto) {
        bean.setPunkt(new GeoPoint(dto.getLat(), dto.getLng()));
    }

    @AfterMapping
    default  void toBearbeiteZaehlstelleDTO(@MappingTarget BearbeiteZaehlstelleDTO dto, Zaehlstelle bean) {
        dto.setLat(bean.getPunkt().getLat());
        dto.setLng(bean.getPunkt().getLon());
    }

    /**
     * bean auf counterSuggestDto
     *
     * @param bean
     * @return
     */
    SucheZaehlstelleSuggestDTO bean2SucheCounterSuggestDto(Zaehlstelle bean);

    @AfterMapping
    default void toSucheCounterSuggestDto(@MappingTarget SucheZaehlstelleSuggestDTO dto, Zaehlstelle bean) {
        dto.setText(bean.getNummer() + " " + bean.getName());
    }

}
