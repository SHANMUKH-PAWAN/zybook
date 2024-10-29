package edu.ncsu.zybook.service;

import edu.ncsu.zybook.domain.model.Course;

import java.util.List;
import java.util.Optional;

public interface ICourseService {
    Course create(Course entity);
    Optional<Course> update(Course entity);
    boolean delete(String id);
    Optional<Course> findById(String id);
    List<Course> findAll();
    Optional<Course> findByTitle(String title);
}
