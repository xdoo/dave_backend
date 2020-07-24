package de.muenchen.dave.domain.elasticsearch;

import lombok.Data;

@Data
public class FahrbeziehungKreuzung extends Fahrbeziehung {

    int von;
    int nach;
}
