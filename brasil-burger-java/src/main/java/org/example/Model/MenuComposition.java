// Fichier: src/main/java/org/example/model/MenuComposition.java
package org.example.model;

public class MenuComposition {
    private Integer id;
    private Integer menuId;
    private Integer burgerId;
    private Integer complementId;
    private TypeElement typeElement;

    // Relations
    private Burger burger;
    private Complement complement;

    public enum TypeElement {
        BURGER, BOISSON, FRITE
    }

    // Constructeur vide
    public MenuComposition() {
    }

    // Constructeur avec paramètres
    public MenuComposition(Integer menuId, Integer burgerId, Integer complementId, TypeElement typeElement) {
        this.menuId = menuId;
        this.burgerId = burgerId;
        this.complementId = complementId;
        this.typeElement = typeElement;
    }

    // Getters et Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }

    public Integer getBurgerId() {
        return burgerId;
    }

    public void setBurgerId(Integer burgerId) {
        this.burgerId = burgerId;
    }

    public Integer getComplementId() {
        return complementId;
    }

    public void setComplementId(Integer complementId) {
        this.complementId = complementId;
    }

    public TypeElement getTypeElement() {
        return typeElement;
    }

    public void setTypeElement(TypeElement typeElement) {
        this.typeElement = typeElement;
    }

    public Burger getBurger() {
        return burger;
    }

    public void setBurger(Burger burger) {
        this.burger = burger;
    }

    public Complement getComplement() {
        return complement;
    }

    public void setComplement(Complement complement) {
        this.complement = complement;
    }

    @Override
    public String toString() {
        return String.format("MenuComposition[id=%d, menuId=%d, type=%s]",
                id, menuId, typeElement);
    }
}