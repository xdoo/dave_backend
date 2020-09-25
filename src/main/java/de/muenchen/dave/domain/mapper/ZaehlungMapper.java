package de.muenchen.dave.domain.mapper;

import de.muenchen.dave.domain.dtos.BearbeiteZaehlungDTO;
import de.muenchen.dave.domain.dtos.SucheZaehlungSuggestDTO;
import de.muenchen.dave.domain.elasticsearch.Zaehlung;
import de.muenchen.dave.services.IndexServiceUtils;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

@Mapper(componentModel = "spring")
public interface ZaehlungMapper {

    final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public final static String SONDERZAEHLUNG = "Sonderzählung";

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
    default void toZaehlung(@MappingTarget Zaehlung bean, BearbeiteZaehlungDTO dto) {
        bean.setJahr(dto.getDatum().getYear());
        bean.setMonat(dto.getDatum().getMonth().getDisplayName(TextStyle.FULL, Locale.GERMANY));
        bean.setJahreszeit(IndexServiceUtils.jahreszeitenDetector(dto.getDatum()));

        if(dto.isSonderzaehlung()){
            bean.setSonderzaehlung(SONDERZAEHLUNG);
        }
    }

    @AfterMapping
    default void toBearbeiteZaehlungDTO(@MappingTarget BearbeiteZaehlungDTO dto, Zaehlung bean) {
        if(bean.getSonderzaehlung().equals(SONDERZAEHLUNG)) {
            dto.setSonderzaehlung(true);
        }
    }

    /**
     * bean auf SucheZaehlungSuggestDto
     *
     * @param bean
     * @return
     */
    public SucheZaehlungSuggestDTO bean2SucheZaehlungSuggestDto(Zaehlung bean);

    @AfterMapping
    default void toSucheCountSuggestDTO(@MappingTarget SucheZaehlungSuggestDTO dto, Zaehlung bean) {
        dto.setText(bean.getDatum().format(DATE_TIME_FORMATTER) + " " + bean.getProjektName());
    }

}
