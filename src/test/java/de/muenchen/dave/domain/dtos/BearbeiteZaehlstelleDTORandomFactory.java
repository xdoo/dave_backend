package de.muenchen.dave.domain.dtos;

import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import com.google.common.primitives.Doubles;
import com.google.common.primitives.Ints;

import java.util.Locale;
import java.util.Map;

public class BearbeiteZaehlstelleDTORandomFactory {

    private final static FakeValuesService service = new FakeValuesService(new Locale("test"), new RandomService());

    private final static String BEZIRKNUMMER = "bezirknummer";
    private final static String BEZIRK = "bezirk";
    private final static String LAT = "lat";
    private final static String LNG = "lng";
    private final static String NAME = "name";
    private final static String STRASSEN = "strassen";
    private final static String GEO = "geo";

    public static BearbeiteZaehlstelleDTO getOne() {

        Map<String,String> x = (Map<String, String>) service.fetch("zaehlstelle.stellen");

        BearbeiteZaehlstelleDTO dto = new BearbeiteZaehlstelleDTO();

        dto.setNummer(Faker.instance().number().digits(10));
        dto.setName(x.get(NAME));
        dto.setStadtbezirkNummer(Ints.tryParse(x.get(BEZIRKNUMMER)));
        dto.setLat(Doubles.tryParse(x.get(LAT)));
        dto.setLng(Doubles.tryParse(x.get(LNG)));
        dto.setStrassen(x.get(STRASSEN));
        dto.setGeographie(x.get(GEO));
        dto.setSuchwoerter(Faker.instance().animal().name());

        return dto;
    }
}
