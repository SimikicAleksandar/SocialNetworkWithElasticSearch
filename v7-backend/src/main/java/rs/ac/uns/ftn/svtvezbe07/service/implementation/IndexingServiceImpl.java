package rs.ac.uns.ftn.svtvezbe07.service.implementation;

import rs.ac.uns.ftn.svtvezbe07.exceptionhandling.exception.LoadingException;
import rs.ac.uns.ftn.svtvezbe07.exceptionhandling.exception.StorageException;
import rs.ac.uns.ftn.svtvezbe07.indexmodel.FileIndex;
import rs.ac.uns.ftn.svtvezbe07.indexrepository.FileIndexRepository;
import rs.ac.uns.ftn.svtvezbe07.model.entity.File;
import rs.ac.uns.ftn.svtvezbe07.repository.FileRepository;
import rs.ac.uns.ftn.svtvezbe07.service.FileService;
import rs.ac.uns.ftn.svtvezbe07.service.IndexingService;

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
public class IndexingServiceImpl implements IndexingService {

    private final FileService fileService;
    private final FileRepository fileRepository;

    private final FileIndexRepository fileIndexRepository;

    private final LanguageDetector languageDetector;


    @Override
    @Transactional
    public File indexDocument(MultipartFile documentFile, String type, Long id) {
        var newEntity = new File();
        var newIndex = new FileIndex();

        if (Objects.equals(type, "group")) {
            newIndex.setGroupId(id);
        } else if (Objects.equals(type, "post")) {
            newIndex.setPostId(id);
        }
        var title = Objects.requireNonNull(documentFile.getOriginalFilename()).split("\\.")[0];
        newIndex.setTitle(title);
        newEntity.setTitle(title);

        var documentContent = extractDocumentContent(documentFile);
        if (detectLanguage(documentContent).equals("SR")) {
            newIndex.setContentSr(documentContent);
        } else {
            newIndex.setContentEn(documentContent);
        }

        var serverFilename = fileService.store(documentFile, UUID.randomUUID().toString());
        newIndex.setServerFilename(serverFilename);
        newEntity.setServerFilename(serverFilename);

        newEntity.setMimeType(detectMimeType(documentFile));
        var savedEntity = fileRepository.save(newEntity);

        newIndex.setDatabaseId(savedEntity.getId());
        fileIndexRepository.save(newIndex);


        return newEntity;
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