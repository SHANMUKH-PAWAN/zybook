package edu.ncsu.zybook.web.controller;

import edu.ncsu.zybook.mapper.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import edu.ncsu.zybook.DTO.SectionReadDTO;
import edu.ncsu.zybook.DTO.TextbookReadDTO;
import edu.ncsu.zybook.DTO.ChapterReadDTO;
import edu.ncsu.zybook.domain.model.Chapter;
import edu.ncsu.zybook.domain.model.Section;
import edu.ncsu.zybook.domain.model.Textbook;
import edu.ncsu.zybook.domain.model.Content;
import edu.ncsu.zybook.service.IChapterService;
import edu.ncsu.zybook.service.ISectionService;
import edu.ncsu.zybook.service.ITextbookService;
import edu.ncsu.zybook.service.IContentService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/sections")
public class SectionController {
    IChapterService iChapterService;
    ITextbookService iTextbookService;

    ISectionService iSectionService;
    IContentService iContentService;
    TextbookReadDTOMapper textbookReadDTOMapper;

    ChapterReadDTOMapper chapterReadDTOMapper;

    SectionReadDTOMapper sectionReadDTOMapper;

    SectionWeakMapper sectionWeakMapper;
    ContentWeakMapper contentWeakMapper;

    public SectionController(IChapterService iChapterService, ITextbookService iTextbookService, ISectionService iSectionService, TextbookReadDTOMapper textbookReadDTOMapper, ChapterReadDTOMapper chapterReadDTOMapper, SectionWeakMapper sectionWeakMapper, IContentService iContentService, SectionReadDTOMapper sectionReadDTOMapper, ContentWeakMapper contentWeakMapper) {
        this.iChapterService = iChapterService;
        this.iTextbookService = iTextbookService;
        this.iSectionService = iSectionService;
        this.textbookReadDTOMapper = textbookReadDTOMapper;
        this.chapterReadDTOMapper = chapterReadDTOMapper;
        this.sectionWeakMapper = sectionWeakMapper;
        this.iContentService = iContentService;
        this.sectionReadDTOMapper = sectionReadDTOMapper;
        this.contentWeakMapper = contentWeakMapper;
    }

    @GetMapping("/edit")
    public String showEditForm(
            @RequestParam("tbookId") int textbookId,
            @RequestParam("chapId") int chapterId,
            @RequestParam("sno") int sectionId,
            Model model) {
        Optional<Section> section = iSectionService.findById(textbookId, chapterId, sectionId);
        if (section.isPresent()) {
            Optional<Textbook> tbook = iTextbookService.findById(textbookId);
            Optional<Chapter> chap = iChapterService.findById(chapterId, textbookId);
            tbook.ifPresent(textbook -> model.addAttribute("textbook", textbook));
            chap.ifPresent(chapter -> model.addAttribute("chapter", chapter));
            model.addAttribute("chapter", chap.get());
            return "chapter/create";
        } else {
            return "chapter/not-found";
        }
    }

    @GetMapping()
    public String getAllChapters(@RequestParam("tbookId") int tbookId, Model model) {
        List<Chapter> chapters = iChapterService.findAllByTextbook(tbookId);
        model.addAttribute("chapters", chapters);
        return "chapter/list";
    }

    @GetMapping("/chapter")
    public String getChapter(
            @RequestParam("tbookId") int textbookId,
            @RequestParam("chapId") int chapterId,
            Model model) {
        Optional<Chapter> chapter = iChapterService.findById(chapterId, textbookId);
        if (chapter.isPresent()) {
            List<Section> sections = iSectionService.findAllByChapter(textbookId,chapterId);
            ChapterReadDTO chapterReadDTO = chapterReadDTOMapper.toDTO(chapter.get());
            chapterReadDTO.setSections(sections.stream().map(sectionWeakMapper::toDTO).collect(Collectors.toList()));
            model.addAttribute("chapter", chapterReadDTO );
            System.out.println(Arrays.toString(chapterReadDTO.getSections().toArray()));
            return "chapter/chapter";
        } else {
            return "chapter/not-found";
        }
    }

    @PostMapping
    public  String createChapter(@ModelAttribute Chapter chapter) {
        iChapterService.create(chapter);
        return "redirect:/textbooks/"+ chapter.getTbookId();
    }

    @GetMapping("/new")
    public String showCreateForm(@RequestParam("tbookId") Integer textbookId, Model model) {
        Chapter chapter = new Chapter();
        chapter.setTbookId(textbookId);
        Optional<Textbook> tbook = iTextbookService.findById(textbookId);
        tbook.ifPresent(textbook -> model.addAttribute("textbook", tbook.get()));
        model.addAttribute("chapter", chapter);
        return "chapter/create";
    }

    @PutMapping("/update")
    public String updateChapter( @RequestParam("tbookId") int textbookId,
                                 @RequestParam("chapId") int chapterId,
                                 @ModelAttribute Chapter chapter) {
        iChapterService.update(chapter);
        return "redirect:/textbooks/" + textbookId;
    }

    @DeleteMapping
    public String deleteChapter(
            @RequestParam("tbookId") int textbookId,
            @RequestParam("chapId") int chapterId
    ){
        iChapterService.delete(textbookId,chapterId);
        return "redirect:/textbooks/"+textbookId;
    }


}
