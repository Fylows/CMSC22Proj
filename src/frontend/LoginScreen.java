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

public class LoginScreen {
	private Stage popupStage; // The pop-up window that shows the sign-up UI
	private Stage ownerStage; // The actual WelcomeScreen
	private StudentManager manager; // Handles validating log-in

	// Constructor
	public LoginScreen(Stage ownerStage, StudentManager manager) {
		this.ownerStage = ownerStage; // Keep reference to the WelcomeScreen
		this.manager = manager;

		this.popupStage = new Stage();
		popupStage.initOwner(ownerStage);
		popupStage.initModality(Modality.APPLICATION_MODAL); // Prevents interaction with other windows
		popupStage.setResizable(false); // Not resizable
		popupStage.setTitle("Log-In"); 

		popupStage.getIcons().add(new Image(getClass().getResourceAsStream("/resources/logo.png"))); // Setting the Kurasu Icon for pop-up
	}

	// Running the screen
	public void show() {
		// Layout for background
		BorderPane root = new BorderPane(); // Used a BorderPane since layout is much simpler
		root.setPadding(new Insets(25));
		root.getStyleClass().add("login-root");

		// Semi-white box container
		VBox form = new VBox(15);
		form.getStyleClass().add("login-form"); // RGB + opacity for transparent look

		// Font Styling for the texts
		Label title = new Label("Log-In");
		title.getStyleClass().add("login-title");

		Label subtitle = new Label("Please enter your credentials.");
		subtitle.getStyleClass().add("login-subtitle");

		// Input Fields of the student
		TextField email = new TextField();
		email.setPromptText("Email");
		email.getStyleClass().add("login-field");

		PasswordField password = new PasswordField();
		password.setPromptText("Password");
		password.getStyleClass().add("login-field");
		
		// Prompt message for any errors in bright red
		Label msgLabel = new Label();
		msgLabel.getStyleClass().add("error-label");

		HBox msgRow = new HBox(msgLabel);
		msgRow.setAlignment(Pos.CENTER);

		// Log-in button and styling
		Button loginBtn = new Button("Log-In");
		loginBtn.getStyleClass().add("login-btn");

		// Events when log-in button is clicked
		loginBtn.setOnAction(e -> {
			String result = manager.login(email.getText(), password.getText()); // Call StudentManager log-in logic

			// If log-in logic returns an error message, show respective message
			if (!result.equals("SUCCESS")) {
				showTemporaryMessage(msgLabel, result, 3);
				return;
			}
			
			Student loggedIn = manager.getStudentByEmail(email.getText()); 
			
			popupStage.close(); // Close the current pop-up
			ownerStage.close(); // Also close the WelcomeScreen
		    new ContentArea(loggedIn, manager).show(); // To pass student info to other screens
		
		});
		
		// Sign-Up Hyperlink
		Hyperlink signUpLink = new Hyperlink("Don't have an account? Sign-Up");
		signUpLink.getStyleClass().add("signup-link");
		signUpLink.setOnAction(e -> {
			popupStage.close(); // Close the Log-in pop-up
			new SignUpScreen(ownerStage, manager).show(); // Ensure that WelcomeScreen is the parent screen
		});

		// Centered layout box for the buttons and hyperlink
		VBox centerBox = new VBox(10);
		centerBox.setAlignment(Pos.CENTER);
		centerBox.getChildren().addAll(loginBtn, signUpLink);

		// Assembles everything into the form
		form.getChildren().addAll(title, subtitle, email, password, msgRow, centerBox);
		root.setCenter(form);

		Scene scene = new Scene(root, 500, 400);
		scene.getStylesheets().add(getClass().getResource("/resources/login.css").toExternalForm()); // Add CSS Styling
		
		popupStage.setScene(scene); // Apply to stage
		popupStage.show(); // Show the pop-up screen
	}
	
	// Helper method that allows to show error message for the given seconds
	private void showTemporaryMessage(Label label, String text, int seconds) {
		label.setText(text); // Set the label to show the provided error or info message
		PauseTransition pause = new PauseTransition(Duration.seconds(seconds));
		pause.setOnFinished(ev -> label.setText("")); // After the pause finishes, clear the label text
		pause.play(); // Start the timer
	}
}
