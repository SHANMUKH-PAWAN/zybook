package edu.ncsu.zybook.persistence.repository;

import edu.ncsu.zybook.domain.model.Course;
import org.w3c.dom.Text;

import java.util.List;
import java.util.Optional;

public interface ICourseRepository {
    Course create(Course entity);
    Optional<Course> update(Course entity);
    boolean delete(String id);
    Optional<Course> findById(String id);
    List<Course> findAll();
    Optional<Course> findByTitle(String title);
}
