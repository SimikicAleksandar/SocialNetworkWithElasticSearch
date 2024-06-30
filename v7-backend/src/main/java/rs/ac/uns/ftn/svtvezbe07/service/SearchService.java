package rs.ac.uns.ftn.svtvezbe07.service;

import rs.ac.uns.ftn.svtvezbe07.indexmodel.DummyIndex;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public interface SearchService {

    Page<DummyIndex> simpleSearch(List<String> keywords, Pageable pageable);

    Page<DummyIndex> advancedSearch(List<String> expression, Pageable pageable);
}
