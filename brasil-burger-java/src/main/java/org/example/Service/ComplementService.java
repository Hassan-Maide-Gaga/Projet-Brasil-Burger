package org.example.Service;

import org.example.Model.Complement;
import org.example.Model.Enum.TypeComplement;
import java.util.List;

public interface ComplementService {
    Complement creerComplement(String nom, double prix, String description, TypeComplement type, String taille);
    Complement modifierComplement(int id, String nom, double prix, String description, TypeComplement type, String taille);
    boolean supprimerComplement(int id);
    boolean archiverComplement(int id);
    Complement getComplementById(int id);
    List<Complement> getAllComplements();
    List<Complement> getComplementsActifs();
    List<Complement> getByType(TypeComplement type);
    List<Complement> getBoissons();
    List<Complement> getFrites();
    List<Complement> rechercherComplements(String recherche);
}