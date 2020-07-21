package de.muenchen.dave.domain.elasticsearch;

import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.primitives.Doubles;
import com.google.common.primitives.Ints;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import java.util.Locale;
import java.util.Map;

public class ZaehlstelleRandomFactory {

    private final static FakeValuesService service = new FakeValuesService(new Locale("test"), new RandomService());

    private final static String BEZIRKNUMMER = "bezirknummer";
    private final static String BEZIRK = "bezirk";
    private final static String LAT = "lat";
    private final static String LNG = "lng";
    private final static String NAME = "name";
    private final static String PLATZ = "platz";
    private final static String STRASSEN = "strassen";
    private final static String GEO = "geo";

    public static Zaehlstelle getOne() {

        Map<String,String> x = (Map<String, String>) service.fetch("zaehlstelle.stellen");

        Zaehlstelle z = new Zaehlstelle();

        z.setId(Faker.instance().crypto().md5());
        z.setNummer(Faker.instance().number().digits(10));
        z.setName(x.get(NAME));
        z.setStadtbezirk(x.get(BEZIRK));
        z.setStadtbezirkNummer(Ints.tryParse(x.get(BEZIRKNUMMER)));
        z.setPunkt(new GeoPoint(Doubles.tryParse(x.get(LAT)), Doubles.tryParse(x.get(LNG))));
        z.setStrassen(Lists.newArrayList(Splitter.on(",").trimResults().omitEmptyStrings().split(x.get(STRASSEN))));
        z.setGeographie(Lists.newArrayList(Splitter.on(",").trimResults().omitEmptyStrings().split(x.get(GEO))));

        return z;
    }

}
