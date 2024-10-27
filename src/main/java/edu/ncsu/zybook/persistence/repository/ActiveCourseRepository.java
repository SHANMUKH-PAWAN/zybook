package edu.ncsu.zybook.persistence.repository;

import edu.ncsu.zybook.domain.model.ActiveCourse;
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
public class ActiveCourseRepository implements IActiveCourseRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Transactional
    @Override
    public ActiveCourse create(ActiveCourse activeCourse) {
        String sql = "INSERT INTO ActiveCourse (course_token, course_capacity, course_id) VALUES (?, ?, ?)";
        int rowsAffected = jdbcTemplate.update(sql, activeCourse.getCourseToken(), activeCourse.getCourseCapacity(), activeCourse.getCourseId());
        if (rowsAffected > 0) {
            return findById(activeCourse.getCourseId())
                    .orElseThrow(() -> new RuntimeException("Failed to retrieve newly inserted active course."));
        } else {
            throw new RuntimeException("Failed to insert active course.");
        }
    }

    @Override
    public Optional<ActiveCourse> findById(String courseId) {
        String sql = "SELECT * FROM ActiveCourse WHERE course_id = ?";
        try {
            ActiveCourse activeCourse = jdbcTemplate.queryForObject(sql, new Object[]{courseId}, new ActiveCourseRowMapper());
            return Optional.of(activeCourse);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Transactional
    @Override
    public Optional<ActiveCourse> update(ActiveCourse activeCourse) {
        String sql = "UPDATE ActiveCourse SET course_token = ?, course_capacity = ? WHERE course_id = ?";
        int rowsAffected = jdbcTemplate.update(sql, activeCourse.getCourseToken(), activeCourse.getCourseCapacity(), activeCourse.getCourseId());
        return rowsAffected > 0 ? Optional.of(activeCourse) : Optional.empty();
    }

    @Transactional
    @Override
    public boolean delete(String courseId) {
        String sql = "DELETE FROM ActiveCourse WHERE course_id = ?";
        int rowsAffected = jdbcTemplate.update(sql, courseId);
        return rowsAffected > 0;
    }

    private static class ActiveCourseRowMapper implements RowMapper<ActiveCourse> {
        @Override
        public ActiveCourse mapRow(ResultSet rs, int rowNum) throws SQLException {
            ActiveCourse activeCourse = new ActiveCourse();
            activeCourse.setCourseId(rs.getString("course_id"));
            activeCourse.setCourseToken(rs.getString("course_token"));
            activeCourse.setCourseCapacity(rs.getInt("course_capacity"));
            return activeCourse;
        }
    }
}
