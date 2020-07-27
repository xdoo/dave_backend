package de.muenchen.dave.domain.dtos;

import lombok.Data;

import java.util.List;

@Data
public class BearbeiteZaehlstelleDTO {

    String nummer;
    String name;
    int stadtbezirkNummer;
    double lat;
    double lng;
    List<String> strassen;
    List<String> geographie;
    String suchwoerter;

}
