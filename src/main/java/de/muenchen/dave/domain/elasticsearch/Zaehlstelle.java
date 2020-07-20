package de.muenchen.dave.domain.elasticsearch;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.GeoPointField;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import java.util.List;

@Data
@NoArgsConstructor
@Document(indexName = "zaehlstelle")
public class Zaehlstelle {

    @Id
    String id;

    @Field(type = FieldType.Text)
    String nummer;

    @Field(type = FieldType.Text)
    String name;

    @Field(type = FieldType.Text)
    String stadtbezirk;

    @Field(type = FieldType.Integer)
    int stadtbezirkNummer;

    @GeoPointField
    GeoPoint punkt;

    @Field(type = FieldType.Integer)
    int letzteZaehlungMonatNummer;

    @Field(type = FieldType.Text)
    String letzteZaehlungMonat;

    List<Integer> zeahljahre;

    @Field(type = FieldType.Text)
    String grundLetzteZaehlung;

    List<String> strassen;

    List<String> geographie;

    @Field(type = FieldType.Text)
    String suchwoerter;

    List<Zaehlung> zeahlungen;

}
