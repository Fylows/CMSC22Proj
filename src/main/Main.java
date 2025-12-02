/***********************************************************
	 *  A JavaFX-based application that implements a user management system (AMIS Style)
	 *  (Insert more here)
	 *
	 * @author Saplan, Perlas, Castaneda, Alquinto
	 * @created_date 2025-11-xx xx:xx
	 *
	 ***********************************************************/

package main;

import java.nio.file.Path;

import backend.CourseManager;
import backend.RegSystem;
import frontend.LoadingScreen;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
	@Override
    public void start(Stage stage) {
			stage.getIcons().add(new Image(getClass().getResourceAsStream("/resources/logo.png"))); // Uploading Kurasu Icon for app 
			stage.setTitle(" クラス | Kurasu"); // Sets the title to Kurasu
			LoadingScreen ld = new LoadingScreen(stage);
			ld.show(); // Show loading screen
	    }

		public static void main(String[] args) {
			CourseManager.loadFromCSV();
			RegSystem system = new RegSystem();
			launch(args); // Starts the JavaFX application
		}
	}