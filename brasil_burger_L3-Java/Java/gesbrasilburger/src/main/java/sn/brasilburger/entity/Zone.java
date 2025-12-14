package sn.brasilburger.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Zone {
    private int id;
    private String nom;
    private Double prixLivraison;
    private boolean etat = true;
    public String toString(){
        return " Complement->[ID: " + id + ", Nom: " + nom + ", Prix Livraison: " + prixLivraison + "]";
    }
}
