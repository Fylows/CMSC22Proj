package application;

import java.util.ArrayList;

import backend.*;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.control.Button;
import javafx.scene.text.*;
import javafx.stage.Stage;

public class WelcomeScreen {
	private Stage stage; // Reference to main window (Stage)

	// Constructor
	public WelcomeScreen(Stage stage) {
		this.stage = stage;
	}

	// Running the screen
	public void show() {
		StackPane root = new StackPane(); // Added a StackPane for easier layouting

		// Layout for background
		Pane bgPane = new Pane(); 
		Image bg = new Image(getClass().getResourceAsStream("/resources/welcomebg.png"));
		BackgroundImage bgImg = new BackgroundImage(
				bg,
				BackgroundRepeat.NO_REPEAT, // No repeating vertically
                BackgroundRepeat.NO_REPEAT, // No repeating horizontally
                BackgroundPosition.CENTER, // Center the image
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
		);
		bgPane.setBackground(new Background(bgImg)); // Apply the image as background

		// Petal animation pane
		Pane petalPane = new Pane(); 
		PetalAnimation.start(petalPane, 50, 0.2); // Starts custom petal animation (50 max petals at 0.2 seconds spawn rate)

		// VBox for UI panel
		VBox ui = new VBox(15); 
		ui.setAlignment(Pos.CENTER_LEFT); // Align all items to the left side in the middle
		ui.setPadding(new Insets(0, 0, 0, 80)); // Left padding for consistent layout
		StackPane.setAlignment(ui, Pos.CENTER_LEFT); // Position the VBox on the left side

		// Font Styling for the texts
		Font japaneseFont = Font.loadFont(getClass().getResourceAsStream("/resources/Seibi Ohkido.otf"), 80); // Seibi Ohkido (Japanese)
		Font poppinsBold = Font.loadFont(getClass().getResourceAsStream("/resources/Poppins Bold.ttf"), 42); // Poppins (for sub headers)
		Font interItalic = Font.loadFont(getClass().getResourceAsStream("/resources/Inter Italic.ttf"), 22); // Inter (for content)
		Font interFont = Font.loadFont(getClass().getResourceAsStream("/resources/Inter.ttf"), 10); // Mini Inter

		Text line1 = new Text("クラス");
		line1.setFont(japaneseFont);

		Text line2 = new Text("Welcome to Kurasu!");
		line2.setFont(poppinsBold);

		Text line3 = new Text("Your academic journey blooms, one class at a time");
		line3.setFont(interItalic);

		Text line4 = new Text("Version 3.12.2");
		line4.setFont(interFont);
		
		// Buttons and styling
		Button signup = new Button("Sign-Up"); // Sign-up button
		Button login = new Button("Log-In"); // Log-in button
		styleButton(signup);
		styleButton(login);
		
		// Unedited
		ArrayList<Student> students = StudentManager.loadStudents();
		StudentManager manager = new StudentManager(students);

		// Sign Up button action
		signup.setOnAction(e -> {
		    SignUpScreen signUp = new SignUpScreen(stage, manager); // pass stage and manager
		    signUp.show(); // show modal
		});

		// Log In button action
		login.setOnAction(e -> {
		    LoginScreen loginScreen = new LoginScreen(stage, manager); // pass stage and manager
		    loginScreen.show(); // show modal
		});
		// End

		// Add a spacer to create visual separation before buttons
		Region spacer = new Region();
		spacer.setPrefHeight(20); // 20px empty space
        
		ui.getChildren().addAll(line1, line2, line3, line4, spacer, signup, login); // Add all UI elements into VBox in order
		root.getChildren().addAll(bgPane, petalPane, ui); // Add layers in order: background -> petals -> UI panel

		Scene scene = new Scene(root, 1200, 800);
		stage.setScene(scene); // Apply to stage
		stage.show(); // Show Welcome Screen
    }

	// For the button styling to avoid repetitiveness
	private void styleButton(Button b) {
		Font poppins = Font.loadFont(getClass().getResourceAsStream("/resources/Poppins-Bold.ttf"), 26); // Poppins Font

		// Base appearance
		b.setFont(poppins);
		b.setPrefWidth(200);
		b.setPrefHeight(45);
		
		// Default style
		b.setStyle(
			"-fx-background-color: #fb6f92;" + // Main color of the button (darker pink)
			"-fx-background-radius: 8;" +
			"-fx-text-fill: #ffe5ec;" +
			"-fx-font-size: 26px;" + // Font size 26
			"-fx-font-weight: bold;"
		);

		// Event Handlers
		// Hover effect
		b.setOnMouseEntered(e -> b.setStyle(
			"-fx-background-color: #ff85ac;" + // Lighter color when hovered
			"-fx-background-radius: 8;" +
			"-fx-text-fill: #ffe5ec;" +
			"-fx-font-size: 26px;" + // Font size 26
			"-fx-font-weight: bold;"
		));

		// Revert hover when mouse exits
		b.setOnMouseExited(e -> b.setStyle(
			"-fx-background-color: #fb6f92;" + // Revert back to original when not hovered anymore
			"-fx-background-radius: 8;" +
			"-fx-text-fill: #ffe5ec;" +
			"-fx-font-size: 26px;" + // Font size 26
			"-fx-font-weight: bold;"
		));
	}
}
