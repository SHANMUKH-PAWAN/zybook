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
import org.springframework.security.access.prepost.PreAuthorize;
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
    public String showTextContentForm(@RequestParam int tbookId,
                                      @RequestParam int chapId,
                                      @RequestParam int sectionId,
                                      Model model) {
        TextContent content = new TextContent();
        content.setTbookId(tbookId);
        content.setChapId(chapId);
        content.setSectionId(sectionId);
        model.addAttribute("textContent", content);
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

//    @PostMapping
//    public String createContent(@RequestBody Content content) {
//        try {
//            Content createdContent = contentService.create(content);
//            return "Helloworld";
//        } catch (RuntimeException e) {
//            return "None"; // need to fix this later;
//        }
//    }

    @PreAuthorize("hasAnyRole('PROFESSOR', 'ADMIN', 'TA')")
    @PostMapping("/text")
    public String createTextContent(@RequestParam("tbookId") int textbookId,
                                    @RequestParam("chapId") int chapterId,
                                    @RequestParam("sectionId") int sectionId,
                                    @ModelAttribute TextContent content) {

        content.setContentType("text");
        Content createdContent = contentService.create(content);
        return "redirect:/contents?tbookId="+textbookId+"&chapId="+chapterId+"&sectionId="+sectionId;
    }

    @PreAuthorize("hasAnyRole('PROFESSOR', 'ADMIN', 'TA')")
    @PostMapping("/image")
    public String createImageContent(@RequestParam("tbookId") int textbookId,
                                     @RequestParam("chapId") int chapterId,
                                     @RequestParam("sectionId") int sectionId,
                                     @RequestParam("image") MultipartFile file,
                                     @ModelAttribute ImageContent content) {

        try {

            String contentType = file.getContentType();
            if (!"image/png".equals(contentType) && !"image/jpeg".equals(contentType)) {
                throw new IllegalArgumentException("File must be a PNG or JPEG image");
            }

            // Convert MultipartFile to byte array
            byte[] imageData = file.getBytes();

            // Set the image data into the content object
            content.setData(imageData);
            content.setContentType("image");

//            System.out.println(content);
            // Create the content
            Content createdContent = contentService.create(content);
            return "redirect:/contents?tbookId="+textbookId+"&chapId="+chapterId+"&sectionId="+sectionId;
        } catch (IOException e) {
            // Handle the exception (e.g., log it and return an error view)
            e.printStackTrace();
            return "error"; // Redirect or return an error view
        }
    }

//    @GetMapping("/{contentId}/{sectionId}/{chapId}/{tbookId}")
//    public ResponseEntity<Content> getContentById(
//            @PathVariable int contentId,
//            @PathVariable int sectionId,
//            @PathVariable int chapId,
//            @PathVariable int tbookId) {
//        Optional<Content> content = contentService.findById(contentId, sectionId, chapId, tbookId);
//        return content.map(ResponseEntity::ok).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
//    }

//    @PutMapping
//    public ResponseEntity<Content> updateContent(@RequestBody Content content) {
//        try {
//            Optional<Content> updatedContent = contentService.update(content);
//            return updatedContent.map(ResponseEntity::ok)
//                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
//        } catch (RuntimeException e) {
//            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
//        }
//    }

    @PreAuthorize("hasAnyRole('PROFESSOR', 'ADMIN', 'TA')")
    @GetMapping("/edit/text")
    public String editTextContentForm(@RequestParam("tbookId") int textbookId,
                                      @RequestParam("chapId") int chapterId,
                                      @RequestParam("sectionId") int sectionId,
                                       @RequestParam("contentId") int contentId,
                                       Model model) {
        Optional<Content> result = contentService.findById(contentId,sectionId, chapterId, textbookId);

        if (result.isPresent()) {
            // Add the found content to the model with a cast to TextContent if needed
            TextContent textContent = (TextContent) result.get();
            System.out.println("In Object " + textContent);
            model.addAttribute("textContent", textContent );
            return "content/createTextContent";
        } else {
            // Redirect to an error or "not found" page if the content is missing
            return "section/not-found";
        }
    }

    @PreAuthorize("hasAnyRole('PROFESSOR', 'ADMIN', 'TA')")
    @GetMapping("/edit/image")
    public String editImageContentForm(@RequestParam("tbookId") int textbookId,
                                       @RequestParam("chapId") int chapterId,
                                       @RequestParam("sectionId") int sectionId,
                                       @RequestParam("contentId") int contentId,
                                      Model model) {
        Optional<Content> result = contentService.findById(contentId,sectionId, chapterId, textbookId);

        if (result.isPresent()) {

            ContentReadDTO contentReadDTO = contentReadDTOMapper.toDTO((ImageContent) result.get());
            // Add the found content to the model with a cast to TextContent if needed
            model.addAttribute("imageContent", contentReadDTO);

            return "content/createImageContent";
        } else {
            // Redirect to an error or "not found" page if the content is missing
            return "section/not-found";
        }
    }



    @PreAuthorize("hasAnyRole('PROFESSOR', 'ADMIN', 'TA')")
    @PutMapping("/text")
    public String updateTextContent(@RequestParam("tbookId") int textbookId,
                                    @RequestParam("chapId") int chapterId,
                                    @RequestParam("sectionId") int sectionId,
                                    @RequestParam("contentId") int contentId,
                                    @ModelAttribute TextContent content
    ) {
        try {
//            System.out.println(content);
            Content baseContent = content;
//            System.out.println(baseContent);
            content.setContentType("text");
            Optional<Content> updatedContent = contentService.update(content);
            if (updatedContent.isPresent()) {
                // Redirect to a content view page after update
                return "redirect:/contents?tbookId=" + textbookId + "&chapId=" + chapterId + "&sectionId=" + sectionId;
            }
            else return "section/not-found";

            } catch (RuntimeException e) {
            return "section/not-found";
        }
    }

    @PreAuthorize("hasAnyRole('PROFESSOR', 'ADMIN', 'TA')")
    @PutMapping("/image")
    public String updateImageContent(@RequestParam("tbookId") int textbookId,
                                     @RequestParam("chapId") int chapterId,
                                     @RequestParam("sectionId") int sectionId,
                                     @RequestParam("image") MultipartFile file,
                                     @RequestParam("contentId") int contentId,
                                     @ModelAttribute ImageContent content) {

        try {

            String contentType = file.getContentType();
            if (!"image/png".equals(contentType) && !"image/jpeg".equals(contentType)) {
                throw new IllegalArgumentException("File must be a PNG or JPEG image");
            }

            // Convert MultipartFile to byte array
            byte[] imageData = file.getBytes();

            // Set the image data into the content object
            content.setData(imageData);
            content.setContentType("image");

//            System.out.println(content);
            // Create the content
            Optional<Content> createdContent = contentService.update(content);
            return "redirect:/contents?tbookId="+textbookId+"&chapId="+chapterId+"&sectionId="+sectionId;
        } catch (IOException e) {
            // Handle the exception (e.g., log it and return an error view)
            e.printStackTrace();
            return "error"; // Redirect or return an error view
        }
    }

    @PreAuthorize("hasAnyRole('PROFESSOR', 'ADMIN', 'TA')")
    @DeleteMapping
    public String deleteContent(@RequestParam int sectionId,
                                @RequestParam int chapId,
                                @RequestParam int tbookId,
                                @RequestParam int contentId
    ) {
        try {
            boolean deleted = contentService.delete(contentId, sectionId, chapId, tbookId);
            return deleted ?"redirect:/contents?tbookId="+tbookId+"&chapId="+chapId+"&sectionId="+sectionId
                    : "section/not-found.html";
        } catch (RuntimeException e) {
            return "section/not-found.html";
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
                    }
                    else if (e instanceof ImageContent) {
                        ImageContent i = (ImageContent) e;
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

        model.addAttribute("allContents", contentReadDTOS);
        model.addAttribute("sectionId", sectionId);
        model.addAttribute("chapId", chapId);
        model.addAttribute("tbookId", tbookId);
        return "content/list";
    }
}
