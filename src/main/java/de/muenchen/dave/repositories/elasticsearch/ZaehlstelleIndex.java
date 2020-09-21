package de.muenchen.dave.repositories.elasticsearch;

import de.muenchen.dave.domain.elasticsearch.Zaehlstelle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ZaehlstelleIndex extends ElasticsearchRepository<Zaehlstelle, String> {

    @Query("{\n" +
            "    \"simple_query_string\": {\n" +
            "      \"fields\": [\"nummer^5\", \"stadtbezirk^2\", \"strassen^4\", \"geographie^2\", \"zaehlungen.datum^4\", \"zaehlungen.jahreszeit\", \"zaehlungen.projektName^4\", \"zaehlungen.kategorien\", \"zaehlungen.jahr\", \"zaehlungen.wetter\", \"zaehlungen.zaehlsituation\", \"zaehlungen.suchwoerter^3\"],\n" +
            "      \"query\": \"?0\",\n" +
            "      \"analyze_wildcard\": true,\n" +
            "      \"default_operator\": \"AND\",\n" +
            "      \"lenient\": true\n" +
            "    }\n" +
            "  }")
    Page<Zaehlstelle> suggestSearch(String query, Pageable pageable);

}
