package sn.brasilburger;

import java.util.List;
import java.util.Optional;

import sn.brasilburger.config.factory.EntityName;
import sn.brasilburger.config.factory.services.ServicesFactory;
import sn.brasilburger.entity.*;
import sn.brasilburger.services.*;
import sn.brasilburger.views.GesViews;

public class Main {
    private boolean success;
    private BurgerService bs = (BurgerService)ServicesFactory.createServices(EntityName.BURGER);
    private ComplementService cs = (ComplementService)ServicesFactory.createServices(EntityName.COMPLEMENT);
    private LivreurService ls = (LivreurService)ServicesFactory.createServices(EntityName.LIVREUR);
    private LoginService logs = (LoginService)ServicesFactory.createServices(EntityName.LOGIN);
    private MenuService ms = (MenuService)ServicesFactory.createServices(EntityName.MENU);
    private ZoneService zs = (ZoneService)ServicesFactory.createServices(EntityName.ZONE);
    private MenuComplementService mcs = (MenuComplementService)ServicesFactory.createServices(EntityName.MENU_COMPLEMENT);
    private ImageUploadService ius = (ImageUploadService)ServicesFactory.createServices(EntityName.IMAGE_UPLOAD);

    public static void main(String[] args) {
        Main app = new Main();
        app.gesAuth();
    }
    private void gesApp(){
        int choix;
        do{
            GesViews.pause(1200);
            GesViews.clearScreen();
            choix = GesViews.menuPrincipale();
            switch (choix) {
                case 1:
                    gesAjout();
                    break;
                case 2:
                    gesUpdate();
                    break;
                case 3:
                    gesArchive();
                    break;
                case 4:
                    gesListe();
                    break;
                case 5:
                    GesViews.afficherWarn("Déconnexion...");
                    return;
                default:
                    GesViews.afficherErreur("Choix indisponible");
                    break;
            }
        }while(choix!=5);
    }
    private void gesAjout(){
        int choix;
        do{
            GesViews.pause(1200);
            GesViews.clearScreen();
            choix = GesViews.menuAjout();
            switch (choix) {
                case 1:
                    GesViews.afficherTitre("Ajout d'un burger");
                    Burger b = new Burger();
                    b.setNom(GesViews.saisirString("Saisir le nom du burger: "));
                    b.setPrix(GesViews.saisirDouble("Saisir le prix du burger: "));
                    var imgBurger = GesViews.saisirString("Saisir le chemin de l'image");
                    try {
                        b.setImage(ius.uploadImage(imgBurger));
                    } catch (Exception e) {
                        GesViews.afficherErreur("Erreur lors de l'upload: " + e.getMessage());
                    }
                    success = bs.createBurger(b);
                    if (success) {
                        GesViews.afficherSuccess("Burger ajouté avec succès !");
                    } else {
                        GesViews.afficherErreur("Erreur lors de l'ajout du burger.");
                    }
                    break;
                case 2:
                    GesViews.afficherTitre("Ajout d'un Complement");
                    Complement c = new Complement();
                    c.setNom(GesViews.saisirString("Saisir le nom du complement: "));
                    c.setPrix(GesViews.saisirDouble("Saisir le prix du complement: "));
                    var imgCmpl = GesViews.saisirString("Saisir le chemin de l'image");
                    try {
                        c.setImage(ius.uploadImage(imgCmpl));
                    } catch (Exception e) {
                        GesViews.afficherErreur("Erreur lors de l'upload: " + e.getMessage());
                    }
                    int choixCat;
                    do {
                        System.out.println("=== Choix de la categorie ===");
                        System.out.println("1. Frites");
                        System.out.println("2. Boisson");
                        choixCat = GesViews.saisirInt("Faites votre choix: ");
                        if (choixCat == 1) {
                            c.setCategorie(CategorieComplement.FRITES);
                        } else if (choixCat == 2) {
                            c.setCategorie(CategorieComplement.BOISSON);
                        } else {
                            GesViews.afficherErreur("Choix incorrect");
                        }
                    } while (choixCat!=1 && choixCat!=2);
                    success = cs.createComplement(c);
                    if (success) {
                        GesViews.afficherSuccess("Complement ajouté avec succès !");
                    } else {
                        GesViews.afficherErreur("Erreur lors de l'ajout du complement.");
                    }
                    break;
                case 3:
                    GesViews.afficherTitre("Ajout d'un menu");
                    Menu m = new Menu();
                    m.setNom(GesViews.saisirString("Saisir le nom du menu: "));
                    var imgMenu = GesViews.saisirString("Saisir le chemin de l'image");
                    try {
                        m.setImage(ius.uploadImage(imgMenu));
                    } catch (Exception e) {
                        GesViews.afficherErreur("Erreur lors de l'upload: " + e.getMessage());
                    }
                    String s;
                    var montant = 0.0;
                    Optional<Burger> bu = Optional.empty();
                    do{
                        List<Burger> bur = bs.getAllBurger();
                        GesViews.afficher(bur,"Aucun burger trouvé");
                        int id = GesViews.saisirInt("Saisir l'id du burger:");
                        bu = bs.getBurgerById(id);
                        if(bu.isPresent()){
                            m.setBurger(bu.get());
                            montant += bu.get().getPrix();
                        }
                    }while(!bu.isPresent());
                    boolean complementAdded = false;
                    do{
                        List<Complement> cmpl = cs.getAllComplement();
                        GesViews.afficher(cmpl,"Aucun complement trouvé");
                        int id = GesViews.saisirInt("Saisir l'id du complement:");
                        Optional<Complement> cm = cs.getComplementById(id);
                        if(cm.isPresent()){
                            m.getComplements().add(cm.get());
                            montant += cm.get().getPrix();
                            complementAdded = true;
                        }
                        if(complementAdded){
                            do{
                                s = GesViews.saisirString("Voulez vous saisir un autre complement?[O/N]").toUpperCase();
                            }while(!s.equals("N") && !s.equals("O"));
                        } else {
                            s = "O";
                            GesViews.afficherErreur("Choix incorrect ! Veuillez sélectionner au moins un complément");
                        }
                    }while(!s.equals("N"));
                    m.setMontant(montant);
                    success = ms.createMenu(m);
                    if(success){
                        for (Complement cpl : m.getComplements()) {
                            MenuComplement menuC = new MenuComplement();
                            menuC.setComplement(cpl);
                            menuC.setMenu(m);
                            mcs.createMenuComplement(menuC);
                        }
                        GesViews.afficherSuccess("Menu ajouté avec succès !");
                    }else{
                        GesViews.afficherErreur("Erreur lors de l'ajout du menu.");
                    }
                    break;
                case 4:
                    GesViews.afficherTitre("Ajout d'une zone");
                    Zone z = new Zone();
                    z.setNom(GesViews.saisirString("Saisir le nom de la zone: "));
                    z.setPrixLivraison(GesViews.saisirDouble("Saisir le prix de livraison: "));
                    success = zs.createZone(z);
                    if (success) {
                        GesViews.afficherSuccess("Zone ajouté avec succès !");
                    } else {
                        GesViews.afficherErreur("Erreur lors de l'ajout du zone.");
                    }
                    break;
                case 5:
                    GesViews.afficherTitre("Ajout d'un livreur");
                    Livreur l = new Livreur();
                    l.setNomComplet(GesViews.saisirString("Saisir le nom et prenom du livreur: "));
                    l.setTelephone(GesViews.saisirTelephone("Saisir le telephone du livreur: "));
                    List<Zone> zones = zs.getAllZone();
                    GesViews.afficher(zones,"Aucun zone trouvé");
                    Optional<Zone> zone;
                    do {
                        zone = zs.getZoneById(GesViews.saisirInt("Saisir l'id de la zone:"));
                    } while (zone.isEmpty());
                    l.setZone(zone.get());
                    success = ls.createLivreur(l);
                    if (success) {
                        GesViews.afficherSuccess("Livreur ajouté avec succès !");
                    } else {
                        GesViews.afficherErreur("Erreur lors de l'ajout du livreur.");
                    }
                    break;
                case 6:
                    GesViews.afficherTitre("Ajout d'un gestionnaire");
                    User newUser = new User();
                    newUser.setNomComplet(GesViews.saisirString("Nom complet: "));
                    newUser.setTelephone(GesViews.saisirTelephone("Telephone: "));
                    newUser.setEmail(GesViews.saisirString("Email: "));
                    newUser.setPassword(GesViews.saisirString("Mot de passe: "));
                    newUser.setRole(RoleUser.GESTIONNAIRE);
                    success = logs.signup(newUser);
                    if (success) {
                        GesViews.afficherSuccess("Gestionnaire ajouté avec succès !");
                    } else {
                        GesViews.afficherErreur("Erreur lors de l'ajout du gestionnaire.");
                    }
                    break;
                case 7:
                    GesViews.afficherWarn("Retour au menu principale...");
                    break;
                default:
                    GesViews.afficherErreur("Choix indisponible");
                    break;
            }
        }while(choix!=7);
    }
    private void gesUpdate(){
        int choix;
        String prixStr;
        String nom;
        String img;
        do{
            GesViews.pause(1200);
            GesViews.clearScreen();
            choix = GesViews.menuUpdate();
            switch (choix) {
                case 1:
                    GesViews.afficherTitre("modification d'un burger");
                    List<Burger> burgers = bs.getAllBurger();
                    GesViews.afficher(burgers,"Aucun burger trouvé");
                    int idBurger = GesViews.saisirInt("Saisir l'id du burger: ");
                    Optional<Burger> burgerOpt = bs.getBurgerById(idBurger);
                    if (burgerOpt.isEmpty()) {
                        GesViews.afficherErreur("Aucun burger trouvé");
                        break;
                    }
                    Burger burger = burgerOpt.get();
                    nom = GesViews.saisirString(
                        "Nom actuel: " + burger.getNom() + ". Nouveau nom: "
                    );
                    burger.setNom(nom);
                    prixStr = GesViews.saisirString(
                        "Montant actuel: " + burger.getPrix() + ". Nouveau montant: "
                    );
                    try {
                        burger.setPrix(Double.parseDouble(prixStr));
                    } catch (NumberFormatException e) {
                        GesViews.afficherErreur("Montant invalide, valeur inchangée.");
                    }
                    img = GesViews.saisirString(
                        "Image actuelle: " + burger.getImage() + ". Nouveau chemin: "
                    );
                    try {
                        burger.setImage(ius.uploadImage(img));
                    } catch (Exception e) {
                        GesViews.afficherErreur("Erreur lors de l'upload: " + e.getMessage());
                    }
                    success = bs.updateBurger(burger);
                    if (success) {
                        GesViews.afficherSuccess("Burger modifié avec succès !");
                    } else {
                        GesViews.afficherErreur("Erreur lors de la modification du burger.");
                    }
                    break;
                case 2:
                    GesViews.afficherTitre("modification d'un complement");
                    List<Complement> complements = cs.getAllComplement();
                    GesViews.afficher(complements,"Aucun complement trouvé");
                    int idComplement = GesViews.saisirInt("Saisir l'id du complement: ");
                    Optional<Complement> compOpt = cs.getComplementById(idComplement);
                    if (compOpt.isEmpty()) {
                        GesViews.afficherErreur("Aucun complement trouvé");
                        break;
                    }
                    Complement comp = compOpt.get();
                    nom = GesViews.saisirString(
                        "Nom actuel: " + comp.getNom() + ". Nouveau nom: "
                    );
                    comp.setNom(nom);
                    prixStr = GesViews.saisirString(
                        "Montant actuel: " + comp.getPrix() + ". Nouveau montant: "
                    );
                    try {
                        comp.setPrix(Double.parseDouble(prixStr));
                    } catch (NumberFormatException e) {
                        GesViews.afficherErreur("Montant invalide, valeur inchangée.");
                    }
                    img = GesViews.saisirString(
                        "Image actuelle: " + comp.getImage() + ". Nouveau chemin: "
                    );
                    try {
                        comp.setImage(ius.uploadImage(img));
                    } catch (Exception e) {
                        GesViews.afficherErreur("Erreur lors de l'upload: " + e.getMessage());
                    }
                    success = cs.updateComplement(comp);
                    if (success) {
                        GesViews.afficherSuccess("Complement modifié avec succès !");
                    } else {
                        GesViews.afficherErreur("Erreur lors de la modification du complement.");
                    }
                    break;
                case 3:
                    GesViews.afficherTitre("modification d'un menu");
                    List<Menu> menus = ms.getAllMenu();
                    GesViews.afficher(menus,"Aucun menu trouvé");
                    int idMenu = GesViews.saisirInt("Saisir l'id du menu: ");
                    Optional<Menu> menuOpt = ms.getMenuById(idMenu);
                    if (menuOpt.isEmpty()) {
                        GesViews.afficherErreur("Aucun menu trouvé");
                        break;
                    }
                    Menu menu = menuOpt.get();
                    nom = GesViews.saisirString(
                        "Nom actuel: " + menu.getNom() + ". Nouveau nom: "
                    );
                    menu.setNom(nom);
                    img = GesViews.saisirString(
                        "Image actuelle: " + menu.getImage() + ". Nouveau chemin: "
                    );
                    try {
                        menu.setImage(ius.uploadImage(img));
                    } catch (Exception e) {
                        GesViews.afficherErreur("Erreur lors de l'upload: " + e.getMessage());
                    }
                    success = ms.updateMenu(menu);
                    if (success) {
                        GesViews.afficherSuccess("Menu modifié avec succès !");
                    } else {
                        GesViews.afficherErreur("Erreur lors de la modification du menu.");
                    }
                    break;
                case 4:
                    GesViews.afficherTitre("modification d'une zone");
                    List<Zone> zones = zs.getAllZone();
                    GesViews.afficher(zones,"Aucune zone trouvée");
                    int idZone = GesViews.saisirInt("Saisir l'id de la zone: ");
                    Optional<Zone> zoneOpt = zs.getZoneById(idZone);
                    if (zoneOpt.isEmpty()) {
                        GesViews.afficherErreur("Aucune zone trouvée");
                        break;
                    }
                    Zone zone = zoneOpt.get();
                    nom = GesViews.saisirString(
                        "Nom actuel: " + zone.getNom() + ". Nouveau nom: "
                    );
                    zone.setNom(nom);
                    prixStr = GesViews.saisirString(
                        "Montant actuel: " + zone.getPrixLivraison() + ". Nouveau montant: "
                    );
                    try {
                        zone.setPrixLivraison(Double.parseDouble(prixStr));
                    } catch (NumberFormatException e) {
                        GesViews.afficherErreur("Montant invalide, valeur inchangée.");
                    }
                    success = zs.updateZone(zone);
                    if (success) {
                        GesViews.afficherSuccess("Zone modifié avec succès !");
                    } else {
                        GesViews.afficherErreur("Erreur lors de la modification dde la zone.");
                    }
                    break;
                case 5:
                    GesViews.afficherTitre("modification d'un livreur");
                    List<Livreur> livreurs = ls.getAllLivreur();
                    GesViews.afficher(livreurs,"Aucun livreur trouvé");
                    int idLivreur = GesViews.saisirInt("Saisir l'id du livreur: ");
                    Optional<Livreur> livOpt = ls.getLivreurById(idLivreur);
                    if (livOpt.isEmpty()) {
                        GesViews.afficherErreur("Aucun livreur trouvé");
                        break;
                    }
                    Livreur liv = livOpt.get();
                    nom = GesViews.saisirString(
                        "Nom actuel: " + liv.getNomComplet() + ". Nouveau nom: "
                    );
                    liv.setNomComplet(nom);
                    String tel = GesViews.saisirTelephone(
                        "Telephone actuel: " + liv.getTelephone() + ". Nouveau telephone: "
                    );
                    liv.setTelephone(tel);
                    List<Zone> zones_ = zs.getAllZone();
                    GesViews.afficher(zones_, "Aucune zone trouvée");
                    String zoneInput = GesViews.saisirString(
                        "ID de la zone actuelle: " + liv.getZone().getId() + ". Nouveau ID : "
                    );
                    try {
                        idZone = Integer.parseInt(zoneInput);
                        Optional<Zone> zOpt = zs.getZoneById(idZone);
                        if (zOpt.isPresent()) {
                            liv.setZone(zOpt.get());
                        } else {
                            GesViews.afficherErreur("Zone invalide, valeur inchangée.");
                        }
                    } catch (NumberFormatException e) {
                        GesViews.afficherErreur("ID invalide, valeur inchangée.");
                    }
                    success = ls.updateLivreur(liv);
                    if (success) {
                        GesViews.afficherSuccess("Livreur modifié avec succès !");
                    } else {
                        GesViews.afficherErreur("Erreur lors de la modification du livreur.");
                    }
                    break;
                case 6:
                    GesViews.afficherTitre("modification d'un gestionnaire");
                    List<User> ges = logs.getAllGes();
                    GesViews.afficher(ges,"Aucun gestionnaire trouvé");
                    String mail = GesViews.saisirMail("Saisir le mail du gestionnaire: ");
                    Optional<User> userOpt = logs.getGesByMail(mail);
                    if (userOpt.isEmpty()) {
                        GesViews.afficherErreur("Aucun gestionnaire trouvé");
                        break;
                    }
                    User user = userOpt.get();
                    nom = GesViews.saisirString(
                        "Nom actuel: " + user.getNomComplet() + ". Nouveau nom: "
                    );
                    user.setNomComplet(nom);
                    String telephone = GesViews.saisirTelephone(
                        "Telephone actuel: " + user.getTelephone() + ". Nouveau telephone: "
                    );
                    user.setTelephone(telephone);
                    String email = GesViews.saisirMail(
                        "Email actuel: " + user.getEmail() + ". Nouvel email: "
                    );
                    user.setEmail(email);
                    String password = GesViews.saisirString(
                        "Mot de passe actuel: " + user.getPassword() + ". Nouveau mot de passe: "
                    );
                    user.setPassword(password);
                    success = logs.updateGes(user);
                    if (success) {
                        GesViews.afficherSuccess("Gestionnaire modifié avec succès !");
                    } else {
                        GesViews.afficherErreur("Erreur lors de la modification du gestionnaire.");
                    }
                    break;
                case 7:
                    GesViews.afficherWarn("Retour au menu principale...");
                    break;
                default:
                    GesViews.afficherErreur("Choix indisponible");
                    break;
            }
        }while(choix!=7);
    }
    private void gesArchive(){
        int choix;
        do{
            GesViews.pause(1200);
            GesViews.clearScreen();
            choix = GesViews.menuArchive();
            switch (choix) {
                case 1:
                    GesViews.afficherTitre("archivage d'un burger");
                    List<Burger> burgers = bs.getAllBurger();
                    GesViews.afficher(burgers,"Aucun burger trouvé");
                    int idBurger = GesViews.saisirInt("Saisir l'id du burger: ");
                    Optional<Burger> burger = bs.getBurgerById(idBurger);
                    if (burger.isEmpty()) {
                        GesViews.afficherErreur("Aucun burger trouvé");
                        break;
                    }
                    success = bs.archiveBurger(burger.get());
                    if (success) {
                        GesViews.afficherSuccess("Burger archivé avec succès !");
                    } else {
                        GesViews.afficherErreur("Erreur lors de l'archivage du burger.");
                    }
                    break;
                case 2:
                    GesViews.afficherTitre("archivage d'un complement");
                    List<Complement> complements = cs.getAllComplement();
                    GesViews.afficher(complements,"Aucun complement trouvé");
                    int idComplement = GesViews.saisirInt("Saisir l'id du complement: ");
                    Optional<Complement> comp = cs.getComplementById(idComplement);
                    if (comp.isEmpty()) {
                        GesViews.afficherErreur("Aucun complement trouvé");
                        break;
                    }
                    success = cs.archiveComplement(comp.get());
                    if (success) {
                        GesViews.afficherSuccess("Complement archivé avec succès !");
                    } else {
                        GesViews.afficherErreur("Erreur lors de l'archivage du complement.");
                    }
                    break;
                case 3:
                    GesViews.afficherTitre("archivage d'un menu");
                    List<Menu> menus = ms.getAllMenu();
                    GesViews.afficher(menus,"Aucun menu trouvé");
                    int idMenu = GesViews.saisirInt("Saisir l'id du menu: ");
                    Optional<Menu> menu = ms.getMenuById(idMenu);
                    if (menu.isEmpty()) {
                        GesViews.afficherErreur("Aucun menu trouvé");
                        break;
                    }
                    success = ms.archiveMenu(menu.get());
                    if (success) {
                        GesViews.afficherSuccess("Burger archivé avec succès !");
                    } else {
                        GesViews.afficherErreur("Erreur lors de l'archivage du burger.");
                    }
                    break;
                case 4:
                    GesViews.afficherTitre("archivage d'une zone");
                    List<Zone> zones = zs.getAllZone();
                    GesViews.afficher(zones,"Aucune zone trouvée"); 
                    int idZone = GesViews.saisirInt("Saisir l'id de la zone: ");
                    Optional<Zone> zone = zs.getZoneById(idZone);
                    if (zone.isEmpty()) {
                        GesViews.afficherErreur("Aucune zone trouvée");
                        break;
                    }
                    success = zs.archiveZone(zone.get());
                    if (success) {
                        GesViews.afficherSuccess("Zone archivé avec succès !");
                    } else {
                        GesViews.afficherErreur("Erreur lors de l'archivage du zone.");
                    }
                    break;
                case 5:
                    GesViews.afficherTitre("archivage d'un livreur");
                    List<Livreur> livreurs = ls.getAllLivreur();
                    GesViews.afficher(livreurs,"Aucun livreur trouvé");
                    int idLivreur = GesViews.saisirInt("Saisir l'id du livreur: ");
                    Optional<Livreur> liv = ls.getLivreurById(idLivreur);
                    if (liv.isEmpty()) {
                        GesViews.afficherErreur("Aucun livreur trouvé");
                        break;
                    }
                    success = ls.archiveLivreur(liv.get());
                    if (success) {
                        GesViews.afficherSuccess("Livreur archivé avec succès !");
                    } else {
                        GesViews.afficherErreur("Erreur lors de l'archivage du livreur.");
                    }
                    break;
                case 6:
                    GesViews.afficherTitre("archivage d'un gestionnaire");
                    List<User> ges = logs.getAllGes();
                    GesViews.afficher(ges,"Aucun gestionnaire trouvé");
                    String mail = GesViews.saisirMail("Saisir le mail du gestionnaire: ");
                    Optional<User> userOpt = logs.getGesByMail(mail);
                    if (userOpt.isEmpty()) {
                        GesViews.afficherErreur("Aucun gestionnaire trouvé");
                        break;
                    }
                    User user = userOpt.get();
                    success = logs.archiveGes(user);
                    if (success) {
                        GesViews.afficherSuccess("Gestionnaire archivé avec succès !");
                    } else {
                        GesViews.afficherErreur("Erreur lors de l'archivage du gestionnaire.");
                    }
                    break;
                case 7:
                    GesViews.afficherWarn("Retour au menu principale...");
                    break;
                default:
                    GesViews.afficherErreur("Choix indisponible");
                    break;
            }
        }while(choix!=7);
    }
    private void gesListe(){
        int choix;
        do{
            GesViews.pause(1200);
            GesViews.clearScreen();
            choix = GesViews.menuLister();
            switch (choix) {
                case 1:
                    GesViews.afficherTitre("liste des burgers");
                    List<Burger> burgers = bs.getAllBurger();
                    GesViews.afficher(burgers,"Aucun burger trouvé");
                    GesViews.waitForKey();
                    break;
                case 2:
                    GesViews.afficherTitre("liste des complements");
                    List<Complement> complements = cs.getAllComplement();
                    GesViews.afficher(complements,"Aucun complement trouvé");
                    GesViews.waitForKey();
                    break;
                case 3:
                    GesViews.afficherTitre("liste des menus");
                    List<Menu> menus = ms.getAllMenu();
                    if (menus.isEmpty()) {
                        GesViews.afficherErreur("Aucun menu trouvé");
                    }else{
                        for (Menu menu : menus) {
                        System.out.println(menu);
                        Optional<Burger> burger = bs.getBurgerByMenu(menu);
                        burger.ifPresent(menu::setBurger);
                        System.out.println(menu.getBurger());
                        List<Complement> complementsMenu = cs.getComplementsByMenu(menu.getId());
                        menu.setComplements(complementsMenu);
                        complementsMenu.forEach(System.out::println);
                        System.out.println("--------------------------------------------------------------------");
                    }
                    GesViews.waitForKey();
                    }
                    break;
                case 4:
                    GesViews.afficherTitre("liste des zones");
                    List<Zone> zones = zs.getAllZone();
                    GesViews.afficher(zones,"Aucun zone trouvé");
                    GesViews.waitForKey();
                    break;
                case 5:
                    GesViews.afficherTitre("liste des livreurs");
                    List<Livreur> livreurs = ls.getAllLivreur();
                    //GesViews.afficher(livreurs,"Aucun livreur trouvé");
                    if (livreurs.isEmpty()) {
                        GesViews.afficherErreur("Aucun livreur trouvé");
                    }else{
                        for (Livreur livreur : livreurs) {
                        System.out.println(livreur);
                        Optional<Zone> zone = zs.getZoneById(livreur.getZone().getId());
                        zone.ifPresent(livreur::setZone);
                        System.out.println("->Zone: " + livreur.getZone());
                    }
                    }
                    GesViews.waitForKey();
                    break;
                case 6: 
                    GesViews.afficherTitre("liste des gestionnaires");
                    List<User> gestionnaires = logs.getAllGes();
                    GesViews.afficher(gestionnaires,"Aucun gestionnaire trouvé");
                    GesViews.waitForKey();
                    break;
                case 7:
                    GesViews.afficherWarn("Retour au menu principale...");
                    break;
                default:
                    GesViews.afficherErreur("Choix indisponible");
                    break;
            }
        }while(choix!=7);
    }
    private void gesAuth(){
        int choix;
        do {
            GesViews.pause(1200);
            GesViews.clearScreen();
            choix = GesViews.menuAuthentification();
            switch (choix) {
                case 1:
                    User user = Login();
                    if (user != null) {
                        gesApp();
                    }
                    break;
                case 2:
                    GesViews.afficherWarn("Aurevoir...");
                    break;
                default:
                    GesViews.afficherErreur("Choix indisponible");
                    break;
            }
        } while (choix != 3);
    }
    private User Login(){
        GesViews.afficherTitre("Connexion");
        String email = GesViews.saisirString("Email: ");
        String password = GesViews.saisirString("Mot de passe: ");
        Optional<User> user = logs.login(email, password);
        if (user.isPresent() && user.get().getRole() == RoleUser.GESTIONNAIRE) {
            var s = "Authentification réussie. Bienvenue " + user.get().getNomComplet();
            GesViews.afficherSuccess(s);
            return user.get();
        } else {
            GesViews.afficherErreur("Échec de l'authentification. Veuillez vérifier vos identifiants.");
            return null;
        }
    }
}
