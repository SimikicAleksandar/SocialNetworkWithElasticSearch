package rs.ac.uns.ftn.svtvezbe07.service;

import io.minio.GetObjectResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface FileService {

    String store(MultipartFile file, String serverFilename);

    void delete(String serverFilename);

    GetObjectResponse loadAsResource(String serverFilename);
}
