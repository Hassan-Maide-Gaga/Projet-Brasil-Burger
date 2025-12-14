package sn.brasilburger.repository;
import java.util.List;
import java.util.Optional;

import sn.brasilburger.entity.*;
public interface BurgerRepository {
    List<Burger> findAll();
    Optional<Burger> findById(int id);
    boolean insert(Burger burger);
    boolean update(Burger burger);
    boolean delete(Burger burger);
    Optional<Burger> getBurgerByMenu(Menu menu);
}
