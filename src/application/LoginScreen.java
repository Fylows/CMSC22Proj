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

    private Stage popupStage;      // login popup window
    private Stage ownerStage;      // reference to WelcomeScreen
    private StudentManager manager;

    public LoginScreen(Stage ownerStage, StudentManager manager) {
        this.ownerStage = ownerStage; 
        this.manager = manager;

        popupStage = new Stage();
        popupStage.initOwner(ownerStage);
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setResizable(false);

        popupStage.setTitle("Log In");
    }

    public void show() {

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #ffc2d1;");
        root.setPadding(new Insets(25));

        VBox box = new VBox(18);
        box.setAlignment(Pos.CENTER);

        // ---------- Title ----------
        Label title = new Label("Log In");
        title.setFont(Font.font("Poppins", FontWeight.BOLD, 36));
        title.setTextFill(Color.web("#fb6f92"));

        // ---------- Fields ----------
        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        emailField.setFont(Font.font("Inter", 14));
        emailField.setPrefWidth(320);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setFont(Font.font("Inter", 14));
        passwordField.setPrefWidth(320);

        // ---------- Button ----------
        Button loginBtn = new Button("Log In");
        loginBtn.setPrefWidth(320);
        loginBtn.setStyle("""
                -fx-background-color: #fb6f92;
                -fx-text-fill: white;
                -fx-font-size: 16px;
                -fx-font-family: 'Poppins';
                -fx-font-weight: bold;
                -fx-background-radius: 10;
            """);

        loginBtn.setOnMouseEntered(e -> loginBtn.setStyle("""
                -fx-background-color: #ff85ac;
                -fx-text-fill: white;
                -fx-font-size: 16px;
                -fx-font-family: 'Poppins';
                -fx-font-weight: bold;
                -fx-background-radius: 10;
            """));

        loginBtn.setOnMouseExited(e -> loginBtn.setStyle("""
                -fx-background-color: #fb6f92;
                -fx-text-fill: white;
                -fx-font-size: 16px;
                -fx-font-family: 'Poppins';
                -fx-font-weight: bold;
                -fx-background-radius: 10;
            """));

        // ---------- Status Label ----------
        Label msgLabel = new Label();
        msgLabel.setFont(Font.font("Inter", 14));
        msgLabel.setTextFill(Color.RED);

        // ---------- Hyperlink to Sign Up ----------
        Hyperlink signUpLink = new Hyperlink("Don't have an account? Sign Up");
        signUpLink.setFont(Font.font("Inter", 13));
        signUpLink.setOnAction(e -> {
            popupStage.close();      // close login popup
            new SignUpScreen(ownerStage, manager).show(); // reopen SignUp
        });

        // ---------- Login Logic ----------
        loginBtn.setOnAction(e -> {
            String email = emailField.getText().trim();
            String password = passwordField.getText().trim();

            String result = manager.login(email, password);

            if (result.equals("SUCCESS")) {
                msgLabel.setTextFill(Color.GREEN);
                msgLabel.setText("Login successful!");

                popupStage.close();      // close login popup
                ownerStage.close();      // close welcome screen

                // open dashboard
                new DashboardScreen().show();
            } else {
                msgLabel.setTextFill(Color.RED);
                msgLabel.setText(result);
            }
        });

        box.getChildren().addAll(
                title,
                emailField,
                passwordField,
                loginBtn,
                msgLabel,
                signUpLink
        );

        root.setCenter(box);

        Scene scene = new Scene(root, 420, 420);
        popupStage.setScene(scene);
        popupStage.showAndWait();
    }
}
