package edu.ncsu.zybook.persistence.repository;

import edu.ncsu.zybook.domain.model.Course;
import org.w3c.dom.Text;

import java.util.Optional;

public interface ICourseRepository extends BaseRepository<Course> {
    Optional<Course> findByTitle(String title);
}
