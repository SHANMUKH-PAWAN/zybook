package edu.ncsu.zybook.persistence.repository;

import edu.ncsu.zybook.domain.model.Answer;
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
public class AnswerRepository implements IAnswerRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Transactional
    @Override
    public Answer create(Answer answer) {
        String sql = "INSERT INTO Answer (answer_id, activity_id, content_id, s_id, c_id, t_id, answer_text, justification) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        int rowsAffected = jdbcTemplate.update(sql,
                answer.getAnswerId(),
                answer.getActivityId(),
                answer.getContentId(),
                answer.getSectionId(),
                answer.getChapId(),
                answer.getTbookId(),
                answer.getAnswerText(),
                answer.getJustification()
        );

        if (rowsAffected > 0) {
            return findById(answer.getAnswerId(), answer.getActivityId(), answer.getContentId(), answer.getSectionId(), answer.getChapId(), answer.getTbookId())
                    .orElseThrow(() -> new RuntimeException("Failed to retrieve newly inserted answer."));
        } else {
            throw new RuntimeException("Failed to insert answer.");
        }
    }

    @Override
    public Optional<Answer> findById(int answerId, int activityId, int contentId, int sectionId, int chapId, int tbookId) {
        String sql = "SELECT * FROM Answer WHERE answer_id = ? AND activity_id = ? AND content_id = ? AND s_id = ? AND c_id = ? AND t_id = ?";
        try {
            Answer answer = jdbcTemplate.queryForObject(sql, new Object[]{answerId, activityId, contentId, sectionId, chapId, tbookId}, new AnswerRowMapper());
            return Optional.of(answer);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Transactional
    @Override
    public Optional<Answer> update(Answer answer) {
        String sql = "UPDATE Answer SET answer_text = ?, justification = ? WHERE answer_id = ? AND activity_id = ? AND content_id = ? AND s_id = ? AND c_id = ? AND t_id = ?";
        int rowsAffected = jdbcTemplate.update(sql,
                answer.getAnswerText(),
                answer.getJustification(),
                answer.getAnswerId(),
                answer.getActivityId(),
                answer.getContentId(),
                answer.getSectionId(),
                answer.getChapId(),
                answer.getTbookId()
        );
        return rowsAffected > 0 ? Optional.of(answer) : Optional.empty();
    }

    @Transactional
    @Override
    public boolean delete(int answerId, int activityId, int contentId, int sectionId, int chapId, int tbookId) {
        String sql = "DELETE FROM Answer WHERE answer_id = ? AND activity_id = ? AND content_id = ? AND s_id = ? AND c_id = ? AND t_id = ?";
        int rowsAffected = jdbcTemplate.update(sql, answerId, activityId, contentId, sectionId, chapId, tbookId);
        return rowsAffected > 0;
    }

    @Override
    public List<Answer> findAllByActivity(int activityId, int contentId, int sectionId, int chapId, int tbookId) {
        String sql = "SELECT * FROM Answer WHERE activity_id = ? AND content_id = ? AND s_id = ? AND c_id = ? AND t_id = ? ORDER BY answer_id";
        return jdbcTemplate.query(sql, new Object[]{activityId, contentId, sectionId, chapId, tbookId}, new AnswerRowMapper());
    }

    private static class AnswerRowMapper implements RowMapper<Answer> {
        @Override
        public Answer mapRow(ResultSet rs, int rowNum) throws SQLException {
            Answer answer = new Answer();
            answer.setAnswerId(rs.getInt("answer_id"));
            answer.setActivityId(rs.getInt("activity_id"));
            answer.setContentId(rs.getInt("content_id"));
            answer.setSectionId(rs.getInt("s_id"));
            answer.setChapId(rs.getInt("c_id"));
            answer.setTbookId(rs.getInt("t_id"));
            answer.setAnswerText(rs.getString("answer_text"));
            answer.setJustification(rs.getString("justification"));
            return answer;
        }
    }
}
