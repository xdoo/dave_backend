package de.muenchen.dave.services;

import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import de.muenchen.dave.domain.elasticsearch.Zaehlung;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class SucheServiceUtils {

    public final static String WINTER = "Winter";
    public final static String FRUEHLING = "Frühling";
    public final static String SOMMER = "Sommer";
    public final static String HERBST = "Herbst";

    /**
     * Gives you the season for a given date.
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
     * Gives you a list with "zaehlung" years.
     *
     * @param zs
     * @return
     */
    public static List<Integer> getZaehljahre(List<Zaehlung> zs) {
        return zs.stream().map(z -> z.getJahr()).collect(Collectors.toList());
    }

    /**
     * Gives you the newest "zaehlung" from your list.
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
