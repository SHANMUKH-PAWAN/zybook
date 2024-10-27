package edu.ncsu.zybook.web.controller;

import edu.ncsu.zybook.DTO.TextbookReadDTO;
import edu.ncsu.zybook.domain.model.Chapter;
import edu.ncsu.zybook.domain.model.Textbook;
import edu.ncsu.zybook.mapper.ChapterWeakMapper;
import edu.ncsu.zybook.mapper.TextbookReadDTOMapper;
import edu.ncsu.zybook.service.IChapterService;
import edu.ncsu.zybook.service.ITextbookService;
import edu.ncsu.zybook.service.impl.ChapterService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/textbooks")
public class TextbookController {

    private final ITextbookService textbookService;
    private final TextbookReadDTOMapper textbookReadDTOMapper;
    private final IChapterService iChapterService;
    private final ChapterWeakMapper chapterWeakMapper;

    public TextbookController(ITextbookService textbookService, TextbookReadDTOMapper textbookReadDTOMapper, IChapterService iChapterService, ChapterWeakMapper chapterWeakMapper) {
        this.textbookService = textbookService;
        this.textbookReadDTOMapper = textbookReadDTOMapper;
        this.iChapterService = iChapterService;
        this.chapterWeakMapper = chapterWeakMapper;
    }

    @GetMapping("/{id}")
    public String getTextbook(@PathVariable int id, Model model) {
        Optional<Textbook> textbook = textbookService.findById(id);
        if (textbook.isPresent()) {

            TextbookReadDTO textbookReadDTO = textbookReadDTOMapper.toDTO(textbook.get());
            List<Chapter> chapters = iChapterService.findAllByTextbook(textbookReadDTO.getUid());
            textbookReadDTO.setChapters(chapters.stream().map(chapterWeakMapper::toDTO).collect(Collectors.toList()));
            model.addAttribute("textbook", textbookReadDTO);
            return "textbook/textbook";
        } else {
            return "textbook/not-found";
        }
    }

    @GetMapping
    public String getAllTextbooks(
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "uid") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection,
            Model model) {
        List<Textbook> textbooks = textbookService.getAllTextbooks(offset, limit, sortBy, sortDirection);
        model.addAttribute("textbooks", textbooks);
        return "textbook/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("textbook", new Textbook());
        return "textbook/create";
    }

    @PostMapping
    public  String createTextbook(@ModelAttribute Textbook textbook) {
        textbookService.create(textbook);
        return "redirect:/textbooks";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        Optional<Textbook> textbook = textbookService.findById(id);
        if (textbook.isPresent()) {
            model.addAttribute("textbook", textbook);
            return "textbook/create";
        } else {
            return "textbook/not-found";
        }
    }

    @PutMapping("/{id}")
    public String updateTextbook(@ModelAttribute Textbook textbook) {
        textbookService.update(textbook);
        return "redirect:/textbooks";
    }

    @DeleteMapping("/{id}")
    public String deleteTextbook(@PathVariable int id){
        textbookService.delete(id);
        return "redirect:/textbooks";
    }
}