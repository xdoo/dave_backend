package de.muenchen.dave.domain.dtos;

import com.github.javafaker.Faker;
import com.google.common.collect.Lists;

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
        dto.setTagesTyp(faker.resolve("zaehlung.tag"));
        dto.setProjektNummer(Faker.instance().number().digits(10));
        dto.setProjektName(Faker.instance().funnyName().name());
        dto.setSonderzaehlung(true);
        dto.setKategorien(Lists.newArrayList("KFZ", "LKW", "RAD"));
        dto.setZaehlsituation(faker.resolve("zaehlung.grund"));
        dto.setZaehlsituationErweitert("Erweiterte Zählsituation");
        dto.setZaehlIntervall(15);
        dto.setWetter(faker.resolve("zaehlung.wetter"));
        dto.setArtDerZaehlung(faker.resolve("zaehlung.art"));
        dto.setZaehldauer(faker.resolve("zaehlung.zeit"));
        dto.setSchulZeiten(faker.resolve("zaehlung.schule"));
        dto.setSuchwoerter("Test, Foo, Bar");

//        dto.setKnotenarme(BearbeiteKnotenarmDTORandomFactory.getSome());
//        dto.setFahrbeziehungen(BearbeiteFahrbeziehungKreuzungDTORandomFactory.getSome());

        return dto;
    }
}
