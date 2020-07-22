package de.muenchen.dave.domain.elasticsearch;

import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.GeoPointField;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import java.util.List;
import java.util.Locale;

@Data
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

    @Field(type = FieldType.Integer)
    int letzteZaehlungJahr;

    List<Integer> zaehljahre;

    @Field(type = FieldType.Text)
    String grundLetzteZaehlung;

    List<String> strassen;

    List<String> geographie;

    @Field(type = FieldType.Text)
    String suchwoerter;

    List<Zaehlung> zaehlungen;

}
