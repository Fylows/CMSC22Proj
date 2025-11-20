/***********************************************************
	 *  A JavaFX-based application that implements a user management system (AMIS Style)
	 *  (Insert more here)
	 *
	 * @author Saplan, Perlas, Castaneda, Alquinto
	 * @created_date 2025-11-xx xx:xx
	 *
	 ***********************************************************/

package application;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
	@Override
    public void start(Stage stage) {
			stage.getIcons().add(new Image(getClass().getResourceAsStream("/resources/logo.png"))); // Uploading Kurasu Icon for app 
			stage.setTitle("❀ クラス | Kurasu"); // Sets the title to Kurasu
			
			new LoadingScreen(stage).show(); // Show loading screen
	    }

		public static void main(String[] args) {
			launch(args); // Starts the JavaFX application
		}
	}