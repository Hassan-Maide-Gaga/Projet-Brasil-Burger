package sn.brasilburger.repository;

import java.util.List;
import java.util.Optional;

import sn.brasilburger.entity.User;

public interface LoginRepository {
    Optional<User> getUserByEmail(String email);
    boolean insert(User user);
    boolean update(User user);
    boolean delete(User user);
    public List<User> getAll();
}
