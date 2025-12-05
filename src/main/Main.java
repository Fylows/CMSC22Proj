/***********************************************************
	 *  A JavaFX-based application that implements a user management system for enrollment (similar to AMIS of UPLB)
	 *  This is designed to help students easily browse their courses from their institute and plan their enrollment for the upcoming semesters
	 *
	 * @author Ljiel Saplan, Mikhail Perlas, Alethea Castañeda, Carlos Alquinto
	 * @created_date 2025-11-11 11:11
	 *
	 ***********************************************************/

package main;

import backend.CourseManager;
import backend.RegSystem;
import frontend.LoadingScreen;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
	@SuppressWarnings("exports")
	@Override
    public void start(Stage stage) {
			stage.getIcons().add(new Image(getClass().getResourceAsStream("/resources/logo.png"))); // Uploading Kurasu Icon for app 
			stage.setTitle(" クラス | Kurasu"); // Sets the title to Kurasu
			LoadingScreen ld = new LoadingScreen(stage);
			ld.show(); // Show loading screen
	    }

		@SuppressWarnings("unused")
		public static void main(String[] args) {
			CourseManager.loadFromCSV();
			RegSystem system = new RegSystem();
			launch(args); // Starts the JavaFX application
		}
	}