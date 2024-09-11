package rs.ac.uns.ftn.svtvezbe07.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import rs.ac.uns.ftn.svtvezbe07.model.entity.File;

@Service
public interface IndexingService {
    File indexDocument(MultipartFile documentFile, String type, Long id);
}
