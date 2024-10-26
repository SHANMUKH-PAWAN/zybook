package edu.ncsu.zybook.web.controller;

import edu.ncsu.zybook.DTO.TextbookReadDTO;
import edu.ncsu.zybook.domain.model.Textbook;
import edu.ncsu.zybook.mapper.TextbookReadDTOMapper;
import edu.ncsu.zybook.service.ITextbookService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller()
@RequestMapping("/textbooks")
public class TextbookController {

    private final ITextbookService textbookService;
    private final TextbookReadDTOMapper textbookReadDTOMapper;

    public TextbookController(ITextbookService textbookService, TextbookReadDTOMapper textbookReadDTOMapper) {
        this.textbookService = textbookService;
        this.textbookReadDTOMapper = textbookReadDTOMapper;
    }

    @GetMapping("/welcome")
    public String Welcome(){
        return "Welcome to DBMS Zybook !!";
    }

    @GetMapping("/{id}")
    public String getTextbook(@PathVariable int id, Model model) {
        Optional<TextbookReadDTO> textbook = textbookService.findById(id);
        if (textbook.isPresent()) {
            model.addAttribute("textbook", textbook.get());
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
        Optional<TextbookReadDTO> textbook = textbookService.findById(id);
        if (textbook.isPresent()) {
            model.addAttribute("textbook", textbookReadDTOMapper.toEntity( textbook.get()));
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