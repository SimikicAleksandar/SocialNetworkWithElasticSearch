package rs.ac.uns.ftn.svtvezbe07.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.svtvezbe07.indexmodel.GroupIndex;
import rs.ac.uns.ftn.svtvezbe07.model.entity.Group;

import javax.transaction.Transactional;

@Service
public interface SearchGroupsService {
    @Transactional
    String indexDocument(Group group);
    GroupIndex updateGroupPostNum(Long id);
    Page<GroupIndex> simpleSearch(List<String> keywords, Pageable pageable);
    Page<GroupIndex> rangeSearch(Integer min, Integer max, Pageable pageable);
    Page<GroupIndex> advancedSearch(List<String> expression, Pageable pageable);
}
