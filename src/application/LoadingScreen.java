package application;

import javafx.animation.*;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LoadingScreen {
	private Stage stage; // Reference to main window (Stage)

	// Constructor
	public LoadingScreen(Stage stage) {
		this.stage = stage;
	}

	// Running the screen
	public void show() {
    	// Color gradient for the background
		Stop[] stops = new Stop[] { // Defines where the gradient color stops
				new Stop(0, Color.web("#ffe5ec")), // Start at light pink
				new Stop(1, Color.web("#ffc2d1")) // End at dark pink
		};

		// Create the background with said colors
		Background bg = new Background(
				new BackgroundFill(
						new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, stops), // Creates a vertical gradient using colors from above
						CornerRadii.EMPTY, // No rounded corners
						Insets.EMPTY // No padding
						)
		);

		StackPane root = new StackPane(); // Root layout that stacks elements
		root.setBackground(bg); // Apply gradient background to root

		// Petal animation pane
		Pane petalPane = new Pane(); 
		PetalAnimation.start(petalPane, 50, 0.2); // Starts custom petal animation (50 max petals at 0.2 seconds spawn rate)

		// Logo
		ImageView logo = new ImageView(new Image(getClass().getResourceAsStream("/resources/logo.png")));
		logo.setFitWidth(200); // Set the logo width 
		logo.setPreserveRatio(true); // Maintain true aspect ratio

		// Progress bar
		ProgressBar bar = new ProgressBar(0); // Creates progress bar starting at 0%
		bar.setPrefWidth(280); // Sets preferred width of the bar
		bar.setStyle("-fx-accent: #fb6f92;"); // Sets bar color to a pink tone

		VBox center = new VBox(50, logo, bar); // Vertical layout with the logo above progress bar on 50px spacing
		center.setAlignment(Pos.CENTER); // Center elements inside the VBox
		
		root.getChildren().addAll(petalPane, center); // Add petal pane and UI elements to root

		Scene scene = new Scene(root, 1200, 800); // Create a scene sized 1200x800 containing the root pane
		stage.setScene(scene); // Attach the scene to the window
		stage.show(); // Show the window

		simulateLoading(bar, root); // Begin simulating progress bar movement
    }

	// Method for the loading bar "loading progress"
	private void simulateLoading(ProgressBar bar, StackPane root) {
		Timeline timeline = new Timeline(); // Timeline used for gradually updating progress bar
		// Increment every 0.05 seconds
		for (int i = 0; i <= 100; i++) { // Loop from 0% to 100%
			double progress = i / 100.0; // Progress fraction (0.0 → 1.0)
			
			// Create keyframe for this progress step
			KeyFrame kf = new KeyFrame(Duration.seconds(i * 0.03), e -> bar.setProgress(progress)); // Add every 0.03 seconds
			timeline.getKeyFrames().add(kf); // Add keyframe to the timeline
		}

		// When progress reaches 100%
		timeline.setOnFinished(e -> {
			PauseTransition pause = new PauseTransition(Duration.seconds(0.5)); // Short pause before transition
			pause.setOnFinished(ev -> fadeOut(root)); // After pause, fade out the loading screen
			pause.play(); // Start the pause
		});
		
		timeline.play(); // Start progress animation
	}

	// Fade out transition to the Welcome Screen
	private void fadeOut(StackPane root) {
		FadeTransition fade = new FadeTransition(Duration.seconds(1), root);
		fade.setFromValue(1); // Start fully visible
		fade.setToValue(0); // End completely invisible

		fade.setOnFinished(e -> new WelcomeScreen(stage).show()); // After fade, show next screen
		fade.play(); // Start the fade-out animation
    }
}
