package de.muenchen.dave.domain.dtos;

import com.github.javafaker.Faker;

public class BearbeiteFahrbeziehungKreuzungDTORandomFactory {

    public static BearbeiteFahrbeziehungKreuzungDTO getOne() {
        BearbeiteFahrbeziehungKreuzungDTO dto = new BearbeiteFahrbeziehungKreuzungDTO();

        dto.setNach(Faker.instance().number().numberBetween(1, 8));
        dto.setVon(Faker.instance().number().numberBetween(1, 8));

        return dto;
    }
}
