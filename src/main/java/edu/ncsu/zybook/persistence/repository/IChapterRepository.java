package edu.ncsu.zybook.persistence.repository;

import edu.ncsu.zybook.domain.model.Chapter;

import java.util.List;
import java.util.Optional;

public interface IChapterRepository{
    Chapter create(Chapter chapter);
    Optional<Chapter> update(Chapter chapter);
    boolean delete(int tbook_id, int chap_id);
    Optional<Chapter> findById(int id);
    List<Chapter> findAll(int offset, int limit, String sortBy, String sortDirection, int tbook_id);
    Optional<Chapter> findByTitle(String chapter_code);
}
