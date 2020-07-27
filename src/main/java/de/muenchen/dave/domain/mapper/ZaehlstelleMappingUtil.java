package de.muenchen.dave.domain.mapper;

import de.muenchen.dave.domain.dtos.BearbeiteZaehlstelleDTO;
import de.muenchen.dave.domain.elasticsearch.Zaehlstelle;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

public class ZaehlstelleMappingUtil {

    public static void setPunkt(Zaehlstelle z, BearbeiteZaehlstelleDTO dto) {
        z.setPunkt(new GeoPoint(dto.getLat(), dto.getLng()));
    }

    public static void setLatLng(BearbeiteZaehlstelleDTO dto, Zaehlstelle z) {
        dto.setLat(z.getPunkt().getLat());
        dto.setLng(z.getPunkt().getLon());
    }

}
