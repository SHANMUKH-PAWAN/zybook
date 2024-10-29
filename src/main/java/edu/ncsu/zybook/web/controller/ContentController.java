package edu.ncsu.zybook.web.controller;

import edu.ncsu.zybook.DTO.ContentReadDTO;
import edu.ncsu.zybook.domain.model.Content;
import edu.ncsu.zybook.domain.model.ImageContent;
import edu.ncsu.zybook.domain.model.TextContent;
import edu.ncsu.zybook.domain.model.Textbook;
import edu.ncsu.zybook.mapper.ContentReadDTOMapper;
import edu.ncsu.zybook.service.IContentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/contents")
public class ContentController {

    private final IContentService contentService;
    private final ContentReadDTOMapper contentReadDTOMapper;

    public ContentController(IContentService contentService, ContentReadDTOMapper contentReadDTOMapper) {
        this.contentService = contentService;
        this.contentReadDTOMapper = contentReadDTOMapper;
    }

    @GetMapping("/new/text")
    public String showTextContentForm(Model model) {
        model.addAttribute("textContent", new TextContent());
        return "content/createTextContent";
    }

    @GetMapping("/new/image")
    public String showImageContentForm(@RequestParam int tbookId,
                                       @RequestParam int chapId,
                                       @RequestParam int sectionId,
                                       Model model) {
        ImageContent content = new ImageContent();
        content.setTbookId(tbookId);
        content.setChapId(chapId);
        content.setSectionId(sectionId);
        model.addAttribute("imageContent", content);
        return "content/createImageContent";
    }

    @PostMapping
    public String createContent(@RequestBody Content content) {
        try {
            Content createdContent = contentService.create(content);
            return "Helloworld";
        } catch (RuntimeException e) {
            return "None"; // need to fix this later;
        }
    }

    @PostMapping("/text")
    public String createTextContent(@RequestParam("tbookId") int textbookId,
                                    @RequestParam("chapId") int chapterId,
                                    @RequestParam("sno") int sectionId,
                                    @ModelAttribute TextContent content) {
        Content createdContent = contentService.create(content);
        return "redirect:/sections/" + content.getSectionId();
    }

    @PostMapping("/image")
    public String createImageContent(@RequestParam("tbookId") int textbookId,
                                     @RequestParam("chapId") int chapterId,
                                     @RequestParam("sectionId") int sectionId,
                                     @RequestParam("data") MultipartFile file,
                                     @ModelAttribute ImageContent content) {

        try {
            // Convert MultipartFile to byte array
            byte[] imageData = file.getBytes();

            // Set the image data into the content object
            content.setData(imageData);

            // Set the other fields if necessary
            content.setTbookId(textbookId);
            content.setChapId(chapterId);
            content.setSectionId(sectionId);

            // Create the content
            Content createdContent = contentService.create(content);
            return "redirect:/sections/" + content.getSectionId();
        } catch (IOException e) {
            // Handle the exception (e.g., log it and return an error view)
            e.printStackTrace();
            return "error"; // Redirect or return an error view
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

    @GetMapping
    public String getAllContentBySection(@RequestParam int sectionId,
                                         @RequestParam int chapId,
                                         @RequestParam int tbookId,
                                         Model model) {
        List<Content> contentList = contentService.getAllContentBySection(sectionId, chapId, tbookId);

        contentList.forEach(e ->
                {
                    if (e instanceof TextContent){
                        TextContent t = (TextContent)e ;
                        System.out.println(t.getData() );
                    }
                    else if (e instanceof ImageContent) {
                        ImageContent i = (ImageContent) e;
                        System.out.println(i.getData());
                    }
                }
                );
        List<ContentReadDTO> contentReadDTOS = new ArrayList<>();
        contentReadDTOS = contentList.stream().map(
                content -> {
                    if (content instanceof TextContent) {
                        return contentReadDTOMapper.toDTO((TextContent) content);
                    } else if (content instanceof ImageContent) {
                        return contentReadDTOMapper.toDTO((ImageContent) content);
                    }
                    return null;
                }
        ).collect(Collectors.toList());

        contentReadDTOS.forEach(content -> System.out.println(content.getData()));
        model.addAttribute("allContents", contentReadDTOS);
        model.addAttribute("sectionId", sectionId);
        model.addAttribute("chapId", chapId);
        model.addAttribute("tbookId", tbookId);
        return "content/list";
    }
}
