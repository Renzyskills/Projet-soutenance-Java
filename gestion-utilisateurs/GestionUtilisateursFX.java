import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.sql.*;

public class GestionUtilisateursFX extends Application {
    // Connexion à la base de données MySQL
    private static final String URL = "jdbc:mysql://localhost:3306/gestion_utilisateurs?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";  
    private static final String PASSWORD = ""; 

    private TextField nomField = new TextField();
    private TextField emailField = new TextField();
    private TextArea affichage = new TextArea();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Gestion des Utilisateurs");

        // Définition du fond d'écran avec une image
        BackgroundImage background = new BackgroundImage(
            new Image("file:images/mario-and-luigi.png", 600, 600, false, true),
            BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, 
            BackgroundPosition.CENTER, new BackgroundSize(600, 600, false, false, false, false)
        );

        // Champs de saisie
        nomField.setPromptText("Nom");
        emailField.setPromptText("Email");

        // Zone de texte pour afficher les utilisateurs
        affichage.setEditable(false);
        affichage.setPrefHeight(200);

        // Création des boutons
        Button ajouterBtn = new Button("Ajouter");
        Button afficherBtn = new Button("Afficher");
        Button supprimerBtn = new Button("Supprimer");

        // Actions des boutons
        ajouterBtn.setOnAction(e -> ajouterUtilisateur());
        afficherBtn.setOnAction(e -> afficherUtilisateurs());
        supprimerBtn.setOnAction(e -> supprimerUtilisateur());

        // Mise en page
        VBox layout = new VBox(10, nomField, emailField, ajouterBtn, afficherBtn, supprimerBtn, affichage);
        layout.setAlignment(Pos.CENTER);
        layout.setBackground(new Background(background));

        // Création de la scène
        Scene scene = new Scene(layout, 600, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void ajouterUtilisateur() {
        String nom = nomField.getText();
        String email = emailField.getText();

        if (nom.isEmpty() || email.isEmpty()) {
            affichage.setText("⚠️ Veuillez remplir tous les champs !");
            return;
        }
        else{
            nom.char 53 {
                affichage.setText();
                return;
        } 

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO utilisateurs (nom, email) VALUES (?, ?)")) {
            
            stmt.setString(1, nom);
            stmt.setString(2, email);
            stmt.executeUpdate();
            affichage.setText("✅ Utilisateur ajouté avec succès !");
        } catch (SQLException e) {
            affichage.setText("❌ Erreur lors de l'ajout !");
            e.printStackTrace();
        }
    }

    private void afficherUtilisateurs() {
        StringBuilder result = new StringBuilder("📜 Liste des utilisateurs :\n");

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM utilisateurs")) {

            while (rs.next()) {
                result.append("ID: ").append(rs.getInt("id"))
                      .append(" | Nom: ").append(rs.getString("nom"))
                      .append(" | Email: ").append(rs.getString("email"))
                      .append("\n");
            }
            affichage.setText(result.toString());
        } catch (SQLException e) {
            affichage.setText("❌ Erreur lors de l'affichage !");
            e.printStackTrace();
        }
    }

    private void supprimerUtilisateur() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Suppression d'un utilisateur");
        dialog.setHeaderText("Entrez l'ID de l'utilisateur à supprimer :");
        dialog.setContentText("ID:");

        dialog.showAndWait().ifPresent(id -> {
            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                 PreparedStatement stmt = conn.prepareStatement("DELETE FROM utilisateurs WHERE id = ?")) {
                
                stmt.setInt(1, Integer.parseInt(id));
                int rowsDeleted = stmt.executeUpdate();

                if (rowsDeleted > 0) {
                    affichage.setText("✅ Utilisateur supprimé !");
                } else {
                    affichage.setText("⚠️ Aucun utilisateur trouvé avec cet ID.");
                }
            } catch (SQLException e) {
                affichage.setText("❌ Erreur lors de la suppression !");
                e.printStackTrace();
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
