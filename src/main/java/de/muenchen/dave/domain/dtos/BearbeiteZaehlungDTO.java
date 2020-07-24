package de.muenchen.dave.domain.dtos;

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
    String kategorien;
    String zaehlsituation;
    String zaehlsituationErweitert;
    String zaehlIntervall;
    String wetter;
    String artDerZaehlung;
    String zaehldauer;
    String schulZeiten;
    String suchwoerter;
    List<BearbeiteKnotenarmDTO> knotenarme;

}
