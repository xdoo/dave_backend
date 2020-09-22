package de.muenchen.dave.domain.dtos;

import lombok.Data;

import java.util.List;

@Data
public class SucheComplexSuggestsDTO {

    List<SucheWordSuggestDTO> wordVorschlaege;
    List<SucheCounterSuggestDTO> zaehlstellenVorschlaege;
    List<SucheCountSuggestDTO> zaehlungenVorschlaege;
}
