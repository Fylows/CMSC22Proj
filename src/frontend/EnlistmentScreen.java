package frontend;

import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

import java.util.ArrayList;


import backend.CourseManager;
import backend.OfferedCourse;
import backend.RegSystem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class EnlistmentScreen extends VBox {
	
	public EnlistmentScreen() {
		setSpacing(20);
		setPadding(new Insets(20));
		setStyle("-fx-background-color: white;");

        		
        // ---------- CALENDAR AND ERROR SCREEN ----------
		
		
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
		
		HBox warnings = new HBox();
		warnings.setPadding(new Insets(20));
		Label lab = new Label("Warnings");
		warnings.getChildren().add(lab);
		
		HBox calendarAndWarnings = new HBox(calendar, warnings);
		 
        // ---------- ACTIVE ENLISTMENTS ----------
        Button b = new Button("Active Enlistment");
        OfferedCourse oc = RegSystem.getAllCourses().get(0);
        b.setOnAction(e -> RegSystem.fillTime(calendar, oc));
        
        // ---------- COURSE SEARCH ----------
        Button c = new Button("Course Search");
        HBox courseSearch = createCourseSearch(RegSystem.getAllCourses());
        c.setOnAction(e -> RegSystem.resetTime(calendar, oc));
        // Add all to layout
        getChildren().addAll(calendarAndWarnings,b,c, courseSearch);
	}
	
	
	private HBox createCourseSearch(ArrayList<OfferedCourse> allOfferedCourses) {
		System.out.println(allOfferedCourses.size());
		// CREATE A TABLE
    	TableView<OfferedCourse> table = new TableView<>();
    	table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        TableColumn<OfferedCourse, String> codeCol   = new TableColumn<>("Code");
        TableColumn<OfferedCourse, String> classDetailsCol      = new TableColumn<>("Class Details");
        
        codeCol.setCellValueFactory(new PropertyValueFactory<>("courseCode"));
        classDetailsCol.setCellValueFactory(new PropertyValueFactory<>("section"));
        
        // MAKE COLUMNS NOT REORDERABLE
        table.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            for (TableColumn<OfferedCourse, ?> col : table.getColumns()) {
                col.setReorderable(false);
            }
        });
        
        // MAKE USERS ARRAY INTO SOMETHING THE TABLE CAN READ
	    ObservableList<OfferedCourse> data = FXCollections.observableArrayList(allOfferedCourses);
        table.setItems(data);
        table.getColumns().addAll(
        		codeCol, classDetailsCol);
        HBox box = new HBox(table);
        HBox.setHgrow(table, Priority.ALWAYS);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        
        return box;
	}
}
