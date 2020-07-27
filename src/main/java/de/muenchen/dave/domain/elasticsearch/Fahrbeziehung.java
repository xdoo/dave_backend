package de.muenchen.dave.domain.elasticsearch;

import lombok.Data;

@Data
public class Fahrbeziehung {

    String id;
    Hochrechnungsfaktor hochrechnungsfaktor;

    // Knoten-Kanten-Modell
    String vonknotvonstrnr;
    String nachknotvonstrnr;
    String von_strnr;
    String vonknotennachstrnr;
    String nachknotnachstrnr;
    String nach_strnr;

}
