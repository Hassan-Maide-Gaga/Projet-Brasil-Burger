package sn.brasilburger.repository;

import java.util.List;
import java.util.Optional;

import sn.brasilburger.entity.Menu;

public interface MenuRepository {
    List<Menu> findAll();
    Optional<Menu> findById(int id);
    boolean insert(Menu menu);
    boolean update(Menu menu);
    boolean delete(Menu menu);
}
