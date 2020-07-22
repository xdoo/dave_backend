package de.muenchen.dave.domain.dtos;

import lombok.Data;

@Data
public class BearbeiteZaehlstelleDTO {

    String nummer;
    String name;
    int stadtbezirkNummer;
    double lat;
    double lng;
    String strassen;
    String geographie;
    String suchwoerter;

}
