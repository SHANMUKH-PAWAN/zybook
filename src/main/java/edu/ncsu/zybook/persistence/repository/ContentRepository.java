package edu.ncsu.zybook.persistence.repository;

import edu.ncsu.zybook.domain.model.Content;
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
public class ContentRepository implements IContentRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Transactional
    @Override
    public Content create(Content content) {
        String sql = "INSERT INTO content (contentId, isHidden, ownedBy, sectionId, tbook_id, chapId) VALUES (?, ?, ?, ?, ?, ?)";
        int rowsAffected = jdbcTemplate.update(sql, content.getContentId(), content.isHidden(), content.getOwnedBy(), content.getSectionId(), content.getTbook_id(), content.getChapId());
        if (rowsAffected > 0) {
            return findById(content.getContentId())
                    .orElseThrow(() -> new RuntimeException("Failed to retrieve newly inserted content"));
        }
        else{
            throw new RuntimeException("Failed to insert content");
        }
    }

    @Transactional
    @Override
    public Optional<Content> update(Content content) {
        String sql = "UPDATE Content SET isHidden = ?, ownedBy = ?, sectionId = ?, tbook_id = ?, chapId = ? WHERE contentId = ?";
        int rowsAffected = jdbcTemplate.update(sql, content.isHidden(), content.getOwnedBy(), content.getSectionId(), content.getTbook_id(), content.getChapId(), content.getContentId());
        return rowsAffected > 0 ? Optional.of(content): Optional.empty();
    }

    @Transactional
    @Override
    public boolean delete(int tbook_id, int chap_id, int section_id, int content_id) {
        String sql = "DELETE FROM Content WHERE t_id = ? AND c_id = ? AND s_id = ? AND content_id = ?";
        int rowsAffected = jdbcTemplate.update(sql, tbook_id, chap_id, section_id, content_id);
        return rowsAffected > 0;
    }

    @Override
    public Optional<Content> findById(int id) {
        String sql = "SELECT * FROM Content WHERE contentId = ?";
        try{
            Content content = jdbcTemplate.queryForObject(sql, new Object[]{id}, new ContentRowMapper());
            return Optional.of(content);
        } catch (EmptyResultDataAccessException e){
            return Optional.empty();
        }
    }

    @Override
    public List<Content> findAll(int offset, int limit, String sortBy, String sortDirection, int tbook_id, int sectionId, int chapId) {
        String validSortDirection = sortDirection.equalsIgnoreCase("DESC") ? "DESC" : "ASC";
        String validSortBy = validateSortBy(sortBy);
        String sql = "SELECT * FROM Content WHERE tbook_id = ?, sectionId = ?, chapId = ? ORDER BY " + validSortBy + " " + validSortDirection + " LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, new Object[]{tbook_id, sectionId, chapId, limit, offset}, new ContentRowMapper());
    }

    private static class ContentRowMapper implements RowMapper<Content> {
        @Override
        public Content mapRow(ResultSet rs, int rowNum) throws SQLException {
            Content content = new Content();
            content.setContentId(rs.getInt("contentId"));
            content.setHidden(rs.getBoolean("isHidden"));
            content.setOwnedBy(rs.getString("ownedBy"));
            content.setSectionId(rs.getInt("sectionId"));
            content.setChapId(rs.getInt("chapId"));
            content.setTbook_id(rs.getInt("tbook_id"));
            return content;
        }
    }

    private String validateSortBy(String sortBy) {
        // List of allowed columns to sort by in table
        List<String> allowedSortColumns = List.of("contentId", "ownedBy", "sectionId", "tbook_id", "chapId");

        if (allowedSortColumns.contains(sortBy)) {
            return sortBy;
        } else {
            return "contentId";
        }
    }
}
