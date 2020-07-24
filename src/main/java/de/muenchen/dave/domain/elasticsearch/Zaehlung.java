package de.muenchen.dave.domain.elasticsearch;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
public class Zaehlung {

    String id;

    @Field(type = FieldType.Date, format = DateFormat.basic_date)
    LocalDate datum;

    int jahr;

    String monat;

    /**
     * Wochenende, Wochentag, Feiertag
     */
    String tagesTyp;

    String jahreszeit;

    String projektNummer;

    String projektName;

    String sonderzaehlung;

    List<String> kategorien;

    String zaehlsituation;

    String zaehlsituationErweitert;

    int zaehlIntervall;

    /**
     * sonnig, leicht bewölkt, stark bewölkt, regnerisch, Schnee
     */
    String wetter;

    /**
     * TODO - hier muss geschaut werden, welche Attribute aus der Liste tatsächlich
     * noch übrig bleiben.
     */
    String artDerZaehlung;

    /**
     * 2x4h, 16h, 24h
     */
    String zaehldauer;

    /**
     * Ferien, Schule
     */
    String schulZeiten;

    String suchwoerter;

    List<Knotenarm> knotenarme;

    List<Fahrbeziehung> fahrbeziehungen;

}
