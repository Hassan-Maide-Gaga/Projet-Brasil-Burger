package sn.brasilburger.repository;

import java.util.List;
import java.util.Optional;

import sn.brasilburger.entity.Livreur;

public interface LivreurRepository {
    List<Livreur> findAll();
    Optional<Livreur> findById(int id);
    boolean insert(Livreur livreur);
    boolean update(Livreur livreur);
    boolean delete(Livreur livreur);
}
