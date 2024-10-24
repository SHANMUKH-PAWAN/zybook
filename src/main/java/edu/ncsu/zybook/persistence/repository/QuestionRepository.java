package edu.ncsu.zybook.persistence.repository;


import edu.ncsu.zybook.domain.model.Activity;
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
        String sql = "INSERT INTO Question (activity_id, content_id, section_id, tbook_id, chapter_id, question_id) VALUES(?, ?, ?, ?, ?, ?)";
        int rowsAffected = jdbcTemplate.update(sql, question.getActivity_id(), question.getContent_id(), question.getSection_id(), question.getTbook_id(), question.getChapter_id(), question.getQuestion_id());
        if(rowsAffected > 0)
        {
            return findById(question.getActivity_id(), question.getContent_id(), question.getSection_id(), question.getTbook_id(), question.getChapter_id(), question.getQuestion_id())
                    .orElseThrow(() -> new RuntimeException("Failed to retrieve newly inserted question."));
        }
        else{
            throw new RuntimeException("Failed to insert question.");
        }
    }

    @Transactional
    @Override
    public Optional<Question> update(Question question) {
        String sql = "UPDATE Question SET activity_id = ? AND content_id = ? AND section_id = ? AND tbook_id = ? AND chapter_id = ? AND question_id = ? WHERE activity_id = ?, content_id = ?, section_id = ?, tbook_id = ?, chapter_id = ?, question_id = ? ";
        int rowsAffected = jdbcTemplate.update(sql, question.getActivity_id(), question.getContent_id(), question.getSection_id(), question.getTbook_id(), question.getChapter_id(), question.getQuestion_id());
        return rowsAffected > 0 ? Optional.of(question) : Optional.empty();
    }

    @Transactional
    @Override
    public boolean delete(Question question) {
        String sql = "DELETE FROM Question WHERE activity_id = ?, content_id = ?, section_id = ?, tbook_id = ? AND chapter_id = ? AND question_id = ?";
        int rowsAffected = jdbcTemplate.update(sql, question.getActivity_id(), question.getContent_id(), question.getSection_id(), question.getTbook_id(), question.getChapter_id(), question.getQuestion_id());
        return rowsAffected>0;
    }

    @Transactional
    public Optional<Question> findById(int activity_id, int content_id, int section_id,int tbook_id, int chapter_id, int question_id) {
        String sql = "SELECT * FROM Chapter WHERE activity_id = ? AND content_id = ? AND section_id = ? AND tbook_id = ? AND chapter_id = ?, question_id = ?";
        try{
            Question question = jdbcTemplate.queryForObject(sql, new Object[]{activity_id, content_id, section_id, tbook_id, chapter_id, question_id}, new QuestionRowMapper());
            return Optional.of(question);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }


    @Override
    public List<Question> findAllByActivity(int activity_id, int content_id, int section_id, int tbook_id, int chapter_id) {
        String sql = "SELECT * FROM Activity WHERE activity_id = ? AND content_id = ? AND section_id = ? AND tbook_id = ? AND chapter_id = ?, question_id = ? ORDER BY activity_id";
        return jdbcTemplate.query(sql, new Object[]{ activity_id, content_id, section_id, tbook_id, chapter_id}, new QuestionRepository.QuestionRowMapper());
    }

    private static class QuestionRowMapper implements RowMapper<Question> {
        @Override
        public Question mapRow(ResultSet rs, int rowNum) throws SQLException {
            Question question = new Question();
            question.setActivity_id(rs.getInt("activity_id"));
            question.setQuestion_id(rs.getInt("question_id"));
            question.setContent_id(rs.getInt("content_id"));
            question.getSection_id(rs.getInt("section_id"));
            question.setChapter_id(rs.getInt("chapter_id"));
            question.setTbook_id(rs.getInt("tbook_id"));
            question.setAnswer_id(rs.getInt("isHidden"));
            return question;
        }
    }

    }
