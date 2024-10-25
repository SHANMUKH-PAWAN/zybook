package edu.ncsu.zybook.service.impl;

import edu.ncsu.zybook.domain.model.Chapter;
import edu.ncsu.zybook.domain.model.Section;
import edu.ncsu.zybook.persistence.repository.IChapterRepository;
import edu.ncsu.zybook.persistence.repository.ISectionRepository;
import edu.ncsu.zybook.service.IChapterService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ChapterService implements IChapterService {

    private final IChapterRepository chapterRepository;
    private final ISectionRepository sectionRepository;

    public ChapterService(IChapterRepository chapterRepository, ISectionRepository sectionRepository) {
        this.chapterRepository = chapterRepository;
        this.sectionRepository = sectionRepository;
    }

    @Override
    public Chapter create(Chapter chapter) {
        Optional<Chapter> result = chapterRepository.findById(chapter.getCno(), chapter.getTbookId());
        if (result.isEmpty()) {
            return chapterRepository.create(chapter);
        }
        else{
            throw new RuntimeException("Chapter already exists!");
        }
    }

    @Override
    public Optional<Chapter> findById (int cno, int tbookId) {

        Optional<Chapter> result = chapterRepository.findById(cno, tbookId);
        if(result.isPresent()) {
            Chapter chapter = result.get();
            List<Section> sections = sectionRepository.findAllByTextbook(tbookId, cno);
            return Optional.of(chapter);
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<Chapter> update(int id, Chapter chapter) {
        if(chapterRepository.findById(id, chapter.getTbookId()).isPresent()){
            return chapterRepository.update(chapter);
        }
        else{
            throw new RuntimeException("Chapter does not exist with id:" + id);
        }
    }

    @Override
    @Transactional
    public boolean delete(int id, Chapter chapter) {
        Optional<Chapter> result = chapterRepository.findById(id, chapter.getTbookId());
        if (result.isPresent()) {
            return chapterRepository.delete(chapter);
        }
        else{
            throw new RuntimeException("Chapter does not exist with id:" + id);
        }
    }

    @Override
    public List<Chapter> findAllByTextbook(int tbookId) {
        return chapterRepository.findAllByTextbook(tbookId);
    }

    @Override
    public Optional<Chapter> findByTitle(String title, int tbookId) {
        Optional<Chapter> result = chapterRepository.findByTitle(title, tbookId);
        if(result.isPresent()) {
            Chapter chapter = result.get();
            return Optional.of(chapter);
        }
        else{
            throw new RuntimeException("Chapter does not exist with title:" + title +" in textbook:" + tbookId);
        }
    }
}
