package frontend;

import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

import java.util.ArrayList;

import javafx.collections.ObservableList;
import backend.OfferedCourse;
import backend.RegSystem;
import backend.Student;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
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
		// TODO initiate calendar fill with subjs
		for (OfferedCourse oc: student.getEnrolledCourses()) {
			RegSystem.fillTime(calendar, oc);
		}
		
		HBox warnings = new HBox();
		warnings.setPadding(new Insets(20));
		Label lab = new Label("Warnings");
		warnings.getChildren().add(lab);
		
		HBox calendarAndWarnings = new HBox(calendar, warnings);
		 

        // ---------- ACTIVE ENLISTMENTS ----------
		VBox activeEnlistments = createActiveEnlistments(student.getEnrolledCourses());
		
        // ---------- COURSE SEARCH ----------
        VBox courseSearch = createCourseSearchGrid(offered);
		
        

        getChildren().addAll(calendarAndWarnings,activeEnlistments, courseSearch);
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
	            	// add student to course
	            	if(RegSystem.enrollStudentInOfferedCourse(student, course)) {
	            		RegSystem.fillTime(calendar, course);
	            		RegSystem.fillTime(calendar, course.getLec());
	            	}
	            	
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
	
	private VBox createActiveEnlistments(ArrayList<OfferedCourse> studentEnrolledCourses) {
	    // Make the student's courses observable
	    ObservableList<OfferedCourse> studentCourses = FXCollections.observableArrayList(studentEnrolledCourses);

	    // Create a ListView to show the courses
	    ListView<OfferedCourse> listView = new ListView<>(studentCourses);

	    // Custom cell factory for each course
	    listView.setCellFactory(lv -> new ListCell<>() {
	        private final HBox hbox = new HBox(10);
	        private final Label classLabel = new Label();
	        private final Button deleteBtn = new Button("Delete");

	        {
	            hbox.getChildren().addAll(classLabel, deleteBtn);

	            deleteBtn.setOnAction(e -> {
	                OfferedCourse course = getItem();
	                if (course != null) {
	                    // Remove course from student and course's student list
	   
		                RegSystem.resetTime(calendar, course);

	                    student.getEnrolledCourses().remove(course);
	                    course.getEnrolledStudents().remove(student);

	                    // Also remove linked lecture if exists
	                    if (course.getLec() != null) {
	                        student.getEnrolledCourses().remove(course.getLec());
	                        course.getLec().getEnrolledStudents().remove(student);
	                        studentCourses.remove(course.getLec());
		            		RegSystem.resetTime(calendar, course.getLec());

	                    }

	                    // Remove from observable list so ListView refreshes automatically
	                    studentCourses.remove(course);
	                }
	            });
	        }

	        @Override
	        protected void updateItem(OfferedCourse course, boolean empty) {
	            super.updateItem(course, empty);
	            if (empty || course == null) {
	                setGraphic(null);
	            } else {
	                classLabel.setText(course.getCourseCode() + " - " + course.getSection());
	                setGraphic(hbox);
	            }
	        }
	    });

	    // Optional: add a header label
	    Label header = new Label("Active Enrollments");
	    VBox vbox = new VBox(10, header, listView);
	    vbox.setPadding(new Insets(10));

	    return vbox;
	}



}
