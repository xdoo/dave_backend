package de.muenchen.dave.domain.dtos;

import com.github.javafaker.Faker;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class BearbeiteZaehlungDTORandomFactory {

    public static BearbeiteZaehlungDTO getOne() {
        BearbeiteZaehlungDTO dto = new BearbeiteZaehlungDTO();

        Faker faker = Faker.instance(new Locale("test"));

        Date date = Faker.instance().date().between(new GregorianCalendar(1990, 0, 1).getTime(), new Date());
        LocalDate d = date.toInstant().atZone((ZoneId.systemDefault())).toLocalDate();

        dto.setDatum(d);
        dto.setGrund(faker.resolve("zaehlung.grund"));
        dto.setWetter(faker.resolve("zaehlung.wetter"));
        dto.setArtDerZaehlung(faker.resolve("zaehlung.art"));
        dto.setZaehlZeit(faker.resolve("zaehlung.zeit"));
        dto.setSchulZeiten(faker.resolve("zaehlung.schule"));
        dto.setKategorien("KFZ, LKW, RAD");
        dto.setTagesTyp(faker.resolve("zaehlung.tag"));
        dto.setSuchwoerter("Test, Foo, Bar");

        return dto;
    }
}
