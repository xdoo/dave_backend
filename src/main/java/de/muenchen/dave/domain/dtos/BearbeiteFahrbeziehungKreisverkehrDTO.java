package de.muenchen.dave.domain.dtos;

import lombok.Data;

@Data
public class BearbeiteFahrbeziehungKreisverkehrDTO extends BearbeiteFahrbeziehungDTO {

    int knotenarm;
    boolean hinein;
    boolean heraus;
    boolean vorbei;
}
