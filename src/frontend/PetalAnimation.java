package frontend;

import javafx.animation.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class PetalAnimation {
	public static void start(Pane layer, int maxPetals, double interval) {
		Timeline spawn = new Timeline( // Timeline that repeatedly spawns new petals
				new KeyFrame(Duration.seconds(interval), e -> { // Spawn petals every given "interval"
					if (layer.getChildren().size() < maxPetals) { // Only spawn new petals if we haven't reached the limit
						createFallingPetal(layer); // Create a new falling petal
					}
				})
		);
		
		spawn.setCycleCount(Animation.INDEFINITE); // Repeat spawning forever
		spawn.play(); // Start spawning petals
	}

	private static void createFallingPetal(Pane layer) {
		ImageView petal = new ImageView(new Image(PetalAnimation.class.getResourceAsStream("/resources/petal.png"))); 
		petal.setOpacity(0.8); // Slight transparency of the petal

		// Randomizer for the size
		double size = 20 + Math.random() * 40; // Size randomly between 20 and 60 px
		petal.setFitWidth(size); // Set width
		petal.setPreserveRatio(true); // Maintain true aspect ratio

		// Determine scene size
		double sceneWidth, sceneHeight;

		if (layer.getScene() != null) { // If scene exists, use actual dimensions
			sceneWidth = layer.getScene().getWidth();
			sceneHeight = layer.getScene().getHeight();
		} else { // If scene is null, use the given
			sceneWidth = 1200;
			sceneHeight = 800;
		}

		// Random starting position anywhere near the top
		double startX = Math.random() * sceneWidth; // Random horizontal start position
		double startY = -50 - Math.random() * 100; // Start slightly above the visible area
		
		// Apply to the x and y axes
		petal.setLayoutX(startX);
		petal.setLayoutY(startY);

		layer.getChildren().add(petal); // Add petal to the layer so it becomes visible

		double durationSeconds = 4 + Math.random() * 3; // Falling animation around 4 to 7 seconds
		TranslateTransition fall = new TranslateTransition(Duration.seconds(durationSeconds), petal); // Move the petal over this duration

		// Random diagonal fall across the full screen
		double endX = startX - (50 + Math.random() * (sceneWidth / 2)); // Move to the left while falling
		double endY = sceneHeight + 50 + Math.random() * 100; // End below the bottom of the screen
		
		// Movement relative to the starting position
		fall.setToX(endX - startX);
		fall.setToY(endY - startY);
		fall.setCycleCount(1);
		fall.setOnFinished(e -> layer.getChildren().remove(petal)); // Remove petal when finished
		fall.play(); // Start falling animation

		// Rotation animation
		RotateTransition rotate = new RotateTransition(Duration.seconds(durationSeconds), petal); 
		rotate.setByAngle(180 + Math.random() * 180); // Rotate 180–360 degrees
		rotate.setCycleCount(1); // Only one spin sequence 
		rotate.play(); // Start rotation

		// Scaling for depth effect (shrinks slightly as if falling away)
		ScaleTransition scale = new ScaleTransition(Duration.seconds(durationSeconds), petal);
		double endScale = 0.5 + Math.random() * 0.5;
		scale.setFromX(1);
		scale.setToX(endScale);
		scale.setFromY(1);
		scale.setToY(endScale);
		scale.setCycleCount(1); // Play once
		scale.play(); // Start scaling
	}
}

