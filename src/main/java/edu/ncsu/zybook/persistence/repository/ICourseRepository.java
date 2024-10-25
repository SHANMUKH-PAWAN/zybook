package edu.ncsu.zybook.persistence.repository;

import edu.ncsu.zybook.domain.model.Course;
import org.w3c.dom.Text;

import java.util.List;
import java.util.Optional;

public interface ICourseRepository {
    Course create(Course entity);
    Optional<Course> update(Course entity);
    boolean delete(int id);
    Optional<Course> findById(String id);
    List<Course> findAll(int offset, int limit, String sortBy, String sortDirection);
    Optional<Course> findByTitle(String title);
}
