package sn.brasilburger.repository;

import java.util.List;
import java.util.Optional;

import sn.brasilburger.entity.*;

public interface ComplementRepository {
    List<Complement> findAll();
    Optional<Complement> findById(int id);
    boolean insert(Complement complement);
    boolean update(Complement complement);
    boolean delete(Complement complement);
    List<Complement> getComplementsByMenu(int idMenu);
}
