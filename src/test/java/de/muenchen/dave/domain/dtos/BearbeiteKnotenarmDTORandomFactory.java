package de.muenchen.dave.domain.dtos;

import com.github.javafaker.Faker;

public class BearbeiteKnotenarmDTORandomFactory {

    public static  BearbeiteKnotenarmDTO getOne() {
        BearbeiteKnotenarmDTO dto = new BearbeiteKnotenarmDTO();

        dto.setNummer(Faker.instance().number().numberBetween(1, 8));
        dto.setStrassenname("Teststrasse_" + dto.getNummer());

        return dto;
    }
}
