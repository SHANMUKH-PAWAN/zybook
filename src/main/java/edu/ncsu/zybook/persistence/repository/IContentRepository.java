package edu.ncsu.zybook.persistence.repository;

import edu.ncsu.zybook.domain.model.Content;

import java.util.List;
import java.util.Optional;

public interface IContentRepository {
    Content create(Content content);
    Optional<Content> update(Content content);
    boolean delete(int tbook_id, int chap_id, int section_id, int content_id);
    Optional<Content> findById(int id);
    List<Content> findAll(int offset, int limit, String sortBy, String sortDirection, int tbook_id, int sectionId, int chapId);
}
