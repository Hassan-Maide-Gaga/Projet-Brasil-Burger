package sn.brasilburger.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Complement {
    private int id;
    private String nom;
    private Double prix;
    private String image;
    private Boolean etat = true;
    private CategorieComplement categorie;
    @Override
    public String toString(){
        return " Complement->[ID: " + id + ", Nom: " + nom + ", Prix: " + prix + "]";
    }
}
