package de.muenchen.dave.domain.dtos;

import lombok.Data;

@Data
public class BearbeiteFahrbeziehungDTO {

    String vonknotvonstrnr;
    String nachknotvonstrnr;
    String von_strnr;
    String vonknotennachstrnr;
    String nachknotnachstrnr;
    String nach_strnr;
}
