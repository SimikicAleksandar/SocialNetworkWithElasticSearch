package rs.ac.uns.ftn.svtvezbe07.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import rs.ac.uns.ftn.svtvezbe07.dto.DummyDocumentFileResponseDTO;
import rs.ac.uns.ftn.svtvezbe07.service.IndexingGroupService;

@RestController
@RequestMapping("/api/group-index")
@RequiredArgsConstructor
public class GroupIndexController {
    private final IndexingGroupService groupIndexingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DummyDocumentFileResponseDTO addGroupFile(@ModelAttribute MultipartFile documentFile) {
        var serverFilename = groupIndexingService.indexGroup(documentFile);
        return new DummyDocumentFileResponseDTO(serverFilename);
    }
}
