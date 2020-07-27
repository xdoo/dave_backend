package de.muenchen.dave.domain.mapper;

import de.muenchen.dave.domain.dtos.BearbeiteZaehlungDTO;
import de.muenchen.dave.domain.elasticsearch.Zaehlung;
import de.muenchen.dave.services.IndexServiceUtils;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.time.format.TextStyle;
import java.util.Locale;

@Mapper(componentModel = "spring")
public interface ZaehlungMapper {

    /**
     * bearbeiten auf bean
     *
     * @param dto
     * @return
     */
    public Zaehlung bearbeiteDto2bean(BearbeiteZaehlungDTO dto);

    /**
     * bean auf bearbeiten
     *
     * @param bean
     * @return
     */
    public BearbeiteZaehlungDTO bean2BearbeiteDto(Zaehlung bean);

    @AfterMapping
    default void calculateValuesForZaehlung(@MappingTarget Zaehlung z, BearbeiteZaehlungDTO dto) {
        z.setJahr(dto.getDatum().getYear());
        z.setMonat(dto.getDatum().getMonth().getDisplayName(TextStyle.FULL, Locale.GERMANY));
        z.setJahreszeit(IndexServiceUtils.jahreszeitenDetector(dto.getDatum()));
    }

}
