package de.muenchen.dave.services;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import de.muenchen.dave.domain.elasticsearch.Zaehlung;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class IndexServiceUtils {

    public final static String WINTER = "Winter";
    public final static String FRUEHLING = "Frühling";
    public final static String SOMMER = "Sommer";
    public final static String HERBST = "Herbst";


    public static List<String> splitStrings(String s) {
        return Splitter.on(",").omitEmptyStrings().trimResults().splitToList(s);
    }

    /**
     * Liefert den Stadbezirk Namen für die Stadbezirk Nummer.
     *
     * @param n
     * @return
     */
    public static String leseStadtbezirk(int n) {
        Map<Integer, String> bs = new HashMap<>();

        bs.put(1, "Altstadt-Lehel");
        bs.put(2, "Ludwigsvorstadt-Isarvorstadt");
        bs.put(3, "Maxvorstadt");
        bs.put(4, "Schwabing-West");
        bs.put(5, "Au-Haidhausen");
        bs.put(6, "Sendling");
        bs.put(7, "Sendling-Westpark");
        bs.put(8, "Schwanthalerhöhe");
        bs.put(9, "Neuhausen-Nymphenburg");
        bs.put(10, "Moosach");
        bs.put(11, "Milbertshofen-Am Hart");
        bs.put(12, "Schwabing-Freimann");
        bs.put(13, "Bogenhausen");
        bs.put(14, "Berg am Laim");
        bs.put(15, "Trudering-Riem");
        bs.put(16, "Ramersdorf-Perlach");
        bs.put(17, "Obergiesing-Fasangarten");
        bs.put(18, "Untergiesing-Harlaching");
        bs.put(19, "Thalkirchen-Obersendling-Forstenried-Fürstenried-Solln");
        bs.put(20, "Hadern");
        bs.put(21, "Pasing-Obermenzing");
        bs.put(22, "Aubing-Lochhausen-Langwied");
        bs.put(23, "Allach-Untermenzing");
        bs.put(24, "Feldmoching-Hasenbergl");
        bs.put(25, "Laim");

        if(bs.containsKey(n)) {
            return bs.get(n);
        } else {
            log.error("No 'stadtbezirk' has been found for key '{}'.", n);
            return "";
        }
    }

    /**
     * Ermittelt die Jahreszeit für ein Datum.
     *
     * @param d
     * @return
     */
    public static String jahreszeitenDetector(LocalDate d) {
        int m = d.getMonthValue();
        if(m < 3 || m > 11) {
            return WINTER;
        }

        if(m > 2 && m < 6) {
            return FRUEHLING;
        }

        if(m > 5 && m < 9) {
            return SOMMER;
        }

        if(m > 8 && m < 12) {
            return HERBST;
        }

        log.error("Local Date with more than 12 month?!!?");
        return "";
    }

    /**
     * Erstellt eine Liste von Jahren aus allen Zählungen.
     *
     * @param zs
     * @return
     */
    public static List<Integer> getZaehljahre(List<Zaehlung> zs) {
        return zs.stream().map(z -> z.getJahr()).collect(Collectors.toList());
    }

    /**
     * Holt die neuste Zählung aus der Liste.
     *
     * @param zs
     * @return
     */
    public static Zaehlung getLetzteZaehlung (List<Zaehlung> zs) {
        OrderingByDate ordering = new OrderingByDate();
        zs.sort(ordering);

        if(!zs.isEmpty()) {
            return zs.get(zs.size()-1);
        } else {
            log.warn("List of 'Zaehlungen' is empty. I can't give you the first entry");
            return null;
        }
    }

    private static class OrderingByDate extends Ordering<Zaehlung> {
        @Override
        public int compare(Zaehlung z1, Zaehlung z2) {
            return  z1.getDatum().compareTo(z2.getDatum());
        }
    }
}
