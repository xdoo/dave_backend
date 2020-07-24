package de.muenchen.dave.domain.dtos;

import lombok.Data;

@Data
public class BearbeiteFahrbeziehungKreuzungDTO extends BearbeiteFahrbeziehungDTO {

    int von;
    int nach;
}
