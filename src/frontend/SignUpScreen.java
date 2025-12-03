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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.control.DatePicker;
import javafx.util.StringConverter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class SignUpScreen {
	private Stage popupStage; // The pop-up window that shows the sign-up UI
	private Stage ownerStage; // The actual WelcomeScreen
	private StudentManager studentManager; // Handles saving and validating sign-ups

	// Constructor
	public SignUpScreen(Stage ownerStage, StudentManager studentManager) {
		this.ownerStage = ownerStage; // Keep reference to the WelcomeScreen
		this.studentManager = studentManager;

		this.popupStage = new Stage();
		popupStage.initOwner(ownerStage);
		popupStage.initModality(Modality.APPLICATION_MODAL); // Prevents interaction with other windows
		popupStage.setResizable(false); // Not resizable
		popupStage.setTitle("Sign-Up");

		popupStage.getIcons().add(new Image(getClass().getResourceAsStream("/cssFiles/logo.png"))); // Setting the Kurasu Icon for pop-up
	}

	// Running the screen
	public void show() {
		// Layout for background
		BorderPane root = new BorderPane(); // Used a BorderPane since layout is much simpler
		root.setPadding(new Insets(25));
		root.getStyleClass().add("signup-root");

		// Semi-white box container
		VBox form = new VBox(15);
		form.getStyleClass().add("signup-form");

		// Font Styling for the texts
		Label title = new Label("Sign-Up");
		title.getStyleClass().add("signup-title");

		Label subtitle = new Label("Fields marked with * are required.");
		subtitle.getStyleClass().add("signup-subtitle");

		// Input Fields of the student
		TextField firstName = createField("First Name: *");
		TextField middleName = createField("Middle Name:");

		TextField lastName = createField("Last Name: *");
		TextField suffix = createField("Generational Suffix:");

		TextField email = createField("Email: *");

		DatePicker birthday = new DatePicker();
		birthday.setPromptText("Birthdate: *");
		birthday.getStyleClass().add("date-picker");
		
		// Custom date format
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		birthday.setConverter(new StringConverter<LocalDate>() {
			@Override
			public String toString(LocalDate date) {
				return date != null ? formatter.format(date) : "";
			}

			@Override
			public LocalDate fromString(String string) {
				if (string == null || string.trim().isEmpty()) return null;
				return LocalDate.parse(string, formatter);
			}
		});

		ComboBox<String> sexBox = new ComboBox<>();
		sexBox.getItems().addAll("Male", "Female", "Other");
		sexBox.setPromptText("Sex Assigned at Birth: *");
		sexBox.getStyleClass().add("combo-box");

		ComboBox<String> degreeBox = new ComboBox<>();
		degreeBox.getItems().addAll("BSCS", "MSCS", "MSIT", "PHD");
		degreeBox.setPromptText("Degree Program: *");
		degreeBox.getStyleClass().add("combo-box");

		PasswordField password = new PasswordField();
		password.setPromptText("Password: *");
		password.getStyleClass().add("password-field");

		PasswordField confirmPassword = new PasswordField();
		confirmPassword.setPromptText("Confirm Password: *");
		password.getStyleClass().add("password-field");

		// Prompt message for any errors in bright red
		Label msgLabel = new Label();
		msgLabel.getStyleClass().add("error-label");

		// Setting up the rows and layout
		HBox nameRow = new HBox(10, firstName, middleName);
		HBox lastRow = new HBox(10, lastName, suffix);
		HBox birthRow = new HBox(10, birthday, sexBox);
		HBox msgRow = new HBox(msgLabel);
		msgRow.setAlignment(Pos.CENTER);

		// Sign-Up button and styling
		Button signUpBtn = new Button("Sign-Up");
		signUpBtn.getStyleClass().add("signup-btn");
		
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

		// Log-in Hyperlink
		Hyperlink loginLink = new Hyperlink("Already have an account? Log-In");
		loginLink.getStyleClass().add("login-link");
		loginLink.setOnAction(e -> {
			popupStage.close(); // Close the Sign-up pop-up
			new LoginScreen(ownerStage, studentManager).show(); // Ensure that WelcomeScreen is the parent screen
		});

		// Centered layout box for the buttons and hyperlink
		VBox centerBox = new VBox(10, signUpBtn, loginLink);
		centerBox.setAlignment(Pos.CENTER);

		// Assembles everything into the form
		form.getChildren().addAll(title, subtitle, nameRow, lastRow, email, birthRow, degreeBox, password, confirmPassword, msgRow, centerBox);
		root.setCenter(form);

		Scene scene = new Scene(root, 600, 620);
        scene.getStylesheets().add(getClass().getResource("/resources/signup.css").toExternalForm()); // Add CSS Styling
		
		popupStage.setScene(scene); // Apply to stage
		popupStage.show(); // Show the pop-up screen
	}

	// Helper method to create styled TextFields
	private TextField createField(String prompt) {
		TextField tf = new TextField();
		tf.setPromptText(prompt);
		tf.getStyleClass().add("input-field");
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
