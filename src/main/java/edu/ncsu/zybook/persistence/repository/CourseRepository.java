package edu.ncsu.zybook.persistence.repository;

import edu.ncsu.zybook.domain.model.ActiveCourse;
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
import java.util.stream.Collectors;

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
        System.out.println("DEBUGGGGG "+ courseId);
        String sql = "SELECT * FROM Course WHERE course_id = ?";
        try {
            Course course = jdbcTemplate.queryForObject(sql, new Object[]{courseId}, new CourseRowMapper());

            System.out.println("DEBUGGGGG 1234s"+course.getCourseId());
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
    public Optional<Course> updateProfessor(Course course, int professorId) {
        String sql = "UPDATE Course SET professor_id = ? WHERE course_id = ?";
        int rowsAffected = jdbcTemplate.update(sql, professorId, course.getCourseId());
        Optional<Course> updatedCourse = findById(course.getCourseId());
        if(rowsAffected > 0){
            return updatedCourse;
        }
        return Optional.empty();
    }

    @Override
    public List<ActiveCourse> getActiveCourse(int userId, String role) {
        if(role.equalsIgnoreCase("faculty")){
            String sql = "SELECT * FROM Course WHERE professor_id = ? AND course_type = 'ACTIVE'";
            List<ActiveCourse> courses = jdbcTemplate.query(sql, new Object[]{userId}, new CourseRowMapper()).stream().map(e->((ActiveCourse) e)).collect(Collectors.toList());
            System.out.println("In repository:"+courses.toString());
            return courses;
        }
       else if(role.equalsIgnoreCase("ta")){
           String sql = "SELECT C.course_id course_id, C.title title, C.start_date start_date, C.end_date end_date, C.course_type course_type, C.textbook_id textbook_id, C.professor_id professor_id FROM Assigned A, Course C WHERE A.course_id = C.course_id AND A.user_id=? AND course_type = 'ACTIVE'";
           List<ActiveCourse> courses = jdbcTemplate.query(sql, new Object[]{userId}, new CourseRowMapper()).stream().map(e->((ActiveCourse) e)).collect(Collectors.toList());
           System.out.println("In repository for TA:"+courses.toString());
           return courses;
       }
       return null;
    }

    @Override
    public List<Course> getEvaluationCourse(int professorId) {
        String sql = "SELECT * FROM Course WHERE professor_id = ? AND course_type = 'EVALUATION'";
        List<Course> evaluationCourses = jdbcTemplate.query(sql, new Object[]{professorId}, new CourseRowMapper());
        return evaluationCourses;
    }

    @Override
    public List<Course> getAllCoursesForUser(int userId) {
        String sql = "SELECT * FROM Course WHERE professor_id = ?";
        List<Course> courses = jdbcTemplate.query(sql, new Object[]{userId}, new CourseRowMapper());
        return courses;
    }

    private class CourseRowMapper implements RowMapper<Course> {
        @Override
        public Course mapRow(ResultSet rs, int rowNum) throws SQLException {
            Course course;
            if(rs.getString("course_type").equalsIgnoreCase("ACTIVE"))
                course = new ActiveCourse();
            else course = new Course();
            course.setCourseId(rs.getString("course_id"));
            course.setTitle(rs.getString("title"));
            course.setStartDate(rs.getDate("start_date"));
            course.setEndDate(rs.getDate("end_date"));
            course.setCourseType(rs.getString("course_type"));
            course.setTbookId(rs.getInt("textbook_id"));
            course.setProfessorId(rs.getInt("professor_id"));
            if(rs.getString("course_type").equalsIgnoreCase("ACTIVE"))
                fetchActiveCourseDetails(course);
            return course;
        }
    }

    private void fetchActiveCourseDetails(Course course) {
        String sql = "SELECT * FROM ActiveCourse WHERE course_id = ?";
        try{
            ActiveCourse result = jdbcTemplate.queryForObject(sql, new Object[]{course.getCourseId()}, new RowMapper<ActiveCourse>() {
                @Override
                public ActiveCourse mapRow(ResultSet rs, int rowNum) throws SQLException {
                    ((ActiveCourse) course).setCourseCapacity(rs.getInt("course_capacity"));
                    ((ActiveCourse) course).setCourseToken(rs.getString("course_token"));
                    return ((ActiveCourse) course);
                }
            });

            ((ActiveCourse) course).setCourseCapacity(result.getCourseCapacity());
            ((ActiveCourse) course).setCourseToken(result.getCourseToken());
        }
        catch(EmptyResultDataAccessException e){
            System.out.println("No active course found with course_id: " + course.getCourseId());
        }
    }
}
