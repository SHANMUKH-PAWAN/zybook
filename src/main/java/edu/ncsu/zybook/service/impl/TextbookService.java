package edu.ncsu.zybook.service.impl;

import edu.ncsu.zybook.domain.model.Textbook;
import edu.ncsu.zybook.persistence.repository.ITextbookRepository;
import edu.ncsu.zybook.service.ITextbookService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TextbookService implements ITextbookService {

    private final ITextbookRepository textbookRepository;
    public TextbookService(ITextbookRepository textbookRepository) {
        this.textbookRepository = textbookRepository;
    }

    @Override
    public Textbook create(Textbook textbook) {
        Optional<Textbook> result= textbookRepository.findByTitle(textbook.getTitle());
        if(result.isEmpty()) {
            return textbookRepository.create(textbook);
        }
        else {
            throw new RuntimeException("Textbook already exists");
        }
    }

    @Override
    public Optional<Textbook> findById(int id) {
        return textbookRepository.findById(id) ;
    }

    @Override
    public Optional<Textbook> update(int id, Textbook textbook) {
        if(textbookRepository.findById(textbook.getUid()).isPresent())
            return textbookRepository.update(textbook);
        else
            throw new RuntimeException("There is no textbook with  id: "+id);
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        Optional<Textbook> existingTextbook = textbookRepository.findById(id);
        if (existingTextbook.isPresent()) {
            textbookRepository.delete(id);
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public List<Textbook> getAllTextbooks(int offset, int limit, String sortBy, String sortDirection) {
        return textbookRepository.findAll(offset, limit, sortBy, sortDirection);
    }
}
