package edu.ncsu.zybook.persistence.repository;

import edu.ncsu.zybook.domain.model.Textbook;
import edu.ncsu.zybook.domain.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository implements IUserRepository{
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public Optional<User> findById(int id) {
        String sql = "SELECT * FROM user WHERE user_id = ?";
        try{
            User user = jdbcTemplate.queryForObject(sql, new Object[]{id}, new UserRepository.UserRowMapper());
            return Optional.of(user);
        }
        catch(EmptyResultDataAccessException e){
            return Optional.empty();
        }
    }

    @Override
    public User create(User user) {
        String sql = "INSERT INTO User (fname,lname,email,password,role_name) VALUES(?,?,?,?,?)";
        int rowsAffected = jdbcTemplate.update(sql, user.getUserId());
        if(rowsAffected > 0)
        {
            return findById(user.getUserId())
                    .orElseThrow(() -> new RuntimeException("Failed to retrieve newly inserted User."));
        }
        else{
            throw new RuntimeException("Failed to insert User.");
        }
    }

    @Override
    public Optional<User> update(User user) {
        String sql = "UPDATE User SET fname = ?, SET lname = ? WHERE user_id = ?";
        int rowsAffected = jdbcTemplate.update(sql, user.getFname(), user.getLname());
        return rowsAffected > 0 ? Optional.of(user) : Optional.empty();
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM User WHERE user_id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        return rowsAffected > 0;
    }

    @Override
    public List<User> getAllUsers() {
        String sql = "SELECT * FROM User";
        return jdbcTemplate.query(sql, new UserRepository.UserRowMapper());
    }

    private static class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setUserId(rs.getInt("user_id"));
            user.setFname(rs.getString("fname"));
            user.setLname(rs.getString("lname"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));
            return user;
        }
    }
}

