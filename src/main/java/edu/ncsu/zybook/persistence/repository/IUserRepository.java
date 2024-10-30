package edu.ncsu.zybook.persistence.repository;

import edu.ncsu.zybook.domain.model.User;

import java.util.List;
import java.util.Optional;

public interface IUserRepository {
    Optional<User> findById(int id);
    User create(User user);
    Optional<User> update(User user);
    boolean delete(int id);
    Optional<User> findByEmail(String email);
    List<User> getAllUsers();
}
