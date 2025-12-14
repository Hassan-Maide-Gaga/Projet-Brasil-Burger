// Fichier: src/main/java/org/example/vue/ComplementVue.java
package org.example.vue;

import org.example.model.Complement;
import org.example.service.ComplementService;

import java.io.File;
import java.util.List;

public class ComplementVue extends Vue {
    private final ComplementService complementService;

    public ComplementVue() {
        super();
        this.complementService = new ComplementService();
    }

    @Override
    public void afficher() {
        boolean continuer = true;

        while (continuer) {
            afficherMenuComplement();
            Integer choix = lireEntier("\nVotre choix");

            if (choix == null) {
                continue;
            }

            switch (choix) {
                case 1:
                    creerComplement();
                    break;
                case 2:
                    listerComplements();
                    break;
                case 3:
                    listerComplementsActifs();
                    break;
                case 4:
                    listerBoissons();
                    break;
                case 5:
                    listerFrites();
                    break;
                case 6:
                    modifierComplement();
                    break;
                case 7:
                    archiverComplement();
                    break;
                case 8:
                    supprimerComplement();
                    break;
                case 0:
                    continuer = false;
                    break;
                default:
                    afficherErreur("Choix invalide");
            }

            if (continuer && choix != 0) {
                pause();
            }
        }
    }

    private void afficherMenuComplement() {
        afficherTitre("GESTION DES COMPLÉMENTS");
        System.out.println("1. Créer un complément");
        System.out.println("2. Lister tous les compléments");
        System.out.println("3. Lister les compléments actifs");
        System.out.println("4. Lister les boissons");
        System.out.println("5. Lister les frites");
        System.out.println("6. Modifier un complément");
        System.out.println("7. Archiver un complément");
        System.out.println("8. Supprimer un complément");
        System.out.println("0. Retour");
        afficherSeparateur();
    }

    private void creerComplement() {
        afficherTitre("CRÉER UN COMPLÉMENT");

        String nom = lireChaine("Nom du complément");
        if (nom.isEmpty()) {
            afficherErreur("Le nom est obligatoire");
            return;
        }

        Double prix = lireDouble("Prix (FCFA)");
        if (prix == null || prix <= 0) {
            afficherErreur("Le prix doit être supérieur à 0");
            return;
        }

        System.out.println("\nType de complément:");
        System.out.println("1. Boisson");
        System.out.println("2. Frites");
        System.out.println("3. Autre");

        Integer typeChoix = lireEntier("Sélectionnez le type");
        Complement.TypeComplement type;

        switch (typeChoix) {
            case 1:
                type = Complement.TypeComplement.BOISSON;
                break;
            case 2:
                type = Complement.TypeComplement.FRITE;
                break;
            case 3:
                type = Complement.TypeComplement.AUTRE;
                break;
            default:
                afficherErreur("Type invalide");
                return;
        }

        boolean avecImage = confirmer("Voulez-vous ajouter une image ?");

        Complement complement;
        if (avecImage) {
            String cheminImage = lireChaine("Chemin complet de l'image");
            File imageFile = new File(cheminImage);

            if (!imageFile.exists()) {
                afficherErreur("Fichier image introuvable");
                return;
            }

            complement = complementService.creerComplementAvecImage(nom, prix, type, imageFile);
        } else {
            complement = complementService.creerComplement(nom, prix, type);
        }

        if (complement != null) {
            afficherSucces("Complément créé avec succès (ID: " + complement.getId() + ")");
            afficherDetailsComplement(complement);
        } else {
            afficherErreur("Échec de la création du complément");
        }
    }

    private void listerComplements() {
        afficherTitre("LISTE DE TOUS LES COMPLÉMENTS");

        List<Complement> complements = complementService.listerComplements();

        if (complements.isEmpty()) {
            afficherMessage("Aucun complément trouvé");
            return;
        }

        afficherListeComplements(complements);
    }

    private void listerComplementsActifs() {
        afficherTitre("LISTE DES COMPLÉMENTS ACTIFS");

        List<Complement> complements = complementService.listerComplementsActifs();

        if (complements.isEmpty()) {
            afficherMessage("Aucun complément actif trouvé");
            return;
        }

        afficherListeComplements(complements);
    }

    private void listerBoissons() {
        afficherTitre("LISTE DES BOISSONS");

        List<Complement> complements = complementService.listerBoissons();

        if (complements.isEmpty()) {
            afficherMessage("Aucune boisson trouvée");
            return;
        }

        afficherListeComplements(complements);
    }

    private void listerFrites() {
        afficherTitre("LISTE DES FRITES");

        List<Complement> complements = complementService.listerFrites();

        if (complements.isEmpty()) {
            afficherMessage("Aucune frite trouvée");
            return;
        }

        afficherListeComplements(complements);
    }

