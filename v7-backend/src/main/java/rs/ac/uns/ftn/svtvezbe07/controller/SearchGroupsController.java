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
import rs.ac.uns.ftn.svtvezbe07.service.SearchGroupsService;
@RestController
@RequestMapping("/api/searchGroups")
@RequiredArgsConstructor
public class SearchGroupsController {

    private final SearchGroupsService searchGroupsService;

    @PostMapping("/simple")
    public Page<GroupIndex> simpleSearch(@RequestBody SearchQueryDTO simpleSearchQuery,
                                         Pageable pageable) {
        return searchGroupsService.simpleSearch(simpleSearchQuery.keywords(), pageable);
    }

    @PostMapping("/advanced")
    public Page<GroupIndex> advancedSearch(@RequestBody SearchQueryDTO advancedSearchQuery,
                                           Pageable pageable) {
        return searchGroupsService.advancedSearch(advancedSearchQuery.keywords(), pageable);
    }
}
