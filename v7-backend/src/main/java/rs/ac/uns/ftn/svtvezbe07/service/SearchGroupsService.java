package rs.ac.uns.ftn.svtvezbe07.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.svtvezbe07.indexmodel.GroupIndex;
@Service
public interface SearchGroupsService {
    Page<GroupIndex> simpleSearch(List<String> keywords, Pageable pageable);

    Page<GroupIndex> advancedSearch(List<String> expression, Pageable pageable);
}
