package frontend;

import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javafx.collections.ObservableList;
import backend.OfferedCourse;
import backend.RegSystem;
import backend.Student;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class EnlistmentScreen extends VBox {
	private GridPane calendar = new GridPane();
	private ArrayList<OfferedCourse> offered = RegSystem.getAllCourses();
	private Student student = ContentArea.getStudent();
	private ObservableList<OfferedCourse> studentCourses = FXCollections.observableList(student.getEnrolledCourses());
	 
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
		VBox activeEnlistments = createActiveEnlistments();
		
        // ---------- COURSE SEARCH ----------
        VBox courseSearch = createCourseSearchGrid(offered);
		
        

        getChildren().addAll(calendarAndWarnings,activeEnlistments, courseSearch);
	}

	private VBox createCourseSearchGrid(ArrayList<OfferedCourse> allOfferedCourses) {
	    ObservableList<OfferedCourse> allCourses = FXCollections.observableArrayList(allOfferedCourses);

	    final int[] currentPage = {0};
	    final int[] itemsPerPage = {10};

	    VBox container = new VBox(10);
	    container.setPadding(new Insets(10));

	    // --- FILTER AND DROPDOWN ---
	    HBox filterRow = new HBox(10);
	    filterRow.setAlignment(Pos.CENTER_LEFT);

	    TextField searchField = new TextField();
	    searchField.setPromptText("Search by course name or section");

	    ComboBox<Integer> itemsPerPageDropdown = new ComboBox<>();
	    itemsPerPageDropdown.getItems().addAll(5, 10, 15, 20);
	    itemsPerPageDropdown.setValue(itemsPerPage[0]);

	    filterRow.getChildren().addAll(new Label("Filter:"), searchField, new Label("Items per page:"), itemsPerPageDropdown);
	    container.getChildren().add(filterRow);

	    // --- LIST GRID ---
	    VBox gridContainer = new VBox(5);
	    container.getChildren().add(gridContainer);

	    // --- PAGINATION ---
	    Button prevBtn = new Button("Previous");
	    Button nextBtn = new Button("Next");
	    Label pageLabel = new Label("Page 1");
	    HBox pagination = new HBox(10, prevBtn, pageLabel, nextBtn);
	    pagination.setAlignment(Pos.CENTER_LEFT);
	    pagination.setPadding(new Insets(10));
	    container.getChildren().add(pagination);

	    // --- SHOW PAGE LOGIC ---
	    final Runnable[] showPage = new Runnable[1];
	    showPage[0] = () -> {
	        gridContainer.getChildren().clear();

	        // Apply filter
	        String query = searchField.getText().trim().toLowerCase();
	        List<OfferedCourse> filtered = allCourses.stream()
	                .filter(c -> c.getCourseCode().toLowerCase().contains(query)
	                        || c.getSection().toLowerCase().contains(query))
	                .collect(Collectors.toList());

	        int totalItems = filtered.size();
	        int start = currentPage[0] * itemsPerPage[0];
	        int end = Math.min(start + itemsPerPage[0], totalItems);

	        for (int i = start; i < end; i++) {
	            OfferedCourse course = filtered.get(i);

	            // Course code label
	            Label codeLabel = new Label(course.getCourseCode());

	            // Lecture details
	            OfferedCourse lecture = course.getLec() != null ? course.getLec() : course;
	            VBox lectureBox = new VBox(3);
	            lectureBox.getChildren().add(new Label("Lecture: " + lecture.getCourseCode() + " - " + lecture.getSection()));
	            lectureBox.getChildren().add(new Label("Schedule: " + lecture.getTimes()));

	            // Lab details if exists
	            VBox labBox = new VBox(3);
	            if (course.getLec() != null) {
	                labBox.getChildren().add(new Label("Lab: " + course.getCourseCode() + " - " + course.getSection()));
	                labBox.getChildren().add(new Label("Schedule: " + course.getTimes()));
	            } else {
	                labBox.getChildren().add(new Label("No associated lab"));
	            }

	            // Add button
	            Button addBtn = new Button("Add");
	            addBtn.setOnAction(e -> {
	                if (RegSystem.enrollStudentInOfferedCourse(student, course, studentCourses) == 0) {
	                    RegSystem.fillTime(calendar, course);
	                    if (course.getLec() != null) {
	                        RegSystem.fillTime(calendar, course.getLec());
	                    }
	                }
	            });

	            // Row HBox with separate columns
	            HBox row = new HBox(20, codeLabel, lectureBox, labBox, addBtn);
	            row.setAlignment(Pos.CENTER_LEFT);
	            gridContainer.getChildren().add(row);
	        }

	        // Update page label
	        pageLabel.setText("Page " + (currentPage[0] + 1) + " of " + ((totalItems + itemsPerPage[0] - 1) / itemsPerPage[0]));
	    };

	    // --- LISTENERS ---
	    searchField.textProperty().addListener((obs, oldVal, newVal) -> {
	        currentPage[0] = 0;
	        showPage[0].run();
	    });

	    itemsPerPageDropdown.valueProperty().addListener((obs, oldVal, newVal) -> {
	        itemsPerPage[0] = newVal;
	        currentPage[0] = 0;
	        showPage[0].run();
	    });

	    prevBtn.setOnAction(e -> {
	        if (currentPage[0] > 0) {
	            currentPage[0]--;
	            showPage[0].run();
	        }
	    });

	    nextBtn.setOnAction(e -> {
	        int totalItems = (int) allCourses.stream()
	                .filter(c -> c.getCourseCode().toLowerCase().contains(searchField.getText().trim().toLowerCase())
	                        || c.getSection().toLowerCase().contains(searchField.getText().trim().toLowerCase()))
	                .count();
	        if ((currentPage[0] + 1) * itemsPerPage[0] < totalItems) {
	            currentPage[0]++;
	            showPage[0].run();
	        }
	    });

	    // Initial page
	    showPage[0].run();

	    return container;
	}




	private VBox createActiveEnlistments() {
	    VBox vbox = new VBox(10);
	    vbox.setPadding(new Insets(10));

	    Label header = new Label("Active Enrollments");
	    vbox.getChildren().add(header);

	    // Container for all courses
	    VBox courseContainer = new VBox(5);
	    vbox.getChildren().add(courseContainer);

	    final Runnable[] refreshGrid = new Runnable[1];

	    refreshGrid[0] = () -> {
	        courseContainer.getChildren().clear();

	        if (studentCourses.isEmpty()) {
	            Label emptyLabel = new Label("No active enlistment. Use the section below to add classes.");
	            courseContainer.getChildren().add(emptyLabel);
	            return;
	        }

	        for (OfferedCourse course : studentCourses) {
	            // Skip labs, only process main lectures
	            if (course.getLec() == null) continue;

	            // Lecture details pane
	            VBox lectureDetails = new VBox(5,
            		new Label("Course Code: " + course.getLec().getCourseCode()),
                    new Label("Section: " + course.getLec().getSection()),
                    new Label("Schedule: " + course.getLec().getTimes())
	            );
	            TitledPane lecturePane = new TitledPane("Lecture", lectureDetails);
	            lecturePane.setExpanded(false);

	            // Lab details pane
	            VBox labDetails;
	            if (studentCourses.contains(course.getLec())) {
	                labDetails = new VBox(5,
	                    new Label("Course Code: " + course.getCourseCode()),
	                    new Label("Section: " + course.getSection()),
	                    new Label("Schedule: " + course.getTimes())
	                );
	            } else {
	                labDetails = new VBox(5, new Label("No associated lab"));
	            }
	            TitledPane labPane = new TitledPane("Lab", labDetails);
	            labPane.setExpanded(false);

	            // Action buttons
	            Button deleteBtn = new Button("Delete");
	            deleteBtn.setOnAction(e -> {
	                RegSystem.resetTime(calendar, course);
                    studentCourses.remove(course);
                    course.getEnrolledStudents().remove(student);
	                //RegSystem.dropStudentFromOfferedCourse(student, course);

	                if (course.getLec() != null) {
	                    studentCourses.remove(course.getLec());
	                    course.getLec().getEnrolledStudents().remove(student);
	                    RegSystem.resetTime(calendar, course.getLec());
	                    //RegSystem.dropStudentFromOfferedCourse(student, course.getLec());
	                }
	                RegSystem.getStudentManager().updateStudent(student);

	                
	                refreshGrid[0].run();
	            });

	            Button enlistBtn = new Button("Enlist");
	            enlistBtn.setDisable(true); // You can enable logic later

	            HBox actionBox = new HBox(10, deleteBtn, enlistBtn);
	            actionBox.setAlignment(Pos.CENTER_LEFT);

	            // Wrap everything in a grid-like HBox
	            HBox row = new HBox(20, lecturePane, labPane, actionBox);
	            row.setAlignment(Pos.CENTER_LEFT);
	            courseContainer.getChildren().add(row);
	        }
	    };

	    // Listener to update whenever studentCourses changes
	    studentCourses.addListener((javafx.collections.ListChangeListener<OfferedCourse>) change -> refreshGrid[0].run());

	    // Initial build
	    refreshGrid[0].run();

	    return vbox;
	}

}
