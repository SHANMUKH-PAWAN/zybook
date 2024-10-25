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
    private JdbcTemplate jdbcTemplate;

    @Transactional
    @Override
    public Content create(Content content) {
        String sql = "INSERT INTO Content (content_id, section_id, chap_id, tbook_id, content_type, owned_by, is_hidden) VALUES (?, ?, ?, ?, ?, ?, ?)";
        int rowsAffected = jdbcTemplate.update(sql, content.getContentId(), content.getSectionId(), content.getChapId(),
                content.getTbook_id(), content.getContentType(), content.getOwnedBy(), content.isHidden());
        if (rowsAffected > 0) {
            return content;
        } else {
            throw new RuntimeException("Failed to insert content.");
        }
    }

    @Transactional
    @Override
    public Optional<Content> update(Content content) {
        String sql = "UPDATE Content SET content_type = ?, owned_by = ?, is_hidden = ? WHERE content_id = ? AND section_id = ? AND chap_id = ? AND tbook_id = ?";
        int rowsAffected = jdbcTemplate.update(sql, content.getContentType(), content.getOwnedBy(), content.isHidden(),
                content.getContentId(), content.getSectionId(), content.getChapId(), content.getTbook_id());
        return rowsAffected > 0 ? Optional.of(content) : Optional.empty();
    }

    @Transactional
    @Override
    public boolean delete(int contentId, int sectionId, int chapId, int tbook_id) {
        String sql = "DELETE FROM Content WHERE content_id = ? AND section_id = ? AND chap_id = ? AND tbook_id = ?";
        int rowsAffected = jdbcTemplate.update(sql, contentId, sectionId, chapId, tbook_id);

        return rowsAffected > 0;
    }

    @Override
    public Optional<Content> findById(int contentId, int sectionId, int chapId, int tbook_id) {
        String sql = "SELECT * FROM Content WHERE content_id = ? AND section_id = ? AND chap_id = ? AND tbook_id = ?";
        try {
            Content content = jdbcTemplate.queryForObject(sql, new Object[]{contentId, sectionId, chapId, tbook_id}, new ContentRowMapper());
            return Optional.of(content);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Content> findAllBySection(int sectionId, int chapId, int tbook_id) {
        String sql = "SELECT * FROM Content WHERE section_id = ? AND chap_id = ? AND tbook_id = ?";
        return jdbcTemplate.query(sql, new Object[]{sectionId, chapId, tbook_id}, new ContentRowMapper());
    }

    private static class ContentRowMapper implements RowMapper<Content> {
        @Override
        public Content mapRow(ResultSet rs, int rowNum) throws SQLException {
            Content content = new Content();
            content.setContentId(rs.getInt("content_id"));
            content.setSectionId(rs.getInt("section_id"));
            content.setChapId(rs.getInt("chap_id"));
            content.setTbook_id(rs.getInt("tbook_id"));
            content.setContentType(rs.getString("content_type"));
            content.setOwnedBy(rs.getString("owned_by"));
            content.setHidden(rs.getBoolean("is_hidden"));
            return content;
        }
    }
}
