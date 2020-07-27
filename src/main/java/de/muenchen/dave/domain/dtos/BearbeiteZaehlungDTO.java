package de.muenchen.dave.domain.dtos;

import de.muenchen.dave.domain.elasticsearch.Fahrbeziehung;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class BearbeiteZaehlungDTO {

    LocalDate datum;
    String tagesTyp;
    String projektNummer;
    String projektName;
    boolean sonderzaehlung;
    List<String> kategorien;
    String zaehlsituation;
    String zaehlsituationErweitert;
    int zaehlIntervall;
    String wetter;
    String artDerZaehlung;
    String zaehldauer;
    String schulZeiten;
    String suchwoerter;
//    List<BearbeiteKnotenarmDTO> knotenarme;
//    List<BearbeiteFahrbeziehungDTO> fahrbeziehungen;
}
