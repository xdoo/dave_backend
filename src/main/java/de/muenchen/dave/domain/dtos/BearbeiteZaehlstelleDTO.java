package de.muenchen.dave.domain.dtos;

import lombok.Data;

@Data
public class BearbeiteZaehlstelleDTO {

    String nummer;
    String name;
    String stadtbezirkNummer;
    double lat;
    double lng;
    String strassen;
    String geographie;
    String suchwörter;

}
