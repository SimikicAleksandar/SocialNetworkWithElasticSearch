package rs.ac.uns.ftn.svtvezbe07.service.implementation;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.common.unit.Fuzziness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHitSupport;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.svtvezbe07.indexmodel.FileIndex;
import rs.ac.uns.ftn.svtvezbe07.service.SearchFilesService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchFilesServiceImpl implements SearchFilesService {
    private final ElasticsearchOperations elasticsearchTemplate;

    @Override
    public Page<FileIndex> simpleSearch(List<String> keywords, Pageable pageable, String type) {
        var searchQueryBuilder =
                new NativeQueryBuilder().withQuery(buildSimpleSearchQuery(keywords, type))
                        .withPageable(pageable);

        return runQuery(searchQueryBuilder.build());
    }


    private Query buildSimpleSearchQuery(List<String> tokens, String type) {
        return BoolQuery.of(q -> q
                .must(mb -> mb.bool(b -> {
                    tokens.forEach(token -> {
                        // Match Query - full-text search with fuzziness
                        // Matches documents with fuzzy matching in "title" field
                        b.should(sb -> sb.match(
                                m -> m.field("title").fuzziness(Fuzziness.ONE.asString()).query(token)));

                        // Match Query - full-text search in other fields
                        // Matches documents with full-text search in other fields
                        b.should(sb -> sb.match(m -> m.field("content_sr").query(token)));
                        b.should(sb -> sb.match(m -> m.field("content_en").query(token)));
                    });
                    return b;
                }))
                .filter(fb -> {
                    if ("group".equals(type)) {
                        fb.exists(e -> e.field("group_id"));
                    } else if ("post".equals(type)) {
                        fb.exists(e -> e.field("post_id"));
                    }
                    return fb;
                })
        )._toQuery();
    }


    private Page<FileIndex> runQuery(NativeQuery searchQuery) {

        var searchHits = elasticsearchTemplate.search(searchQuery, FileIndex.class,
                IndexCoordinates.of("file_index"));

        var searchHitsPaged = SearchHitSupport.searchPageFor(searchHits, searchQuery.getPageable());

        return (Page<FileIndex>) SearchHitSupport.unwrapSearchHits(searchHitsPaged);
    }
}
