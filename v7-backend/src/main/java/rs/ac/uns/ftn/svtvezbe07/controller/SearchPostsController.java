package rs.ac.uns.ftn.svtvezbe07.controller;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.ac.uns.ftn.svtvezbe07.dto.SearchQueryDTO;
import rs.ac.uns.ftn.svtvezbe07.indexmodel.GroupIndex;
import rs.ac.uns.ftn.svtvezbe07.indexmodel.PostIndex;
import rs.ac.uns.ftn.svtvezbe07.service.SearchPostsService;

@RestController
@RequestMapping("/api/searchPosts")
@RequiredArgsConstructor
public class SearchPostsController {
    private final SearchPostsService searchPostsService;

    @PostMapping("/simple")
    public Page<PostIndex> simpleSearch(@RequestBody SearchQueryDTO simpleSearchQuery,
                                        Pageable pageable) {
        return searchPostsService.simpleSearch(simpleSearchQuery.keywords(), pageable);
    }

    @PostMapping("/advanced")
    public Page<PostIndex> advancedSearch(@RequestBody SearchQueryDTO advancedSearchQuery,
                                           Pageable pageable) {
        return searchPostsService.advancedSearch(advancedSearchQuery.keywords(), pageable);
    }
}
