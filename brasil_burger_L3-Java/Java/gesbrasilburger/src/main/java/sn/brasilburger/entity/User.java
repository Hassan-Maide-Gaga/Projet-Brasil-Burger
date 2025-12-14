package sn.brasilburger.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class User {
    private int id;
    private String nomComplet;
    private String telephone;
    private String email;
    private String password;
    private RoleUser role;
    private boolean etat = true;
    @Override
    public String toString(){
        return " Complement->[ID: " + id + ", Nom Complet: " + nomComplet + ", Telephone: " + 
        telephone + ", Email: " + email + "]";
    }
}

