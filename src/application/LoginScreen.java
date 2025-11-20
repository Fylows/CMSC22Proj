package application;

import backend.StudentManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class LoginScreen {

    private Stage stage;
    private StudentManager manager;

    public LoginScreen(Stage owner, StudentManager manager) {
        this.stage = new Stage();
        this.manager = manager;

        // Make it modal so user cannot close main window
        stage.initOwner(owner);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Log In");
    }

    public void show() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);

        // ---------- Title ----------
        Label title = new Label("Log In");
        title.setFont(Font.font("Poppins", FontWeight.BOLD, 36));
        title.setTextFill(Color.web("#fb6f92"));

        // ---------- Input Fields ----------
        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        // ---------- Login Button ----------
        Button loginBtn = new Button("Log In");
        loginBtn.setStyle("-fx-background-color: #fb6f92; -fx-text-fill: white; -fx-font-size: 18px;");
        loginBtn.setOnMouseEntered(e -> loginBtn.setStyle("-fx-background-color: #ff85ac; -fx-text-fill: white; -fx-font-size: 18px;"));
        loginBtn.setOnMouseExited(e -> loginBtn.setStyle("-fx-background-color: #fb6f92; -fx-text-fill: white; -fx-font-size: 18px;"));

        // ---------- Message Label ----------
        Label msgLabel = new Label();
        msgLabel.setTextFill(Color.RED);

        // ---------- Action ----------
        loginBtn.setOnAction(e -> {
            String email = emailField.getText().trim();
            String password = passwordField.getText().trim();
            String result = manager.login(email, password);

            if (result.equals("SUCCESS")) {
                msgLabel.setTextFill(Color.GREEN);
                msgLabel.setText("Login successful!");
                // TODO: Navigate to dashboard or main app
                stage.close();
            } else {
                msgLabel.setTextFill(Color.RED);
                msgLabel.setText(result);
            }
        });

        // ---------- Hyperlink to Sign Up ----------
        Hyperlink signUpLink = new Hyperlink("Don't have an account? Sign Up");
        signUpLink.setOnAction(e -> {
            stage.close();
            // Open signup modal
            new SignUpScreen(stage, manager).show();
        });

        // ---------- Layout ----------
        root.getChildren().addAll(title, emailField, passwordField, loginBtn, msgLabel, signUpLink);
        root.setPrefWidth(400);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.showAndWait();
    }
}
