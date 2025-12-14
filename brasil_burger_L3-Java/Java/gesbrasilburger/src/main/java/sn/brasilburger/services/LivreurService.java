package sn.brasilburger.services;

import java.util.List;
import java.util.Optional;

import sn.brasilburger.entity.Livreur;

public interface LivreurService {
    List<Livreur> getAllLivreur();
    Optional<Livreur> getLivreurById(int id);
    boolean createLivreur(Livreur livreur);
    boolean updateLivreur(Livreur livreur);
    boolean archiveLivreur(Livreur livreur);
}
