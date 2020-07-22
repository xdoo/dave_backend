package de.muenchen.dave.domain.dtos;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BearbeiteZaehlungDTO {

    LocalDate datum;
    String grund;
    String wetter;
    String artDerZaehlung;
    String zaehlZeit;
    String SchulZeiten;
    String suchwoerter;
}
