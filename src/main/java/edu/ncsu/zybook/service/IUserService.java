package edu.ncsu.zybook.service;

import edu.ncsu.zybook.domain.model.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    User create(User user);
    Optional<User> findById(int id);
    Optional<User> update(User user);
    boolean delete(int id);
    List<User> getAllUsers();
}
