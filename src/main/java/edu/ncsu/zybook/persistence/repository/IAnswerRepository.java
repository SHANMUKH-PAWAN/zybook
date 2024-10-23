package edu.ncsu.zybook.persistence.repository;

import edu.ncsu.zybook.domain.model.Answer;

import java.util.List;
import java.util.Optional;

public interface IAnswerRepository {
    Answer create(Answer answer);
    Optional<Answer> findById(int answerId, int activityId, int contentId, int sectionId, int chapId, int tbookId);
    Optional<Answer> update(Answer answer);
    boolean delete(int answerId, int activityId, int contentId, int sectionId, int chapId, int tbookId);
    List<Answer> findAllByActivity(int activityId, int contentId, int sectionId, int chapId, int tbookId);
}
