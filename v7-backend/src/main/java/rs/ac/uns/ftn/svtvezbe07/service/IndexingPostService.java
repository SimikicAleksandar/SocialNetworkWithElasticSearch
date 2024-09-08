package rs.ac.uns.ftn.svtvezbe07.service;
import org.springframework.web.multipart.MultipartFile;
import rs.ac.uns.ftn.svtvezbe07.model.entity.Post;

import javax.transaction.Transactional;
public interface IndexingPostService {
    String indexPost(MultipartFile documentFile);

    @Transactional
    String indexDocument(Post post);
}
