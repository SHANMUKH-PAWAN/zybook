package edu.ncsu.zybook.persistence.repository;


import edu.ncsu.zybook.domain.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class QuestionRepository implements IQuestionRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;


    @Transactional
    @Override
    public Question create(Question question) {
        String sql = "INSERT INTO Question (activity_id, content_id, section_id, tbook_id, chapter_id, q_id) VALUES(?, ?, ?, ?, ?, ?, ?)";
        int rowsAffected = jdbcTemplate.update(sql, question.getActivity_id(), question.getContent_id(), question.getSection_id(), question.getTbook_id(), question.getChapter_id(), question.getQuestion_id());
        if(rowsAffected > 0)
        {
            return findById(question.getQuestion_id(), question.getActivity_id(), question.getContent_id(), question.getSection_id(), question.getChapter_id(), question.getTbook_id())
                    .orElseThrow(() -> new RuntimeException("Failed to retrieve newly inserted question."));
        }
        else{
            throw new RuntimeException("Failed to insert question.");
        }
    }

    @Transactional
    @Override
    public Optional<Question> update(Question question) {
        String sql = "UPDATE Question SET question = ?, answer_id = ? WHERE activity_id = ? AND content_id = ? AND section_id = ? AND chapter_id = ? AND tbook_id = ? AND q_id = ? ";
        int rowsAffected = jdbcTemplate.update(sql, question.getQuestion(), question.getAnswer_id(), question.getActivity_id(), question.getContent_id(), question.getSection_id(), question.getChapter_id(), question.getTbook_id(), question.getQuestion_id());
        return rowsAffected > 0 ? Optional.of(question) : Optional.empty();
    }

    @Transactional
    @Override
    public boolean delete(int question_id, int activity_id, int content_id, int section_id, int chapter_id, int tbook_id) {
        String sql = "DELETE FROM Question WHERE q_id = ? AND activity_id = ? AND content_id = ? AND section_id = ? AND chapter_id = ? AND tbook_id = ? ";
        int rowsAffected = jdbcTemplate.update(sql, question_id, activity_id, content_id, section_id, chapter_id, tbook_id);
        return rowsAffected>0;
    }

    @Transactional
    public Optional<Question> findById(int question_id, int activity_id, int content_id, int section_id, int chapter_id, int tbook_id) {
        String sql = "SELECT * FROM Question WHERE q_id = ? AND activity_id = ? AND content_id = ? AND section_id = ? AND tbook_id = ? AND chapter_id = ? AND q_id = ?";
        try{
            Question question = jdbcTemplate.queryForObject(sql, new Object[]{activity_id, content_id, section_id, tbook_id, chapter_id, question_id}, new QuestionRowMapper());
            return Optional.of(question);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Question> findAllByActivity(int activityId, int contentId, int sectionId, int chapterId, int tbookId) {
        String sql = "SELECT * FROM Question WHERE activity_id = ? AND content_id = ? AND section_id = ? AND tbook_id = ? AND chapter_id = ? ORDER BY activity_id";
        return jdbcTemplate.query(sql, new Object[]{activityId, contentId, sectionId, tbookId, chapterId}, new QuestionRowMapper());
    }


    private static class QuestionRowMapper implements RowMapper<Question> {
        @Override
        public Question mapRow(ResultSet rs, int rowNum) throws SQLException {
            Question question = new Question();
            question.setActivity_id(rs.getInt("activity_id"));
            question.setQuestion_id(rs.getInt("question_id"));
            question.setContent_id(rs.getInt("content_id"));
            question.setSection_id(rs.getInt("section_id"));
            question.setChapter_id(rs.getInt("chapter_id"));
            question.setTbook_id(rs.getInt("tbook_id"));
            question.setAnswer_id(rs.getInt("answer_id"));
            question.setQuestion(rs.getString("question"));
            return question;
        }
    }

    }
