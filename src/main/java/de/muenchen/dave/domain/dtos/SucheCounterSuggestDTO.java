package de.muenchen.dave.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
public class SucheCounterSuggestDTO {

    String text;
    String id;
}
