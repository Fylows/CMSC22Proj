package frontend;

import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

import java.util.ArrayList;


import backend.OfferedCourse;
import backend.RegSystem;
import backend.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class EnlistmentScreen extends VBox {
	private GridPane calendar = new GridPane();
	private ArrayList<OfferedCourse> offered = RegSystem.getAllCourses();
	private Student student = ContentArea.getStudent();
	public EnlistmentScreen() {
		setSpacing(20);
		setPadding(new Insets(60, 20, 20, 20)); 
		setStyle("-fx-background-color: white;");

        		
        // ---------- CALENDAR AND ERROR SCREEN ----------
		
		
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

        // ---------- COURSE SEARCH ----------
        VBox courseSearch = createCourseSearchGrid(offered);

        getChildren().addAll(calendarAndWarnings,courseSearch);
	}
	
	private VBox createCourseSearchGrid(ArrayList<OfferedCourse> allOfferedCourses) {
	    // Observable list for easier pagination
	    ObservableList<OfferedCourse> allCourses = FXCollections.observableArrayList(allOfferedCourses);

	    // Pagination variables
	    final int ITEMS_PER_PAGE = 10;
	    final int[] currentPage = {0}; // Using array to allow modification inside lambda

	    // Create the GridPane
	    GridPane grid = new GridPane();
	    grid.setHgap(10);
	    grid.setVgap(5);
	    grid.setPadding(new Insets(10));

	    // Column headers
	    grid.addRow(0,
	        new Label("Course"),
	        new Label("Associated Lab"),
	        new Label("Action")
	    );

	    // Method to update the grid for a given page
	    Runnable showPage = () -> {
	        grid.getChildren().removeIf(node -> GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) > 0);

	        int start = currentPage[0] * ITEMS_PER_PAGE;
	        int end = Math.min(start + ITEMS_PER_PAGE, allCourses.size());

	        for (int i = start; i < end; i++) {
	            OfferedCourse course = allCourses.get(i);

	            Label courseLabel = new Label(course.getCourseCode());
	            Label labLabel = new Label(course.getSection()); // Assuming section is the lab info
	            Button enlistBtn = new Button("Add");

	            // Example action for the button
	            enlistBtn.setOnAction(e -> {
	            	if(RegSystem.enrollStudentInOfferedCourse(student, course)) RegSystem.fillTime(calendar, course);
	            });

	            grid.addRow(i - start + 1, courseLabel, labLabel, enlistBtn);
	        }
	    };

	    // Pagination controls
	    Button prevBtn = new Button("Previous");
	    Button nextBtn = new Button("Next");
	    Label pageLabel = new Label();

	    prevBtn.setOnAction(e -> {
	        if (currentPage[0] > 0) {
	            currentPage[0]--;
	            showPage.run();
	            pageLabel.setText("Page " + (currentPage[0] + 1));
	        }
	    });

	    nextBtn.setOnAction(e -> {
	        if ((currentPage[0] + 1) * ITEMS_PER_PAGE < allCourses.size()) {
	            currentPage[0]++;
	            showPage.run();
	            pageLabel.setText("Page " + (currentPage[0] + 1));
	        }
	    });

	    HBox pagination = new HBox(10, prevBtn, pageLabel, nextBtn);
	    pagination.setPadding(new Insets(10));
	    pageLabel.setText("Page 1");

	    // Initial page
	    showPage.run();

	    VBox container = new VBox(10, grid, pagination);
	    return container;
	}
}
