package rs.ac.uns.ftn.svtvezbe07.service.implementation;

import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.tika.Tika;
import org.apache.tika.language.detect.LanguageDetector;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import rs.ac.uns.ftn.svtvezbe07.exceptionhandling.exception.LoadingException;
import rs.ac.uns.ftn.svtvezbe07.exceptionhandling.exception.StorageException;
import rs.ac.uns.ftn.svtvezbe07.indexmodel.PostIndex;
import rs.ac.uns.ftn.svtvezbe07.indexrepository.PostIndexRepository;
import rs.ac.uns.ftn.svtvezbe07.model.entity.Post;
import rs.ac.uns.ftn.svtvezbe07.repository.PostRepository;
import rs.ac.uns.ftn.svtvezbe07.service.FileService;
import rs.ac.uns.ftn.svtvezbe07.service.IndexingPostService;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IndexingPostServiceImpl implements IndexingPostService {
    private final PostIndexRepository postIndexRepository;
    private final PostRepository postRepository;
    private final FileService fileService;
    private final LanguageDetector languageDetector;

    @Override
    @Transactional
    public String indexPost(MultipartFile documentFile) {
        var newPost = new Post();
        var newPostIndex = new PostIndex();

        var name = Objects.requireNonNull(documentFile.getOriginalFilename()).split("\\.")[0];
        newPost.setTitle(name);
        newPostIndex.setTitle(name);

        var documentContent = extractDocumentContent(documentFile);
        if (detectLanguage(documentContent).equals("SR")) {
            newPostIndex.setContentSr(documentContent);
        } else {
            newPostIndex.setContentEn(documentContent);
        }

        var serverFilename = fileService.store(documentFile, UUID.randomUUID().toString());
        newPostIndex.setTitle(serverFilename);
        newPost.setTitle(serverFilename);

        newPost.setContent(detectMimeType(documentFile));
        var savedPost = postRepository.save(newPost);
//ovde mozda izbaci gresku zbog Long.valueOf......
        newPostIndex.setId(Long.valueOf(savedPost.getId().toString()));
        postIndexRepository.save(newPostIndex);

        return serverFilename;
    }
    @Override
    @Transactional
    public String indexDocument(Post post) {
        PostIndex newEntity = new PostIndex(post.getId(), post.getTitle(), post.getCreationDate().toLocalDate());

        if (detectLanguage(post.getContent()).equals("SR")) {
            newEntity.setContentSr(post.getContent());
        } else {
            newEntity.setContentEn(post.getContent());
        }

        postIndexRepository.save(newEntity);

        return post.getTitle();
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

