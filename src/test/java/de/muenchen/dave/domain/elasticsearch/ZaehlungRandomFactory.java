package de.muenchen.dave.domain.elasticsearch;

import com.github.javafaker.Faker;
import com.google.common.collect.Lists;
import de.muenchen.dave.services.SucheServiceUtils;

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

        z.setId(Faker.instance().crypto().md5());
        z.setDatum(d);
        z.setJahr(d.getYear());
        z.setMonat(d.getMonth().getDisplayName(TextStyle.FULL_STANDALONE, Locale.GERMANY));
        z.setTagesTyp(d.getDayOfWeek().getDisplayName(TextStyle.FULL_STANDALONE, Locale.GERMANY));
        z.setJahreszeit(SucheServiceUtils.jahreszeitenDetector(d));

        z.setKategorien(generateKategorien());
        z.setGrund(faker.resolve("zaehlung.grund"));
        z.setWetter(faker.resolve("zaehlung.wetter"));
        z.setArtDerZaehlung(faker.resolve("zaehlung.art"));
        z.setZaehlZeit(faker.resolve("zaehlung.zeit"));
        z.setSchulZeiten(faker.resolve("zaehlung.schule"));
        z.setSuchwoerter(Faker.instance().chuckNorris().fact());

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
