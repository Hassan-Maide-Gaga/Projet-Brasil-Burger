package sn.brasilburger.views;

import java.util.List;
import java.util.Scanner;

public class GesViews {
    private static Scanner scanner = new Scanner(System.in);
    private GesViews(){
    }
    public static int menuAuthentification(){
        afficherTitre("Authentification");
        System.out.println("1. Se connecter");
        System.out.println("2. Quitter");
        System.out.println("Faites votre choix: ");
        int choix = scanner.nextInt();
        scanner.nextLine();
        return choix;
    }
    public static int menuPrincipale(){
        afficherTitre("Bienvenue dans Brasil Burger");
        System.out.println("1. Ajouter une ressource");
        System.out.println("2. Modifier une ressource");
        System.out.println("3. Archiver une ressource");
        System.out.println("4. Lister les ressources");
        System.out.println("5. Se deconnnecter");
        System.out.println("Faites votre choix: ");
        int choix = scanner.nextInt();
        scanner.nextLine();
        return choix;
    }
    public static int menuAjout(){
        afficherTitre("Ajouter une ressource");
        System.out.println("1. Ajouter un Burger");
        System.out.println("2. Ajouter un Complement");
        System.out.println("3. Ajouter un Menu");
        System.out.println("4. Ajouter une Zone");
        System.out.println("5. Ajouter un Livreur");
        System.out.println("6. Ajouter un Gestionnaire");
        System.out.println("7. Retour");
        System.out.println("Faites votre choix: ");
        int choix = scanner.nextInt();
        scanner.nextLine();
        return choix;
    }
    public static int menuUpdate(){
        afficherTitre("Modifier une ressource");
        System.out.println("1. Modifier un Burger");
        System.out.println("2. Modifier un Complement");
        System.out.println("3. Modifier un Menu");
        System.out.println("4. Modifier une Zone");
        System.out.println("5. Modifier un Livreur");
        System.out.println("6. Modifier un Gestionnaire");
        System.out.println("7. Retour");
        System.out.println("Faites votre choix: ");
        int choix = scanner.nextInt();
        scanner.nextLine();
        return choix;
    }
    public static int menuArchive(){
        afficherTitre("Archiver une ressource");
        System.out.println("1. Archiver un Burger");
        System.out.println("2. Archiver un Complement");
        System.out.println("3. Archiver un Menu");
        System.out.println("4. Archiver une Zone");
        System.out.println("5. Archiver un Livreur");
        System.out.println("6. Archiver un Gestionnaire");
        System.out.println("7. Retour");
        int choix = scanner.nextInt();
        scanner.nextLine();
        return choix;
    }
    public static int menuLister(){
        afficherTitre("Lister une ressource");  
        System.out.println("1. Lister les Burgers");
        System.out.println("2. Lister les Complements");
        System.out.println("3. Lister les Menus");
        System.out.println("4. Lister les Zones");
        System.out.println("5. Lister les Livreurs");
        System.out.println("6. Lister les Gestionnaire");
        System.out.println("7. Retour");
        int choix = scanner.nextInt();
        scanner.nextLine();
        return choix;
    }
    public static String saisirString(String msg){
        String champ;
        do {
            System.out.println(msg);
            champ = scanner.nextLine();
            if (champ.isEmpty()) {
                afficherErreur("Champ obligatoire!!");
            }
        } while (champ.isEmpty());
        return champ;
    }
    public static int saisirInt(String msg){
        int champ;
        do {
            System.out.println(msg);
            champ = scanner.nextInt();
            scanner.nextLine();
            if (champ < 0) {
                afficherErreur("Le Champ doit etre positif!!");
            }
        } while (champ < 0);
        return champ;
    }
    public static double saisirDouble(String msg){
        double champ;
        do {
            System.out.println(msg);
            champ = scanner.nextInt();
            scanner.nextLine();
            if (champ < 0) {
                afficherErreur("Le Champ doit etre positif!!");
            }
        } while (champ < 0);
        return champ;
    }
    public static String saisirTelephone(String msg){
        String champ;
        String regex = "^(77|78|76|75|70|71)[0-9]{7}$";
        do {
            System.out.println(msg);
            champ = scanner.nextLine().trim();
            if (!champ.matches(regex)) {
                afficherErreur("Le telephone doit contenir 9 chiffres et commencer par 77, 78, 76, 75, 70 ou 71.");
            }
            if(champ.isEmpty()){
                afficherErreur("Champ obligatoire!!");
            }
        } while (champ.isEmpty() || !champ.matches(regex));
        return champ;
    }
    public static String saisirMail(String msg){
        String champ;
        String regex = "^[A-Za-z0-9_.-]+@[a-z.-]+\\.[a-z]{2,6}$";
        do {
            System.out.println(msg);
            champ = scanner.nextLine().trim();
            if (!champ.matches(regex)) {
                afficherErreur("L'email doit etre valide (Exemple@abc.sn).");
            }
            if(champ.isEmpty()){
                afficherErreur("Champ obligatoire!!");
            }
        } while (champ.isEmpty() || !champ.matches(regex));
        return champ;
    }
    public static <T> void afficher(List<T> list,String msg){
        if (list.isEmpty()) {
            afficherErreur(msg);
            return;
        }
        list.forEach(System.out::println);
    }
    public static void afficherErreur(String msg){
        System.out.println(Color.RED + msg +  Color.RESET);
        pause(5000);
    }
    public static void afficherSuccess(String msg){
        System.out.println(Color.GREEN + msg + Color.RESET);
    }
    public static void afficherTitre(String msg){
        System.out.println(Color.BLUE + "=== " + msg.toUpperCase() + " ===" + Color.RESET);
    }
    public static void afficherWarn(String msg){
        System.out.println(Color.YELLOW + msg + Color.RESET);
    }
    public static void clearScreen() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public static void pause(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    public static void waitForKey() {
    System.out.println("Appuyez sur une touche pour continuer...");
    try {
        System.in.read();
    } catch (Exception e) {
        afficherErreur(e.getMessage());
    }
}

}
