package edu.ncsu.zybook.web.controller;

import edu.ncsu.zybook.domain.model.Content;
import edu.ncsu.zybook.service.IContentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/content")
public class ContentController {

    private final IContentService contentService;

    public ContentController(IContentService contentService) {
        this.contentService = contentService;
    }

    @PostMapping
    public ResponseEntity<Content> createContent(@RequestBody Content content) {
        try {
            Content createdContent = contentService.create(content);
            return new ResponseEntity<>(createdContent, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/{contentId}/{sectionId}/{chapId}/{tbookId}")
    public ResponseEntity<Content> getContentById(
            @PathVariable int contentId,
            @PathVariable int sectionId,
            @PathVariable int chapId,
            @PathVariable int tbookId) {
        Optional<Content> content = contentService.findById(contentId, sectionId, chapId, tbookId);
        return content.map(ResponseEntity::ok).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping
    public ResponseEntity<Content> updateContent(@RequestBody Content content) {
        try {
            Optional<Content> updatedContent = contentService.update(content);
            return updatedContent.map(ResponseEntity::ok)
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteContent(@RequestBody Content content) {
        try {
            boolean deleted = contentService.delete(content);
            return deleted ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                    : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/section/{sectionId}/{chapId}/{tbookId}")
    public ResponseEntity<List<Content>> getAllContentBySection(
            @PathVariable int sectionId,
            @PathVariable int chapId,
            @PathVariable int tbookId) {
        List<Content> contentList = contentService.getAllContentBySection(sectionId, chapId, tbookId);
        return contentList.isEmpty() ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(contentList, HttpStatus.OK);
    }
}
