package rs.ac.uns.ftn.svtvezbe07.service.implementation;

import rs.ac.uns.ftn.svtvezbe07.exceptionhandling.exception.LoadingException;
import rs.ac.uns.ftn.svtvezbe07.exceptionhandling.exception.StorageException;
import rs.ac.uns.ftn.svtvezbe07.indexmodel.GroupIndex;
import rs.ac.uns.ftn.svtvezbe07.indexrepository.GroupIndexRepository;
import rs.ac.uns.ftn.svtvezbe07.model.entity.Group;
import rs.ac.uns.ftn.svtvezbe07.repository.GroupRepository;
import rs.ac.uns.ftn.svtvezbe07.service.FileService;
import rs.ac.uns.ftn.svtvezbe07.service.IndexingGroupService;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.tika.Tika;
import org.apache.tika.language.detect.LanguageDetector;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.UUID;
@Service
@RequiredArgsConstructor
public class IndexingGroupServiceImpl implements IndexingGroupService {
    private final GroupIndexRepository groupIndexRepository;
    private final GroupRepository groupRepository;
    private final FileService fileService;
    private final LanguageDetector languageDetector;

    @Override
    @Transactional
    public String indexGroup(MultipartFile documentFile) {
        var newGroup = new Group();
        var newGroupIndex = new GroupIndex();

        var name = Objects.requireNonNull(documentFile.getOriginalFilename()).split("\\.")[0];
        newGroup.setName(name);
        newGroupIndex.setName(name);

        var documentContent = extractDocumentContent(documentFile);
        if (detectLanguage(documentContent).equals("SR")) {
            newGroupIndex.setDescription(documentContent);
        } else {
            newGroupIndex.setDescription(documentContent);
        }

        var serverFilename = fileService.store(documentFile, UUID.randomUUID().toString());
        newGroupIndex.setName(serverFilename);
        newGroup.setName(serverFilename);

        newGroup.setDescription(detectMimeType(documentFile));
        var savedGroup = groupRepository.save(newGroup);

        newGroupIndex.setId(savedGroup.getId().toString());
        groupIndexRepository.save(newGroupIndex);

        return serverFilename;
    }

    @Override
    @Transactional
    public String indexDocument(Group group) {
        GroupIndex newEntity = new GroupIndex(group.getId().toString(), group.getName(), group.getDescription(), group.getCreationDate().toLocalDate(), group.getGroupAdmin(), group.getDeleted());

        groupIndexRepository.save(newEntity);

        return group.getName();
    }

    private String extractDocumentContent(MultipartFile multipartPdfFile) {
        String documentContent;
        try (var pdfFile = multipartPdfFile.getInputStream()) {
            var pdDocument = PDDocument.load(pdfFile);
            var textStripper = new PDFTextStripper();
            documentContent = textStripper.getText(pdDocument);
            pdDocument.close();
        } catch (IOException e) {
            throw new LoadingException("Error while trying to load PDF file content.");
        }

        return documentContent;
    }

    private String detectLanguage(String text) {
        var detectedLanguage = languageDetector.detect(text).getLanguage().toUpperCase();
        if (detectedLanguage.equals("HR")) {
            detectedLanguage = "SR";
        }

        return detectedLanguage;
    }

    private String detectMimeType(MultipartFile file) {
        var contentAnalyzer = new Tika();

        String trueMimeType;
        String specifiedMimeType;
        try {
            trueMimeType = contentAnalyzer.detect(file.getBytes());
            specifiedMimeType =
                    Files.probeContentType(Path.of(Objects.requireNonNull(file.getOriginalFilename())));
        } catch (IOException e) {
            throw new StorageException("Failed to detect mime type for file.");
        }

        if (!trueMimeType.equals(specifiedMimeType) &&
                !(trueMimeType.contains("zip") && specifiedMimeType.contains("zip"))) {
            throw new StorageException("True mime type is different from specified one, aborting.");
        }

        return trueMimeType;
    }
}
