package edu.ncsu.zybook.persistence.repository;

import edu.ncsu.zybook.domain.model.Course;
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
public class CourseRepository implements ICourseRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Course> findByTitle(String title) {
        String sql = "SELECT * FROM course WHERE title = ?";
        try{
            Course course = jdbcTemplate.queryForObject(sql, new Object[]{title}, new CourseRepository.CourseRowMapper());
            return Optional.of(course);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }


    @Transactional
    @Override
    public Course create(Course course) {
        String sql = "INSERT INTO Course (course_id, title, start_date, end_date, course_type, textbook_id) VALUES(?,?,?,?,?,?)";
        int rowsAffected = jdbcTemplate.update(sql, course.getCourseId(), course.getTitle(), course.getStartDate(), course.getEndDate(), course.getCourseType(), course.getTbookId());
        if(rowsAffected > 0)
        {
            return findById(course.getCourseId())
                    .orElseThrow(() -> new RuntimeException("Failed to retrieve newly inserted Course."));
        }
        else{
            throw new RuntimeException("Failed to insert Course.");
        }
    }

    @Override
    public Optional<Course> findById(String courseId) {
        String sql = "SELECT * FROM Course WHERE course_id = ?";
        try {
            Course course = jdbcTemplate.queryForObject(sql, new Object[]{courseId}, new CourseRowMapper());
            return Optional.of(course);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Transactional
    @Override
    public Optional<Course> update(Course course) {
        String sql = "UPDATE Course SET title = ?, start_date = ?, end_date = ?, course_type = ?, textbook_id = ? WHERE course_id = ?";
        int rowsAffected = jdbcTemplate.update(sql, course.getTitle(), course.getStartDate(), course.getEndDate(), course.getCourseType(), course.getTbookId(), course.getCourseId());
        return rowsAffected > 0 ? Optional.of(course) : Optional.empty();
    }

    @Transactional
    @Override
    public boolean delete(String courseId) {
        String sql = "DELETE FROM Course WHERE course_id = ?";
        int rowsAffected = jdbcTemplate.update(sql, courseId);
        return rowsAffected > 0;
    }

    @Override
    public List<Course> findAll() {
        String sql = "SELECT * FROM Course";
        return jdbcTemplate.query(sql, new CourseRowMapper());
    }

    private static class CourseRowMapper implements RowMapper<Course> {
        @Override
        public Course mapRow(ResultSet rs, int rowNum) throws SQLException {
            Course course = new Course();
            course.setCourseId(rs.getString("course_id"));
            course.setTitle(rs.getString("title"));
            course.setStartDate(rs.getDate("start_date"));
            course.setEndDate(rs.getDate("end_date"));
            course.setCourseType(rs.getString("course_type"));
            course.setTbookId(rs.getInt("textbook_id"));
            return course;
        }
    }
}
