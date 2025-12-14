package sn.brasilburger.services;

import java.util.List;
import java.util.Optional;

import sn.brasilburger.entity.Complement;

public interface ComplementService {
    List<Complement> getAllComplement();
    Optional<Complement> getComplementById(int id);
    boolean createComplement(Complement complement);
    boolean updateComplement(Complement complement);
    boolean archiveComplement(Complement complement);
    List<Complement> getComplementsByMenu(int idMenu);
}
