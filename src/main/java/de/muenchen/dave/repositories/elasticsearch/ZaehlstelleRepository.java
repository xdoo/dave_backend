package de.muenchen.dave.repositories.elasticsearch;

import de.muenchen.dave.domain.elasticsearch.Zaehlstelle;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ZaehlstelleRepository extends ElasticsearchRepository<Zaehlstelle, String> {
}