    private void afficherListeComplements(List<Complement> complements) {
        System.out.println(String.format("\n%-5s %-30s %-15s %-15s %-15s",
                "ID", "Nom", "Type", "Prix (FCFA)", "Statut"));
        afficherSeparateur();

        for (Complement complement : complements) {
            System.out.println(String.format("%-5d %-30s %-15s %-15.2f %-15s",
                    complement.getId(),
                    complement.getNom(),
                    complement.getTypeComplement(),
                    complement.getPrix(),
                    complement.getStatutArchivage()));
        }

        System.out.println("\nTotal: " + complements.size() + " complément(s)");
    }

    private void afficherDetailsComplement(Complement complement) {
        System.out.println("\n--- Détails du Complément ---");
        System.out.println("ID: " + complement.getId());
        System.out.println("Nom: " + complement.getNom());
        System.out.println("Type: " + complement.getTypeComplement());
        System.out.println("Prix: " + complement.getPrix() + " FCFA");
        System.out.println("Image URL: " + (complement.getImageUrl() != null ? complement.getImageUrl() : "Aucune image"));
        System.out.println("Statut: " + complement.getStatutArchivage());
        System.out.println("Date création: " + complement.getDateCreation());
    }

    private void modifierComplement() {
        afficherTitre("MODIFIER UN COMPLÉMENT");

        Integer id = lireEntier("ID du complément à modifier");
        if (id == null) {
            afficherErreur("ID invalide");
            return;
        }

        Complement complement = complementService.obtenirComplement(id);
        if (complement == null) {
            afficherErreur("Complément introuvable");
            return;
        }

        afficherDetailsComplement(complement);
        System.out.println("\n(Laissez vide pour conserver la valeur actuelle)");

        String nom = lireChaine("Nouveau nom");
        Double prix = lireDouble("Nouveau prix (FCFA)");

        System.out.println("\nNouveau type (0 pour conserver):");
        System.out.println("1. Boisson");
        System.out.println("2. Frites");
        System.out.println("3. Autre");

        Integer typeChoix = lireEntier("Sélectionnez le type");
        Complement.TypeComplement type = null;

        if (typeChoix != null && typeChoix != 0) {
            switch (typeChoix) {
                case 1:
                    type = Complement.TypeComplement.BOISSON;
                    break;
                case 2:
                    type = Complement.TypeComplement.FRITE;
                    break;
                case 3:
                    type = Complement.TypeComplement.AUTRE;
                    break;
            }
        }

        Complement modifie = complementService.modifierComplement(id,
                nom.isEmpty() ? null : nom,
                prix,
                type);

        if (modifie != null) {
            afficherSucces("Complément modifié avec succès");

            if (confirmer("Voulez-vous modifier l'image ?")) {
                String cheminImage = lireChaine("Chemin complet de la nouvelle image");
                File imageFile = new File(cheminImage);

                if (imageFile.exists()) {
                    if (complementService.modifierImageComplement(id, imageFile)) {
                        afficherSucces("Image modifiée avec succès");
                    }
                }
            }
        }
    }

    private void archiverComplement() {
        afficherTitre("ARCHIVER UN COMPLÉMENT");

        Integer id = lireEntier("ID du complément à archiver");
        if (id == null) {
            afficherErreur("ID invalide");
            return;
        }

        Complement complement = complementService.obtenirComplement(id);
        if (complement == null) {
            afficherErreur("Complément introuvable");
            return;
        }

        afficherDetailsComplement(complement);

        if (confirmer("\nConfirmez-vous l'archivage de ce complément ?")) {
            if (complementService.archiverComplement(id)) {
                afficherSucces("Complément archivé avec succès");
            } else {
                afficherErreur("Échec de l'archivage");
            }
        }
    }

    private void supprimerComplement() {
        afficherTitre("SUPPRIMER UN COMPLÉMENT");

        Integer id = lireEntier("ID du complément à supprimer");
        if (id == null) {
            afficherErreur("ID invalide");
            return;
        }

        Complement complement = complementService.obtenirComplement(id);
        if (complement == null) {
            afficherErreur("Complément introuvable");
            return;
        }

        afficherDetailsComplement(complement);

        System.out.println("\n⚠️  ATTENTION: Cette action est irréversible!");
        if (confirmer("Confirmez-vous la suppression définitive de ce complément ?")) {
            if (complementService.supprimerComplement(id)) {
                afficherSucces("Complément supprimé avec succès");
            } else {
                afficherErreur("Échec de la suppression");
            }
        }
    }
}