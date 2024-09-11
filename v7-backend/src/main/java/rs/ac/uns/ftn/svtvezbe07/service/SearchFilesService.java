package rs.ac.uns.ftn.svtvezbe07.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rs.ac.uns.ftn.svtvezbe07.indexmodel.FileIndex;

import java.util.List;

public interface SearchFilesService {
    Page<FileIndex> simpleSearch(List<String> keywords, Pageable pageable, String type);
}
