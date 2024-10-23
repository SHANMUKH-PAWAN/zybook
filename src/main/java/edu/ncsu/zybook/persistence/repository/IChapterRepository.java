package edu.ncsu.zybook.persistence.repository;

import edu.ncsu.zybook.domain.model.Chapter;

import java.util.Optional;

public interface IChapterRepository extends BaseRepository<Chapter> {
    Optional<Chapter> findByTitle(String chapter_code);
}
