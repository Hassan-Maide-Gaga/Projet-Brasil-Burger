package org.example.Repository;

import org.example.Model.Complement;
import org.example.Model.Enum.TypeComplement;
import java.util.List;
import java.util.Optional;

public interface ComplementRepository {
    Complement save(Complement complement);
    Complement update(Complement complement);
    boolean delete(int id);
    boolean archive(int id);
    Optional<Complement> findById(int id);
    List<Complement> findAll();
    List<Complement> findByArchive(boolean archive);
    List<Complement> findByName(String nom);
    List<Complement> findByType(TypeComplement type);
    List<Complement> findBoissons();
    List<Complement> findFrites();
    List<Complement> findByTypeAndArchive(TypeComplement type, boolean archive);
}