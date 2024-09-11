//package rs.ac.uns.ftn.svtvezbe07.controller;
//
//import rs.ac.uns.ftn.svtvezbe07.dto.DummyDocumentFileDTO;
//import rs.ac.uns.ftn.svtvezbe07.dto.DummyDocumentFileResponseDTO;
//import rs.ac.uns.ftn.svtvezbe07.service.IndexingService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/index")
//@RequiredArgsConstructor
//public class IndexController {
//
//    private final IndexingService indexingService;
//
//    @PostMapping
//    @ResponseStatus(HttpStatus.CREATED)
//    public DummyDocumentFileResponseDTO addDocumentFile(
//        @ModelAttribute DummyDocumentFileDTO documentFile) {
//        var serverFilename = indexingService.indexDocument(documentFile.file());
//        return new DummyDocumentFileResponseDTO(serverFilename);
//    }
//}
