package org.example.Vue;

import org.example.Model.Burger;
import org.example.Model.Complement;
import org.example.Model.Enum.TypeComplement;
import org.example.Model.Menu;
import org.example.Service.BurgerService;
import org.example.Service.ComplementService;
import org.example.Service.MenuService;
import org.example.config.factory.service.ServiceFactory;
import java.util.List;

public class ComplementVue extends Vue {

    private final ComplementService complementService;

    public ComplementVue() {
        this.complementService = ServiceFactory.getComplementService();
    }

    public void afficherMenuComplement() {
        boolean continuer = true;

        while (continuer) {
            afficherTitre("GESTION DES COMPLÉMENTS");
            String[] options = {
                    "Lister tous les compléments",
                    "Lister par type",
                    "Ajouter un complément",
                    "Modifier un complément",
                    "Archiver un complément",
                    "Supprimer un complément",
                    "Rechercher un complément",
                    "Lister les boissons",
                    "Lister les frites",
                    "Lister les sauces"
            };
            afficherMenu(options);

            int choix = lireChoix("\nVotre choix: ");

            switch (choix) {
                case 1:
                    listerTousComplements();
                    break;
                case 2:
                    listerParType();
                    break;
                case 3:
                    ajouterComplement();
                    break;
                case 4:
                    modifierComplement();
                    break;
                case 5:
                    archiverComplement();
                    break;
                case 6:
                    supprimerComplement();
                    break;
                case 7:
                    rechercherComplement();
                    break;
                case 8:
                    listerBoissons();
                    break;
                case 9:
                    listerFrites();
                    break;
                case 10:
                    listerSauces();
                    break;
                case 0:
                    continuer = false;
                    break;
                default:
                    afficherLigne("Choix invalide!");
            }
        }
    }

    private void listerTousComplements() {
        List<Complement> complements = complementService.getAllComplements();
        afficherTitre("LISTE DES COMPLÉMENTS (" + complements.size() + ")");

        if (complements.isEmpty()) {
            afficherLigne("Aucun complément trouvé.");
            return;
        }

        for (Complement complement : complements) {
            afficherLigne(formatComplement(complement));
        }
    }

    private void listerParType() {
        afficherTitre("LISTE PAR TYPE DE COMPLÉMENT");
        System.out.println("Types disponibles:");
        for (TypeComplement type : TypeComplement.values()) {
            System.out.println("- " + type);
        }

        String typeStr = lireString("Type à afficher: ").toUpperCase();
        try {
            TypeComplement type = TypeComplement.valueOf(typeStr);
            List<Complement> complements = complementService.getByType(type);

            afficherTitre("COMPLÉMENTS DE TYPE " + type + " (" + complements.size() + ")");

            if (complements.isEmpty()) {
                afficherLigne("Aucun complément de ce type.");
                return;
            }

            for (Complement complement : complements) {
                afficherLigne(formatComplement(complement));
            }
        } catch (IllegalArgumentException e) {
            afficherLigne("❌ Type invalide!");
        }
    }

    private void ajouterComplement() {
        afficherTitre("AJOUT D'UN NOUVEAU COMPLÉMENT");

        String nom = lireString("Nom: ");
        double prix = lireDouble("Prix (FCFA): ");
        String description = lireString("Description: ");

        // Choix du type
        afficherLigne("\nTypes disponibles:");
        TypeComplement[] types = TypeComplement.values();
        for (int i = 0; i < types.length; i++) {
            afficherLigne((i + 1) + ". " + types[i]);
        }

        int choixType = lireChoix("Type (1-" + types.length + "): ");
        if (choixType < 1 || choixType > types.length) {
            afficherLigne("Choix invalide!");
            return;
        }
        TypeComplement type = types[choixType - 1];

        // Taille (seulement pour boissons et frites)
        String taille = null;
        /*
        if (type == TypeComplement.BOISSON || type == TypeComplement.FRITE) {
            afficherLigne("\nTailles disponibles:");
            afficherLigne("1. PETITE");
            afficherLigne("2. MOYENNE");
            afficherLigne("3. GRANDE");
            int choixTaille = lireChoix("Taille (1-3): ");
            switch (choixTaille) {
                case 1: taille = "PETITE"; break;
                case 2: taille = "MOYENNE"; break;
                case 3: taille = "GRANDE"; break;
                default: taille = "MOYENNE";
            }
        }*/

        try {
            Complement complement = complementService.creerComplement(nom, prix, description, type, taille);
            afficherLigne("✅ Complément créé avec succès!");
            afficherLigne(formatComplementDetail(complement));
        } catch (Exception e) {
            afficherLigne("❌ Erreur lors de la création: " + e.getMessage());
        }
    }

