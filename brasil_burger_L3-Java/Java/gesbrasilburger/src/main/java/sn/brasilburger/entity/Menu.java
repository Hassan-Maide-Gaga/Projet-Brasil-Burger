package sn.brasilburger.entity;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Menu {
    private int id;
    private String nom;
    private String image;
    private Boolean etat = true;
    private Double montant = 0.0;
    private Burger burger;
    private List<Complement> complements = new ArrayList<>();
    @Override
    public String toString(){
        return " Menu->[ID: " + id + ", Nom: " + nom + ", Prix: " + montant + "]";
    }
}
