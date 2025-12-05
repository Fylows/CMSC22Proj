package frontend;

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
	private StudentManager manager;
	
	// Constructor
	@SuppressWarnings("exports")
	public WelcomeScreen(Stage stage) {
		this.stage = stage;
		this.manager = RegSystem.getStudentManager();
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
		Text line1 = new Text("クラス");
		line1.getStyleClass().add("jp-title");

		Text line2 = new Text("Welcome to Kurasu!");
		line2.getStyleClass().add("sub-title");

		Text line3 = new Text("Your academic journey blooms, one class at a time");
		line3.getStyleClass().add("tagline");

		Text line4 = new Text("Version 3.12.2");
		line4.getStyleClass().add("version");
		
		// Buttons and styling
		Button signup = new Button("Sign-Up"); // Sign-up button
		signup.getStyleClass().add("pink-button");
		
		Button login = new Button("Log-In"); // Log-in button
        login.getStyleClass().add("pink-button");
		

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

		// Add a spacer to create visual separation before buttons
		Region spacer = new Region();
		spacer.setPrefHeight(20); // 20px empty space
        
		ui.getChildren().addAll(line1, line2, line3, line4, spacer, signup, login); // Add all UI elements into VBox in order
		root.getChildren().addAll(bgPane, petalPane, ui); // Add layers in order: background -> petals -> UI panel

		Scene scene = new Scene(root, 1200, 800);
        scene.getStylesheets().add(getClass().getResource("/cssFiles/welcome.css").toExternalForm()); // Add CSS Styling
	
		stage.setScene(scene); // Apply to stage
		stage.show(); // Show Welcome Screen
    }
}
