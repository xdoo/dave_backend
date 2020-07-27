package de.muenchen.dave.domain.elasticsearch;

import com.github.javafaker.Faker;
import com.google.common.collect.Lists;
import de.muenchen.dave.domain.mapper.ZaehlungMapper;
import de.muenchen.dave.services.IndexServiceUtils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.*;

public class ZaehlungRandomFactory {

    public static Zaehlung getOne() {
        Zaehlung z = new Zaehlung();

        Faker faker = Faker.instance(new Locale("test"));

        // create random date
        Date date = Faker.instance().date().between(new GregorianCalendar(1990, 0, 1).getTime(), new Date());
        LocalDate d = date.toInstant().atZone((ZoneId.systemDefault())).toLocalDate();

        z.setId(UUID.randomUUID().toString());
        z.setDatum(d);
        z.setJahr(d.getYear());
        z.setMonat(d.getMonth().getDisplayName(TextStyle.FULL_STANDALONE, Locale.GERMANY));
        z.setTagesTyp(d.getDayOfWeek().getDisplayName(TextStyle.FULL_STANDALONE, Locale.GERMANY));
        z.setJahreszeit(IndexServiceUtils.jahreszeitenDetector(d));

        z.setProjektNummer(Faker.instance().number().digits(10));
        z.setProjektName(Faker.instance().funnyName().name());
        z.setSonderzaehlung(ZaehlungMapper.SONDERZAEHLUNG);
        z.setKategorien(generateKategorien());
        z.setZaehlsituation(faker.resolve("zaehlung.grund"));
        z.setZaehlsituationErweitert("Die erweiterte Zählsituation");
        z.setZaehlIntervall(15);
        z.setWetter(faker.resolve("zaehlung.wetter"));
        z.setArtDerZaehlung(faker.resolve("zaehlung.art"));
        z.setZaehldauer(faker.resolve("zaehlung.zeit"));
        z.setSchulZeiten(faker.resolve("zaehlung.schule"));
        z.setSuchwoerter("foo bar foobar");

        return z;
    }

    public static List<Zaehlung> getSome() {
        List<Zaehlung> zs = new ArrayList<>();
        int x = Faker.instance().number().numberBetween(1, 10);

        for(int i = 0; i < x; i++) {
            zs.add(getOne());
        }
        return zs;
    }

    /**
     * Creates a random list of "kategoorien" (PKW, LKW,...)
     *
     * @return
     */
    private static List<String> generateKategorien() {
        int x = Faker.instance().number().numberBetween(1, 10);
        Set<String> k = new HashSet<>();

        for(int i = 0; i < x; i++) {
            k.add(Faker.instance(new Locale("test")).resolve("zaehlung.kategorie"));
        }

        return Lists.newArrayList(k);
    }

}
