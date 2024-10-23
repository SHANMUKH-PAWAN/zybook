package edu.ncsu.zybook.persistence.repository;

import edu.ncsu.zybook.domain.model.Chapter;
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
public class ChapterRepository implements IChapterRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Chapter> findByTitle(String chapter_code) {
        return Optional.empty();
    }

    @Transactional
    @Override
    public Chapter create(Chapter chapter) {
        String sql = "INSERT INTO Chapter (cno, chapter_code, title, isHidden, tbook_id) VALUES(?)";
        int rowsAffected = jdbcTemplate.update(sql, chapter.getCno());
        if(rowsAffected > 0)
        {
            return findById(chapter.getCno())
                    .orElseThrow(() -> new RuntimeException("Failed to retrieve newly inserted textbook."));
        }
        else{
            throw new RuntimeException("Failed to insert textbook.");
        }
    }

    @Transactional
    @Override
    public Optional<Chapter> update(Chapter entity) {
        return Optional.empty();
    }

    @Transactional
    @Override
    public boolean delete(int id) {
        return false;
    }

    @Override
    public Optional<Chapter> findById(int id) {
        return Optional.empty();
    }

    @Override
    public List<Chapter> findAll(int offset, int limit, String sortBy, String sortDirection) {
        return List.of();
    }
}
