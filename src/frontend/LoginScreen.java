package frontend;

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

public class LoginScreen {
	private Stage popupStage; // The pop-up window that shows the sign-up UI
	private Stage ownerStage; // The actual WelcomeScreen
	private StudentManager manager; // Handles validating log-in

	// Fixed widths and height
	private final double FULL_WIDTH = 300;
	private final double HEIGHT = 40;

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

		Label title = new Label("Log-In");
		title.setFont(poppinsBold);

		Label subtitle = new Label("Please enter your credentials.");
		subtitle.setFont(interItalic);
		subtitle.setStyle("-fx-text-fill: #444444;");

		// Input Fields of the student
		TextField email = new TextField();
		email.setPromptText("Email");
		email.setFont(inter);
		email.setPrefWidth(FULL_WIDTH);
		email.setPrefHeight(HEIGHT);

		PasswordField password = new PasswordField();
		password.setPromptText("Password");
		password.setFont(inter);
		password.setPrefWidth(FULL_WIDTH);
		password.setPrefHeight(HEIGHT);

		// Prompt message for any errors in bright red
		Label msgLabel = new Label();
		msgLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");

		HBox msgRow = new HBox(msgLabel);
		msgRow.setAlignment(Pos.CENTER);

		// Log-in button and styling
		Button loginBtn = new Button("Log-In");
		loginBtn.setPrefWidth(FULL_WIDTH);

		loginBtn.setStyle(
				"-fx-background-color: #fb6f92;" + // Main color of the button (darker pink)
				"-fx-background-radius: 8;" +
				"-fx-text-fill: #ffe5ec;" +
				"-fx-font-size: 20px;" + // Font size 20
				"-fx-font-weight: bold;"
        );

		// Event Handlers (when hovered and exit hovering)
		loginBtn.setOnMouseEntered(e ->
			loginBtn.setStyle(
				"-fx-background-color: #ff85ac;" + // Lighter color when hovered
				"-fx-background-radius: 8;" +
				"-fx-text-fill: #ffe5ec;" +
				"-fx-font-size: 20px;" + // Font size 20
				"-fx-font-weight: bold;"
			)
		);

		loginBtn.setOnMouseExited(e ->
			loginBtn.setStyle(
				"-fx-background-color: #fb6f92;" + // Revert back to original when not hovered anymore
				"-fx-background-radius: 8;" +
				"-fx-text-fill: #ffe5ec;" +
				"-fx-font-size: 20px;" + // Font size 20
				"-fx-font-weight: bold;"
			)
		);

		// Sign-Up Hyperlink
		Hyperlink signUpLink = new Hyperlink("Don't have an account? Sign-Up");
		signUpLink.setFont(inter);
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
		popupStage.setScene(scene); // Apply to stage
		popupStage.show(); // Show the pop-up screen

		// Events when log-in button is clicked
		loginBtn.setOnAction(e -> {
			String result = manager.login(email.getText(), password.getText()); // Call StudentManager log-in logic

			// If log-in logic returns an error message, show respective message
			if (!result.equals("SUCCESS")) {
				showTemporaryMessage(msgLabel, result, 3);
				return;
			}

			popupStage.close(); // Close the current pop-up
			ownerStage.close(); // Also close the WelcomeScreen
			new ContentArea().show(); // Open the Content
		});
	}
	
	// Helper method that allows to show error message for the given seconds
	private void showTemporaryMessage(Label label, String text, int seconds) {
		label.setText(text); // Set the label to show the provided error or info message
		PauseTransition pause = new PauseTransition(Duration.seconds(seconds));
		pause.setOnFinished(ev -> label.setText("")); // After the pause finishes, clear the label text
		pause.play(); // Start the timer
	}
}
