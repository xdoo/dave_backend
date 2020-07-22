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

    String tagesTyp;

    String jahreszeit;

    List<String> kategorien;

    String grund;

    String wetter;

    String artDerZaehlung;

    String zaehlZeit;

    String schulZeiten; // Ferien, Schule

    String suchwörter;

}
