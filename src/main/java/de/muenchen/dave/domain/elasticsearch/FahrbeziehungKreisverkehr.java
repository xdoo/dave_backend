package de.muenchen.dave.domain.elasticsearch;

import lombok.Data;

@Data
/**
 * Beim Kreisverkehr gibt es keine von zu Beziehung. Hier wird
 * lediglich definiert, welcher Knotenarm gemssen und was an ihm gemessen
 * wird. So kann es pro Knotenarm maximal drei Fahrbeziehungen geben.
 */
public class FahrbeziehungKreisverkehr extends Fahrbeziehung {

    int knotenarm;
    boolean hinein;
    boolean heraus;
    boolean vorbei;
}
