package rs.ac.uns.ftn.svtvezbe07.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.svtvezbe07.indexmodel.PostIndex;
import rs.ac.uns.ftn.svtvezbe07.model.entity.Post;

import javax.transaction.Transactional;
import java.util.List;
@Service
public interface SearchPostsService {
//    @Transactional
//    String indexDocument(Post post);

    PostIndex updatePostLikeNum(Long id);

    Page<PostIndex> simpleSearch(List<String> keywords, Pageable pageable);

    Page<PostIndex> rangeSearch(Integer min, Integer max, Pageable pageable);

    Page<PostIndex> advancedSearch(List<String> expression, Pageable pageable);
}
