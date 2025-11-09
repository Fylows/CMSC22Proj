package application;
	
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


public class Main extends Application {

	@Override
    public void start(Stage stage) {
		// The root we add to scene, we add everything here
		Group b = new Group();
		
		// by default canvas is 0x0
		Canvas canvas = new Canvas(1920,1080);
		GraphicsContext gc = canvas.getGraphicsContext2D();
		// getClass() gets our class, then gets the Resource named "cat", then we make it an External form
		Image im1 = new Image(getClass().getResource("cat.jpg").toExternalForm());
		
		
		gc.drawImage(im1, 0, 0);
		b.getChildren().add(canvas);
		
		// adding root to scene after 
		Scene sc = new Scene(b, 400, 400);
		stage.setScene(sc);
		stage.setTitle("YOO");
		stage.show();
				

    }

    public static void main(String[] args) {
        launch(args);
    }
}
