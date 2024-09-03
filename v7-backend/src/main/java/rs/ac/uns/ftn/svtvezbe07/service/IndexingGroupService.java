package rs.ac.uns.ftn.svtvezbe07.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import rs.ac.uns.ftn.svtvezbe07.model.entity.Group;

import javax.transaction.Transactional;

public interface IndexingGroupService {
    String indexGroup(MultipartFile documentFile);

    @Transactional
    String indexDocument(Group group);
}
