package sn.brasilburger.services;

import java.util.List;
import java.util.Optional;

import sn.brasilburger.entity.User;

public interface LoginService {
    Optional<User> login(String email, String pwd);
    boolean signup(User user);
    Optional<User> getGesByMail(String email);
    boolean updateGes(User user);
    boolean archiveGes(User user);
    List<User> getAllGes();
}
