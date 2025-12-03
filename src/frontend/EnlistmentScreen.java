package frontend;

import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import backend.CourseManager;
import backend.OfferedCourse;
import backend.RegSystem;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class EnlistmentScreen extends VBox {
	
	public EnlistmentScreen() {
		setSpacing(20);
		setPadding(new Insets(20));
		setStyle("-fx-background-color: white;");

        		
        // ---------- CALENDAR ----------
		
//		GridPane calendar = createCalendar();
		
		GridPane calendar = new GridPane();
		calendar.setPadding(new Insets(20));
		for (int row = 0; row < 24; row++) {
		        for (int col = 0; col < 6; col++) {
		        // Example: a rectangle in each cell
				Rectangle cell = new Rectangle(80, 30); // width, height
				cell.setFill(Color.WHITE);
				cell.setStroke(Color.BLACK);
				
				StackPane cellPane = new StackPane();
		        cellPane.getChildren().add(cell);  // Rectangle is at bottom
		        calendar.add(cellPane, col, row);
	        }
		}
		 
        // ---------- ACTIVE ENLISTMENTS ----------
        Button b = new Button("Active Enlistment");
        OfferedCourse oc = RegSystem.getAllCourses().get(0);
        b.setOnAction(e -> RegSystem.fillTime(calendar, oc));
        
        // ---------- COURSE SEARCH ----------
        Button c = new Button("Course Search");
        c.setOnAction(e -> RegSystem.resetTime(calendar, oc));
        // Add all to layout
        getChildren().addAll(calendar,b,c);
	}
	
	public GridPane createCalendar() {
		GridPane calendar = new GridPane();
		calendar.setPadding(new Insets(20));
		 for (int row = 0; row < 24; row++) {
		        for (int col = 0; col < 6; col++) {
		        // Example: a rectangle in each cell
				Rectangle cell = new Rectangle(80, 30); // width, height
				cell.setFill(Color.WHITE);
				cell.setStroke(Color.BLACK);
				
				// Optional: put a label inside
				Label label = new Label("(" + row + "," + col + ")");
		        GridPane cellPane = new GridPane();
		        cellPane.getChildren().addAll(cell, label);
		
		        calendar.add(cellPane, col, row);
		    }
		}
		 
		HBox warnings = new HBox();
		warnings.setPadding(new Insets(20));
		Label lab = new Label("Warnings");
		warnings.getChildren().add(lab);
		
		HBox calendarAndWarnings = new HBox(calendar, warnings);
		
		return calendar;
	}
}
