package edu.ncsu.zybook.persistence.repository;

import edu.ncsu.zybook.domain.model.Activity;
import edu.ncsu.zybook.domain.model.Question;
import org.w3c.dom.Text;

import java.util.List;
import java.util.Optional;

public interface IQuestionRepository {
    Question create(Question question);

    Optional<Question> findById(int activity_id, int content_id, int section_id,int tbook_id, int chapter_id, int question_id);

    Optional<Question> update(Question question);

    boolean delete(Question question);

    List<Question> findAllByActivity(int activity_id, int content_id, int section_id, int tbook_id, int chapter_id);
}
