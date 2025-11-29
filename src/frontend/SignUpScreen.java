package frontend;

import backend.Student;
import backend.StudentManager;
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SignUpScreen {
	private Stage popupStage; // The pop-up window that shows the sign-up UI
	private Stage ownerStage; // The actual WelcomeScreen
	private StudentManager studentManager; // Handles saving and validating sign-ups

	// Fixed widths and height
	private final double FULL_WIDTH = 300;
	private final double HALF_WIDTH = 300;
	private final double HEIGHT = 40;

	// Constructor
	public SignUpScreen(Stage ownerStage, StudentManager studentManager) {
		this.ownerStage = ownerStage; // Keep reference to the WelcomeScreen
		this.studentManager = studentManager;

		this.popupStage = new Stage();
		popupStage.initOwner(ownerStage);
		popupStage.initModality(Modality.APPLICATION_MODAL); // Prevents interaction with other windows
		popupStage.setResizable(false); // Not resizable
		popupStage.setTitle("Sign-Up");

		popupStage.getIcons().add(new Image(getClass().getResourceAsStream("/resources/logo.png"))); // Setting the Kurasu Icon for pop-up
	}

	// Running the screen
	public void show() {
		// Layout for background
		BorderPane root = new BorderPane(); // Used a BorderPane since layout is much simpler
		root.setPadding(new Insets(25));
		root.setStyle("-fx-background-color: #ffc2d1;");

		// Semi-white box container
		VBox form = new VBox(15);
		form.setAlignment(Pos.TOP_LEFT);
		form.setPadding(new Insets(25));
		form.setStyle("-fx-background-color: rgba(255,255,255,0.9); -fx-background-radius: 20;"); // RGB + opacity for transparent look

		// Font Styling for the texts
		Font poppinsBold = Font.loadFont(getClass().getResourceAsStream("/resources/Poppins Bold.ttf"), 32);
		Font inter = Font.loadFont(getClass().getResourceAsStream("/resources/Inter.ttf"), 14);
		Font interItalic = Font.loadFont(getClass().getResourceAsStream("/resources/Inter Italic.ttf"), 14);

		Label title = new Label("Sign-Up");
		title.setFont(poppinsBold);

		Label subtitle = new Label("Fields marked with * are required.");
		subtitle.setFont(interItalic);
		subtitle.setStyle("-fx-text-fill: #444444;");

		// Input Fields of the student
		TextField firstName = createField("First Name: *", inter, HALF_WIDTH, HEIGHT);
		TextField middleName = createField("Middle Name:", inter, HALF_WIDTH, HEIGHT);

		TextField lastName = createField("Last Name: *", inter, HALF_WIDTH, HEIGHT);
		TextField suffix = createField("Generational Suffix:", inter, HALF_WIDTH, HEIGHT);

		TextField email = createField("Email: *", inter, FULL_WIDTH, HEIGHT);

		DatePicker birthday = new DatePicker();
		birthday.setPromptText("Birthdate: *");
		birthday.setPrefWidth(HALF_WIDTH);
		birthday.setPrefHeight(HEIGHT);
		birthday.setStyle("-fx-font-family: 'Inter';" + "-fx-font-size: 14px;");

		ComboBox<String> sexBox = new ComboBox<>();
		sexBox.getItems().addAll("Male", "Female", "Other");
		sexBox.setPromptText("Sex Assigned at Birth: *");
		sexBox.setPrefWidth(HALF_WIDTH);
		sexBox.setPrefHeight(HEIGHT);
		sexBox.setStyle("-fx-font-family: 'Inter';" + "-fx-font-size: 14px;");

		ComboBox<String> degreeBox = new ComboBox<>();
		degreeBox.getItems().addAll("BSCS", "MSCS", "MSIT", "PHD");
		degreeBox.setPromptText("Degree Program: *");
		degreeBox.setPrefWidth(FULL_WIDTH);
		degreeBox.setPrefHeight(HEIGHT);
		degreeBox.setStyle("-fx-font-family: 'Inter';" + "-fx-font-size: 14px;");

		PasswordField password = new PasswordField();
		password.setPromptText("Password: *");
		password.setFont(inter);
		password.setPrefWidth(FULL_WIDTH);
		password.setPrefHeight(HEIGHT);

		PasswordField confirmPassword = new PasswordField();
		confirmPassword.setPromptText("Confirm Password: *");
		confirmPassword.setFont(inter);
		confirmPassword.setPrefWidth(FULL_WIDTH);
		confirmPassword.setPrefHeight(HEIGHT);

		// Prompt message for any errors in bright red
		Label msgLabel = new Label();
		msgLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");

		// Setting up the rows and layout
		HBox nameRow = new HBox(10, firstName, middleName);
		nameRow.setAlignment(Pos.TOP_LEFT);

		HBox lastRow = new HBox(10, lastName, suffix);
		lastRow.setAlignment(Pos.TOP_LEFT);

		HBox birthRow = new HBox(10, birthday, sexBox);
		birthRow.setAlignment(Pos.TOP_LEFT);

		HBox msgRow = new HBox(msgLabel);
		msgRow.setAlignment(Pos.CENTER);

		// Sign-Up button and styling
		Button signUpBtn = new Button("Sign-Up");
		signUpBtn.setPrefWidth(FULL_WIDTH);

		signUpBtn.setStyle(
				"-fx-background-color: #fb6f92;" + // Main color of the button (darker pink)
				"-fx-background-radius: 8;" +
				"-fx-text-fill: #ffe5ec;" +
				"-fx-font-size: 20px;" + // Font size 20
				"-fx-font-weight: bold;"
        );

		// Event Handlers (when hovered and exit hovering)
		signUpBtn.setOnMouseEntered(e ->
			signUpBtn.setStyle(
				"-fx-background-color: #ff85ac;" + // Lighter color when hovered
				"-fx-background-radius: 8;" +
				"-fx-text-fill: #ffe5ec;" +
				"-fx-font-size: 20px;" + // Font size 20
				"-fx-font-weight: bold;"
			)
		);

		signUpBtn.setOnMouseExited(e ->
			signUpBtn.setStyle(
				"-fx-background-color: #fb6f92;" + // Revert back to original when not hovered anymore
				"-fx-background-radius: 8;" +
				"-fx-text-fill: #ffe5ec;" +
				"-fx-font-size: 20px;" + // Font size 20
				"-fx-font-weight: bold;"
			)
		);

		// Log-in Hyperlink
		Hyperlink loginLink = new Hyperlink("Already have an account? Log-In");
		loginLink.setFont(inter);
		loginLink.setOnAction(e -> {
			popupStage.close(); // Close the Sign-up pop-up
			new LoginScreen(ownerStage, studentManager).show(); // Ensure that WelcomeScreen is the parent screen
		});

		// Centered layout box for the buttons and hyperlink
		VBox centerBox = new VBox(10);
		centerBox.setAlignment(Pos.CENTER);
		centerBox.getChildren().addAll(signUpBtn, loginLink);

		// Assembles everything into the form
		form.getChildren().addAll(title, subtitle, nameRow, lastRow, email, birthRow, degreeBox, password, confirmPassword, msgRow, centerBox);
		root.setCenter(form);

		Scene scene = new Scene(root, 600, 620);
		popupStage.setScene(scene); // Apply to stage
		popupStage.show(); // Show the pop-up screen

		// Events when sign-up button is clicked
		signUpBtn.setOnAction(e -> {
			// Checks if passwords match
			if (!password.getText().equals(confirmPassword.getText())) {
				showTemporaryMessage(msgLabel, "Passwords do not match!", 3);
				return;
			}

			// Call StudentManager sign-up logic
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

			// If sign-up logic returns an error message, show respective message
			if (!result.equals("SUCCESS")) {
				showTemporaryMessage(msgLabel, result, 3);
				return;
			}

			Student student = studentManager.getStudentByEmail(email.getText().trim()); // Retrieve newly created student object

			popupStage.close(); // Close the current pop-up
			new CourseSelectionScreen(ownerStage, student).show(); // Show continuation of the pop-up for course selection
		});
	}

	// Helper method to create styled TextFields
	private TextField createField(String prompt, Font font, double width, double height) {
		TextField tf = new TextField();
		tf.setPromptText(prompt);
		tf.setFont(font);
		tf.setPrefWidth(width);
		tf.setPrefHeight(height);   // <- unify height
		return tf;
	}
	
	// Helper method that allows to show error message for the given seconds
	private void showTemporaryMessage(Label label, String text, int seconds) {
		label.setText(text); // Set the label to show the provided error or info message
		PauseTransition pause = new PauseTransition(Duration.seconds(seconds));
		pause.setOnFinished(ev -> label.setText("")); // After the pause finishes, clear the label text
		pause.play(); // Start the timer
	}
}
