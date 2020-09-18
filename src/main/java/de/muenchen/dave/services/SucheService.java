package de.muenchen.dave.services;

import de.muenchen.dave.repositories.elasticsearch.ZaehlstelleIndex;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.SimpleQueryStringBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
@Slf4j
public class SucheService {

    private static Pattern DE_DATE = Pattern.compile("^\\d{1,2}.\\d{1,2}.\\d{2,4}$");

    private final ZaehlstelleIndex zaehlstelleIndex;

    public SucheService(ZaehlstelleIndex zaehlstelleIndex) {
        this.zaehlstelleIndex = zaehlstelleIndex;
    }

    public void complexSuggest(String query) {
        String q = this.createQueryString(query);
        SimpleQueryStringBuilder queryBuilder = new SimpleQueryStringBuilder(q);
        log.info("query '{}'", q);

        queryBuilder.field("nummer", 5);
        queryBuilder.field("stadtbezirk", 2);
        queryBuilder.field("strassen", 4);
        queryBuilder.field("geographie", 2);
        // zählungen
        queryBuilder.field("zaehlungen.datum", 4);
        queryBuilder.field("zaehlungen.jahr");
        queryBuilder.field("zaehlungen.jahreszeit");
        queryBuilder.field("zaehlungen.projektName", 4);
        queryBuilder.field("zaehlungen.kategorien");
        queryBuilder.field("zaehlungen.wetter");
        queryBuilder.field("zaehlungen.zaehlsituation");
        queryBuilder.field("zaehlungen.suchwoerter");

        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                .withPageable(PageRequest.of(0, 3))
                .build();


    }

    public String createQueryString(String query) {
        StringBuilder queryBuilder = new StringBuilder();
        String[] words = query.split(" ");
        for(int i = 0; i < words.length; i++) {
            if(this.isDate(words[i])){
                queryBuilder.append(this.rewriteDate(words[i])).append(" ");
            } else {
                queryBuilder.append(words[i]).append("* ");
            }
        }
        return queryBuilder.toString();
    }

    /**
     * Checkt, ob ein Suchstring ein Datum ist.
     *
     * @param x
     * @return
     */
    public boolean isDate(String x) {
        return DE_DATE.matcher(x).matches();
    }

    /**
     * Wandelt das deutsche Datum in einen Datumsstring um
     * der von ES interpretiert werden kann.
     *
     * @param d
     * @return
     */
    public String rewriteDate(String d) {
        String[] x = d.split("\\.");
        String t = x[0].length() < 2 ? 0 + x[0] : x[0];
        String m = x[1].length() < 2 ? 0 + x[1] : x[1];
        String j = x[2].length() < 4 ? 20 + x[2] : x[2];
        return j+m+t;
    }

}
