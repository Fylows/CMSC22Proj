package application;

import backend.Student;
import backend.StudentManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SignUpScreen {

    private Stage popupStage;
    private Stage ownerStage;  // <-- the actual welcome screen
    private StudentManager studentManager;

    // Fixed widths
    private final double FULL_WIDTH = 300;
    private final double HALF_WIDTH = 145;

    public SignUpScreen(Stage ownerStage, StudentManager studentManager) {
        this.ownerStage = ownerStage;       // keep reference to WelcomeScreen
        this.popupStage = new Stage();
        this.studentManager = studentManager;

        popupStage.initOwner(ownerStage);  // still modal
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setResizable(false);
        popupStage.setTitle("Sign Up");

        // Icon
        popupStage.getIcons().add(
            new Image(getClass().getResourceAsStream("/resources/logo.png"))
        );
    }


    public void show() {

        // ROOT LAYER
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(25));
        root.setStyle("-fx-background-color: #ffc2d1;");

        // FORM CONTAINER
        VBox form = new VBox(15);
        form.setAlignment(Pos.TOP_LEFT);       // <-- LEFT-ALIGNED
        form.setPadding(new Insets(25));
        form.setStyle("-fx-background-color: rgba(255,255,255,0.9); -fx-background-radius: 20;");

        // FONTS
        Font poppinsBold = Font.loadFont(getClass().getResourceAsStream("/resources/Poppins Bold.ttf"), 32);
        Font inter = Font.loadFont(getClass().getResourceAsStream("/resources/Inter.ttf"), 14);
        Font interItalic = Font.loadFont(getClass().getResourceAsStream("/resources/Inter Italic.ttf"), 14);

        // TITLE + SUBTITLE
        Label title = new Label("Sign-Up");
        title.setFont(poppinsBold);
        title.setStyle("-fx-text-fill: #000000;");

        Label subtitle = new Label("Fields marked with * are required.");
        subtitle.setFont(interItalic);
        subtitle.setStyle("-fx-text-fill: #444444;");

        // INPUT FIELDS
        TextField firstName = createField("First Name: *", inter, HALF_WIDTH);
        TextField middleName = createField("Middle Name", inter, HALF_WIDTH);

        TextField lastName = createField("Last Name: *", inter, HALF_WIDTH);
        TextField suffix = createField("Suffix", inter, HALF_WIDTH);

        TextField email = createField("Email: *", inter, FULL_WIDTH);

        DatePicker birthday = new DatePicker();
        birthday.setPromptText("Birthday");
        birthday.setPrefWidth(HALF_WIDTH);
        birthday.setStyle("-fx-font-family: 'Inter';");

        ComboBox<String> sexBox = new ComboBox<>();
        sexBox.getItems().addAll("Male", "Female", "Other");
        sexBox.setPromptText("Sex Assigned at Birth");
        sexBox.setPrefWidth(HALF_WIDTH);
        sexBox.setStyle("-fx-font-family: 'Inter';");

        ComboBox<String> degreeBox = new ComboBox<>();
        degreeBox.getItems().addAll("BSCS", "MSCS", "MSIT", "PHD");
        degreeBox.setPromptText("Degree Program: *");
        degreeBox.setPrefWidth(FULL_WIDTH);
        degreeBox.setStyle("-fx-font-family: 'Inter';");

        PasswordField password = new PasswordField();
        password.setPromptText("Password: *");
        password.setPrefWidth(FULL_WIDTH);
        password.setStyle("-fx-font-family: 'Inter';");

        PasswordField confirmPassword = new PasswordField();
        confirmPassword.setPromptText("Confirm Password: *");
        confirmPassword.setPrefWidth(FULL_WIDTH);
        confirmPassword.setStyle("-fx-font-family: 'Inter';");

        Label msgLabel = new Label();
        msgLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");

        // ROWS (LEFT-ALIGNED)
        HBox nameRow = new HBox(10, firstName, middleName);
        nameRow.setAlignment(Pos.TOP_LEFT);

        HBox lastRow = new HBox(10, lastName, suffix);
        lastRow.setAlignment(Pos.TOP_LEFT);

        HBox birthRow = new HBox(10, birthday, sexBox);
        birthRow.setAlignment(Pos.TOP_LEFT);

        // SIGN UP BUTTON
        Button signUpBtn = new Button("Sign Up");
        signUpBtn.setPrefWidth(FULL_WIDTH);
        signUpBtn.setStyle(
                "-fx-background-color: #fb6f92;" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-font-size: 16px;" +
                "-fx-background-radius: 12;"
        );

        signUpBtn.setOnMouseEntered(e ->
                signUpBtn.setStyle("-fx-background-color: #ff85ac; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px; -fx-background-radius: 12;")
        );
        signUpBtn.setOnMouseExited(e ->
                signUpBtn.setStyle("-fx-background-color: #fb6f92; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px; -fx-background-radius: 12;")
        );

        // LOGIN HYPERLINK
        Hyperlink loginLink = new Hyperlink("Already have an account? Log in instead");
        loginLink.setStyle("-fx-font-family: 'Inter'; -fx-font-size: 13px;");
        loginLink.setOnAction(e -> {
            popupStage.close();  // close the sign-up popup
            new LoginScreen(ownerStage, studentManager).show(); // login knows the real welcome screen
        });


        // CENTER ONLY BUTTON + HYPERLINK
        VBox centerBox = new VBox(10);
        centerBox.setAlignment(Pos.CENTER);     // <-- CENTERED
        centerBox.getChildren().addAll(signUpBtn, loginLink);

        // ASSEMBLE EVERYTHING
        form.getChildren().addAll(
                title, subtitle,
                nameRow,
                lastRow,
                email,
                birthRow,
                degreeBox,
                password,
                confirmPassword,
                msgLabel,
                centerBox     // <-- ONLY THIS PART CENTERED
        );

        root.setCenter(form);

        Scene scene = new Scene(root, 600, 620);
        popupStage.setScene(scene);
        popupStage.show();

        // SIGN UP ACTION
        signUpBtn.setOnAction(e -> {

            msgLabel.setText("");

            if (!password.getText().equals(confirmPassword.getText())) {
                msgLabel.setText("Passwords do not match!");
                return;
            }

            String result = studentManager.signUp(
                    firstName.getText(),
                    middleName.getText(),
                    lastName.getText(),
                    suffix.getText(),
                    email.getText(),
                    birthday.getValue() == null ? "" : birthday.getValue().toString(),
                    sexBox.getValue() == null ? "" : sexBox.getValue(),
                    password.getText(),
                    degreeBox.getValue() == null ? "" : degreeBox.getValue()
            );

            if (!result.equals("SUCCESS")) {
                msgLabel.setText(result);
                return;
            }

            Student student = studentManager.getStudentByEmail(email.getText().trim());

            popupStage.close();
            new CourseSelectionScreen(ownerStage, student).show();
        });
    }

    // Helper — Creates styled TextFields
    private TextField createField(String prompt, Font font, double width) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.setFont(font);
        tf.setPrefWidth(width);
        return tf;
    }
}
