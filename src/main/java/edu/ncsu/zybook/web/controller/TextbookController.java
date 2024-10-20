package edu.ncsu.zybook.web.controller;

import edu.ncsu.zybook.domain.model.Textbook;
import edu.ncsu.zybook.service.ITextbookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController()
@RequestMapping("/textbooks")
public class TextbookController {

    private final ITextbookService textbookService;

    public TextbookController(ITextbookService textbookService) {
        this.textbookService = textbookService;
    }

    @GetMapping("/welcome")
    public String Welcome(){
        return "Welcome to DBMS Zybook !!";
    }

    @GetMapping("/{id}")
    public ResponseEntity<Textbook> getTextbook(@PathVariable int id) {
        Optional<Textbook> textbook = textbookService.findById(id);
        return textbook.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<Textbook> getAllTextbooks(
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "uid") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        List<Textbook> textbooks = textbookService.getAllTextbooks(offset, limit, sortBy, sortDirection);
        return textbooks;
    }

    @PostMapping
    public  ResponseEntity<Void> createTextbook(@RequestBody Textbook textbook) {
        System.out.println("Entered post!!");
        textbookService.create(textbook);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateTextbook(@PathVariable int id, @RequestBody Textbook textbook) {
        textbookService.update(id,textbook);
        return ResponseEntity.ok("Textbook updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTextbook(@PathVariable int id){
        textbookService.delete(id);
        return ResponseEntity.ok().build();
    }
}
