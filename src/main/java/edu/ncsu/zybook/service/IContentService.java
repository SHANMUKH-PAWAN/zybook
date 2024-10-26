package edu.ncsu.zybook.service;

import edu.ncsu.zybook.domain.model.Content;

import java.util.List;
import java.util.Optional;

public interface IContentService {
    Content create(Content content);
    Optional<Content> findById(int contentId, int sectionId, int chapId, int tbookId);
    Optional<Content> update(Content content);
    boolean delete(Content content);
    List<Content> getAllContentBySection(int sectionId, int chapId, int tbookId);
}
