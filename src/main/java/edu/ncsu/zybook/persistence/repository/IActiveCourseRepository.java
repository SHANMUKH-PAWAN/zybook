package edu.ncsu.zybook.persistence.repository;

import edu.ncsu.zybook.domain.model.ActiveCourse;
import org.w3c.dom.Text;

import java.util.Optional;

public interface IActiveCourseRepository extends BaseRepository<ActiveCourse> {
    Optional<ActiveCourse> findByTitle(String title);
}

