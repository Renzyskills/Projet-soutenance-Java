import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class GestionUtilisateurs {
    // Connexion à MySQL
    private static final String URL = "jdbc:mysql://localhost:3306/gestion_utilisateurs?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";  
    private static final String PASSWORD = "";  

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Vérifier si le pilote JDBC est bien chargé
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("✅ Pilote JDBC chargé avec succès !");
        } catch (ClassNotFoundException e) {
            System.out.println("❌ Erreur : Pilote JDBC non trouvé !");
            e.printStackTrace();
            return; 
        }

        while (true) {
            System.out.println("\n--- Menu Gestion des Utilisateurs ---");
            System.out.println("1. Ajouter un utilisateur");
            System.out.println("2. Modifier un utilisateur");
            System.out.println("3. Supprimer un utilisateur");
            System.out.println("4. Lister les utilisateurs");
            System.out.println("5. Quitter");
            System.out.print("Choisissez une option : ");

            int choix = scanner.nextInt();
            scanner.nextLine(); 

            switch (choix) {
                case 1:
                    ajouterUtilisateur(scanner);
                    break;
                case 2:
                    modifierUtilisateur(scanner);
                    break;
                case 3:
                    supprimerUtilisateur(scanner);
                    break;
                case 4:
                    listerUtilisateurs();
                    break;
                case 5:
                    System.out.println("Fermeture du programme...");
                    return;
                default:
                    System.out.println("Option invalide, veuillez réessayer.");
            }
        }
    }

    private static void ajouterUtilisateur(Scanner scanner) {
        System.out.print("Entrez le nom de l'utilisateur : ");
        String nom = scanner.nextLine();
        System.out.print("Entrez l'email de l'utilisateur : ");
        String email = scanner.nextLine();

        String sql = "INSERT INTO utilisateurs (nom, email) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nom);
            stmt.setString(2, email);
            stmt.executeUpdate();
            System.out.println("✅ Utilisateur ajouté avec succès !");
            
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de l'ajout de l'utilisateur !");
            e.printStackTrace();
        }
    }

    private static void modifierUtilisateur(Scanner scanner) {
        System.out.print("Entrez l'ID de l'utilisateur à modifier : ");
        int id = scanner.nextInt();
        scanner.nextLine(); 

        System.out.print("Entrez le nouveau nom : ");
        String nom = scanner.nextLine();
        System.out.print("Entrez le nouvel email : ");
        String email = scanner.nextLine();

        String sql = "UPDATE utilisateurs SET nom = ?, email = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nom);
            stmt.setString(2, email);
            stmt.setInt(3, id);
            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("✅ Utilisateur modifié avec succès !");
            } else {
                System.out.println("⚠️ Aucun utilisateur trouvé avec cet ID.");
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la modification !");
            e.printStackTrace();
        }
    }

    private static void supprimerUtilisateur(Scanner scanner) {
        System.out.print("Entrez l'ID de l'utilisateur à supprimer : ");
        int id = scanner.nextInt();

        String sql = "DELETE FROM utilisateurs WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            int rowsDeleted = stmt.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("✅ Utilisateur supprimé avec succès !");
            } else {
                System.out.println("⚠️ Aucun utilisateur trouvé avec cet ID.");
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la suppression !");
            e.printStackTrace();
        }
    }

    private static void listerUtilisateurs() {
        String sql = "SELECT * FROM utilisateurs";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            System.out.println("\n📜 Liste des utilisateurs :");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") + ", Nom: " + rs.getString("nom") + ", Email: " + rs.getString("email"));
            }
            
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de l'affichage des utilisateurs !");
            e.printStackTrace();
        }
    }
}
