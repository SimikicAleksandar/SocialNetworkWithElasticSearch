package rs.ac.uns.ftn.svtvezbe07.service.implementation;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.json.JsonData;
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
import rs.ac.uns.ftn.svtvezbe07.exceptionhandling.exception.MalformedQueryException;
import rs.ac.uns.ftn.svtvezbe07.indexmodel.PostIndex;
import rs.ac.uns.ftn.svtvezbe07.indexrepository.PostIndexRepository;
import rs.ac.uns.ftn.svtvezbe07.service.SearchPostsService;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
@Service
@RequiredArgsConstructor
public class SearchPostsServiceImpl implements SearchPostsService {
    private final ElasticsearchOperations elasticsearchTemplate;
    private final PostIndexRepository indexRepository;

    @Override
    public PostIndex updatePostLikeNum(Long id) {
        var searchQuery = new NativeQueryBuilder()
                .withQuery(sb -> sb.match(
                        m -> m.field("id").query(id)))
                .build();
        Page<PostIndex> posts = runQuery(searchQuery);
        PostIndex post = posts.getContent().get(0);
        post.setLikeNumber(post.getLikeNumber() + 1);
        return indexRepository.save(post);
    }

    @Override
    public Page<PostIndex> simpleSearch(List<String> keywords, Pageable pageable) {
        var searchQueryBuilder =
                new NativeQueryBuilder().withQuery(buildSimpleSearchQuery(keywords))
                        .withPageable(pageable);

        return runQuery(searchQueryBuilder.build());
    }

    @Override
    public Page<PostIndex> rangeSearch(Integer min, Integer max, Pageable pageable) {
        var searchQueryBuilder =
                new NativeQueryBuilder().withQuery(buildRangeSearchQuery(min, max))
                        .withPageable(pageable);

        return runQuery(searchQueryBuilder.build());
    }

    @Override
    public Page<PostIndex> advancedSearch(List<String> expression, Pageable pageable) {
        if (expression.size() != 4) {
            throw new MalformedQueryException("Search query malformed.");
        }
        String operation = expression.get(3);
        expression.remove(3);

        var valueFIle = expression.get(2).split(":")[1];
        expression.remove(2);

        List<String> fileKeywords = new ArrayList<>();
        fileKeywords.add(valueFIle);
//        // Search for files related to groups
//
//        Page<FileIndex> fileResults = fileSearchService.simpleSearch(fileKeywords, pageable, "post");
//
//        // Extract post ids from fileResults
//        List<FieldValue> postIdsFromFiles = fileResults.getContent().stream()
//                .map(FileIndex::getPostId)
//                .distinct()
//                .map(FieldValue::of)
//                .toList();

        var searchQueryBuilder =
                new NativeQueryBuilder().withQuery(buildAdvancedSearchQuery(expression, operation))
                        .withPageable(pageable);

        return runQuery(searchQueryBuilder.build());
    }



    private Query buildSimpleSearchQuery(List<String> tokens) {
        return BoolQuery.of(q -> q.must(mb -> mb.bool(b -> {
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
            // Ensure documents have a post ID from the file search results

            return b;
        })))._toQuery();
    }

    private Query buildRangeSearchQuery(Integer min, Integer max) {
        return BoolQuery.of(q -> q.must(mb -> mb.bool(b -> {
            // Match Query - full-text search in other fields
            // Matches documents with full-text search in other fields
            b.must(sb -> sb.range(m -> m.field("like_number").gte(JsonData.of(min))));
            b.must(sb -> sb.range(m -> m.field("like_number").lte(JsonData.of(max))));

            return b;
        })))._toQuery();
    }

    private Query buildAdvancedSearchQuery(List<String> operands, String operation) {
        return BoolQuery.of(q -> q.must(mb -> mb.bool(b -> {
            var field1 = operands.get(0).split(":")[0];
            var value1 = operands.get(0).split(":")[1];
            var field2 = operands.get(1).split(":")[0];
            var value2 = operands.get(1).split(":")[1];

            switch (operation) {
                case "AND":
                    b.must(sb -> sb.match(
                            m -> m.field(field1).fuzziness(Fuzziness.ONE.asString()).query(value1)));
                    b.must(sb -> sb.match(m -> m.field(field2).query(value2)));
                    break;
                case "OR":
                    b.should(sb -> sb.match(
                            m -> m.field(field1).fuzziness(Fuzziness.ONE.asString()).query(value1)));
                    b.should(sb -> sb.match(m -> m.field(field2).query(value2)));
                    break;
            }

            return b;
        })))._toQuery();
    }

    private Page<PostIndex> runQuery(NativeQuery searchQuery) {

        var searchHits = elasticsearchTemplate.search(searchQuery, PostIndex.class,
                IndexCoordinates.of("post"));

        var searchHitsPaged = SearchHitSupport.searchPageFor(searchHits, searchQuery.getPageable());

        return (Page<PostIndex>) SearchHitSupport.unwrapSearchHits(searchHitsPaged);
    }
}