    private void modifierComplement() {
        afficherTitre("MODIFICATION D'UN COMPLÉMENT");

        int id = lireChoix("ID du complément à modifier: ");

        try {
            Complement complement = complementService.getComplementById(id);
            afficherLigne("Complément actuel:");
            afficherLigne(formatComplementDetail(complement));

            String nom = lireString("\nNouveau nom (laisser vide pour garder '" + complement.getNom() + "'): ");
            String prixStr = lireString("Nouveau prix (laisser vide pour garder " + complement.getPrix() + "): ");
            String description = lireString("Nouvelle description (laisser vide pour garder): ");

            // Modification du type
            afficherLigne("\nChanger le type?");
            String changerType = lireString("(oui/non): ");
            TypeComplement type = complement.getTypeComplement();
            String taille = complement.getTaille();

            if (changerType.equalsIgnoreCase("oui")) {
                afficherLigne("Types disponibles:");
                TypeComplement[] types = TypeComplement.values();
                for (int i = 0; i < types.length; i++) {
                    afficherLigne((i + 1) + ". " + types[i]);
                }
                int choixType = lireChoix("Nouveau type (1-" + types.length + "): ");
                if (choixType >= 1 && choixType <= types.length) {
                    type = types[choixType - 1];

                    // Redemander la taille si nécessaire
                    /*
                    if (type == TypeComplement.BOISSON || type == TypeComplement.FRITE) {
                        afficherLigne("\nTailles disponibles:");
                        afficherLigne("1. PETITE");
                        afficherLigne("2. MOYENNE");
                        afficherLigne("3. GRANDE");
                        int choixTaille = lireChoix("Taille (1-3): ");
                        switch (choixTaille) {
                            case 1: taille = "PETITE"; break;
                            case 2: taille = "MOYENNE"; break;
                            case 3: taille = "GRANDE"; break;
                            default: taille = "MOYENNE";
                        }
                    } else {
                        taille = null;
                    }*/
                }
            }

            // Appliquer les modifications
            if (!nom.isEmpty()) complement.setNom(nom);
            if (!prixStr.isEmpty()) complement.setPrix(Double.parseDouble(prixStr));
            if (!description.isEmpty()) complement.setDescription(description);
            complement.setTypeComplement(type);
            complement.setTaille(taille);

            Complement complementModifie = complementService.modifierComplement(
                    id,
                    complement.getNom(),
                    complement.getPrix(),
                    complement.getDescription(),
                    type,
                    taille
            );

            afficherLigne("✅ Complément modifié avec succès!");
            afficherLigne(formatComplementDetail(complementModifie));

        } catch (Exception e) {
            afficherLigne("❌ Erreur lors de la modification: " + e.getMessage());
        }
    }

    private void archiverComplement() {
        afficherTitre("ARCHIVAGE D'UN COMPLÉMENT");

        int id = lireChoix("ID du complément à archiver: ");

        try {
            complementService.archiverComplement(id);
            afficherLigne("✅ Complément archivé avec succès!");
        } catch (Exception e) {
            afficherLigne("❌ Erreur lors de l'archivage: " + e.getMessage());
        }
    }

    private void supprimerComplement() {
        afficherTitre("SUPPRESSION D'UN COMPLÉMENT");

        int id = lireChoix("ID du complément à supprimer: ");

        try {
            Complement complement = complementService.getComplementById(id);
            afficherLigne("Complément à supprimer: " + complement.getNom());
            String confirmation = lireString("Êtes-vous sûr? (oui/non): ");

            if (confirmation.equalsIgnoreCase("oui")) {
                boolean success = complementService.supprimerComplement(id);
                if (success) {
                    afficherLigne("✅ Complément supprimé avec succès!");
                } else {
                    afficherLigne("❌ Erreur lors de la suppression.");
                }
            } else {
                afficherLigne("Suppression annulée.");
            }
        } catch (Exception e) {
            afficherLigne("❌ Erreur: " + e.getMessage());
        }
    }

    private void rechercherComplement() {
        afficherTitre("RECHERCHE DE COMPLÉMENT");

        String recherche = lireString("Nom à rechercher: ");
        List<Complement> complements = complementService.rechercherComplements(recherche);

        afficherTitre("RÉSULTATS (" + complements.size() + ")");

        if (complements.isEmpty()) {
            afficherLigne("Aucun complément trouvé.");
            return;
        }

        for (Complement complement : complements) {
            afficherLigne(formatComplement(complement));
        }
    }

    private void listerBoissons() {
        List<Complement> boissons = complementService.getBoissons();
        afficherTitre("BOISSONS DISPONIBLES (" + boissons.size() + ")");

        if (boissons.isEmpty()) {
            afficherLigne("Aucune boisson disponible.");
            return;
        }

        for (Complement boisson : boissons) {
            afficherLigne(formatComplementDetail(boisson));
        }
    }

    private void listerFrites() {
        List<Complement> frites = complementService.getFrites();
        afficherTitre("FRITES DISPONIBLES (" + frites.size() + ")");

        if (frites.isEmpty()) {
            afficherLigne("Aucune frite disponible.");
            return;
        }

        for (Complement frite : frites) {
            afficherLigne(formatComplementDetail(frite));
        }
    }

    private void listerSauces() {
        List<Complement> sauces = complementService.getByType(TypeComplement.SAUCE);
        afficherTitre("SAUCES DISPONIBLES (" + sauces.size() + ")");

        if (sauces.isEmpty()) {
            afficherLigne("Aucune sauce disponible.");
            return;
        }

        for (Complement sauce : sauces) {
            afficherLigne(formatComplementDetail(sauce));
        }
    }

    private String formatComplement(Complement complement) {
        return String.format("ID: %d | %s | %.0f FCFA | %s | %s",
                complement.getId(),
                complement.getNom(),
                complement.getPrix(),
                complement.getTypeComplement(),
                complement.isEstArchive() ? "[ARCHIVÉ]" : "[ACTIF]"
        );
    }

    private String formatComplementDetail(Complement complement) {
        StringBuilder sb = new StringBuilder();
        sb.append("- ").append(complement.getNom())
                .append(" (").append(complement.getTypeComplement()).append(")")
                .append(" - ").append(complement.getPrix()).append(" FCFA");

        if (complement.getTaille() != null) {
            sb.append(" - Taille: ").append(complement.getTaille());
        }

        if (complement.getDescription() != null && !complement.getDescription().isEmpty()) {
            sb.append("\n  Description: ").append(complement.getDescription());
        }

        if (complement.isEstArchive()) {
            sb.append(" [ARCHIVÉ]");
        }

        return sb.toString();
    }
}