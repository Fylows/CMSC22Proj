package frontend;

import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javafx.collections.ObservableList;
import backend.OfferedCourse;
import backend.RegSystem;
import backend.Student;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class EnlistmentScreen extends VBox {
	private GridPane calendar = new GridPane();
	private ArrayList<OfferedCourse> offered = RegSystem.getAllCourses();
	private Student student = ContentArea.getStudent();
	private StackPane root;
	private ObservableList<OfferedCourse> studentCourses = FXCollections.observableList(student.getEnrolledCourses());
	 
	public EnlistmentScreen(StackPane parentStack) {
		this.root = parentStack;
		
		this.getStyleClass().add("enlistment-root");
		setSpacing(20);
		
        // ---------- CALENDAR AND ENLISTMENTS TAB ----------
		VBox calendarContainer = new VBox(10);
		calendarContainer.getStyleClass().add("schedule-grid");
		calendarContainer.setPadding(new Insets(20));
		
		Label header1 = new Label("Schedule");
		header1.getStyleClass().add("section-title");

		// Column headers (Monday to Saturday)
		String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

		// Time intervals (7:00 AM to 7:00 PM, 30-min intervals)
		List<String> times = new ArrayList<>();
		for (int hour = 7; hour <= 19; hour++) {
		    String ampm = hour < 12 ? "AM" : "PM";
		    int displayHour = hour <= 12 ? hour : hour - 12;
		    times.add(displayHour + ":00 " + ampm);
		    times.add(displayHour + ":30 " + ampm);
		}

		// Add column headers
		for (int col = 0; col < days.length; col++) {
		    Label dayLabel = new Label(days[col]);
		    dayLabel.getStyleClass().add("calendar-day-label");
		    StackPane headerPane = new StackPane(dayLabel);
		    headerPane.setPrefSize(80, 40);
		    calendar.add(headerPane, col + 1, 0); // +1 because column 0 is for time labels
		}

		// Add row headers and cells
		for (int row = 0; row < times.size(); row++) {
		    // Time label in first column
		    Label timeLabel = new Label(times.get(row));
		    timeLabel.setPrefSize(80, 40);
		    timeLabel.getStyleClass().add("calendar-time-label");
		    calendar.add(timeLabel, 0, row + 1); // +1 because row 0 is for column headers

		    // Cells for each day
		    for (int col = 0; col < days.length; col++) {
		        Rectangle cell = new Rectangle(80, 30);
		        cell.setFill(Color.WHITE);
		        cell.setStroke(Color.BLACK);

		        StackPane cellPane = new StackPane(cell);
		        calendar.add(cellPane, col + 1, row + 1);
		    }
		}

		calendar.setAlignment(Pos.CENTER_LEFT);
		calendarContainer.getChildren().addAll(header1, calendar);
		calendarContainer.setAlignment(Pos.CENTER_LEFT);
		
		int totalUnits = 0;
		for (OfferedCourse oc: student.getEnrolledCourses()) {
			RegSystem.fillTime(calendar, oc);
			if (oc.getLec() == null) {
				totalUnits += oc.getCourse().getUnits();
			}
		}
		
		
		VBox enlistments = new VBox();
		enlistments.getStyleClass().add("active-enlistments");
		enlistments.setPadding(new Insets(20));

		Label header2 = new Label("Active Enlistments");
		header2.getStyleClass().add("section-title");
		enlistments.getChildren().add(header2);
		
		Label units = new Label("Total units: " + totalUnits);
		units.setStyle("-fx-font-family: \"Inter\"; -fx-font-size: 14px; -fx-text-fill: #333;");
		
		VBox activeEnlistments = createActiveEnlistments();
		studentCourses.addListener((ListChangeListener<OfferedCourse>) change -> {
			int total = 0;
			for (OfferedCourse oc : studentCourses) {
				if (oc.getLec() == null) {
					total += oc.getCourse().getUnits();
				}
		    }
            units.setText("Total Units: " + total);
        });
		
		Color[] colors = {Color.PINK, Color.LIGHTBLUE, Color.LIGHTSEAGREEN};
		String[] states = {"Bookmarked", "Enlisted", "Finalized"};
		String[] tooltips = {
		    "Courses you have bookmarked but not yet enlisted",
		    "Courses you are currently enlisted in",
		    "Courses that are finalized in your schedule"
		};

		HBox enlistmentDetails = new HBox(20);
		for (int i = 0; i < 3; i++) {
		    Rectangle rect = new Rectangle(100, 80, colors[i]);
		    rect.getStyleClass().add("legend-rect");

		    // Create label
		    Label label = new Label(states[i]);
		    label.getStyleClass().add("legend-label");

		    // StackPane to put label on top of rectangle
		    StackPane stack = new StackPane();
		    stack.getChildren().addAll(rect, label);
		    stack.setAlignment(Pos.CENTER);

		    // Add tooltip
		    Tooltip tooltip = new Tooltip(tooltips[i]);
		    Tooltip.install(stack, tooltip); // Attach tooltip to the StackPane

		    // Add to HBox
		    enlistmentDetails.getChildren().add(stack);
		}
		
		enlistments.getChildren().addAll(enlistmentDetails,units,activeEnlistments);
		HBox calendarAndCourses = new HBox(calendarContainer, enlistments);
        // ---------- COURSE SEARCH ----------
        VBox courseSearch = createCourseSearchGrid(offered);
		VBox courseSearchContainer = new VBox();
		courseSearchContainer.getStyleClass().add("search-section");

		Label header3 = new Label("Search Class");
		courseSearchContainer.getChildren().addAll(header3, courseSearch);
		header3.getStyleClass().add("section-title");
		
		calendarAndCourses.setAlignment(Pos.CENTER);
		courseSearchContainer.setAlignment(Pos.CENTER);
        getChildren().addAll(calendarAndCourses, courseSearchContainer);
	}

	private VBox createCourseSearchGrid(ArrayList<OfferedCourse> allOfferedCourses) {
	    ObservableList<OfferedCourse> allCourses = FXCollections.observableArrayList(allOfferedCourses);

	    final int[] currentPage = {0};
	    final int[] itemsPerPage = {10};

	    VBox container = new VBox(10);
	    container.setPadding(new Insets(10));

	    // --- FILTER AND DROPDOWN ---
	    HBox filterRow = new HBox(10);
	    filterRow.setAlignment(Pos.CENTER);

	    TextField searchField = new TextField();
	    searchField.getStyleClass().add("search-field");
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
	    prevBtn.getStyleClass().add("pagination-button");
        nextBtn.getStyleClass().add("pagination-button");
	    Label pageLabel = new Label("Page 1");
	    HBox pagination = new HBox(10, prevBtn, pageLabel, nextBtn);
	    pagination.setAlignment(Pos.CENTER);
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
	            addBtn.getStyleClass().add("add-btn");
	            addBtn.setOnAction(e -> {
	            	int state = RegSystem.enrollStudentInOfferedCourse(student, course, studentCourses);
	                if (state == 0) {
	                    RegSystem.fillTime(calendar, course);
	                    if (course.getLec() != null) {
	                        RegSystem.fillTime(calendar, course.getLec());
	                    }
	                    showToast(root, "Course list updated!", true);
	                }
	                else {
	                	String message = "";
	                	switch(state) {
	                		case(-1) : message = "null pointer exception."; break;
	                		case (1) : message = "Already enlisted in " + course.getCourseCode() + "."; break;
	                		case (2) : message = course.getCourseCode() + " is a " + course.getCourse().getType() + " course."; break;
	                		case (3) : message = "Prerequisites not met."; break;
	                		case (4) : message = "Course time conflicts"; break;
	                	}
	                	System.out.println(state);
	                	showToast(root, message , false);
	                }
	            });

	            // Row HBox with separate columns
	            HBox row = new HBox(20, codeLabel, lectureBox, labBox, addBtn);
	            row.setAlignment(Pos.CENTER);
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
                    new Label("Schedule: " + course.getLec().getTimes()),
                    new Label("Room: " + course.getLec().getRoom())
	            );
	            TitledPane lecturePane = new TitledPane("Lecture", lectureDetails);
	            lecturePane.setExpanded(false);

	            // Lab details pane
	            VBox labDetails;
	            if (studentCourses.contains(course.getLec())) {
	                labDetails = new VBox(5,
	                    new Label("Course Code: " + course.getCourseCode()),
	                    new Label("Section: " + course.getSection()),
	                    new Label("Schedule: " + course.getTimes()),
	                    new Label("Room: " + course.getLec().getRoom())

	                );
	            } else {
	                labDetails = new VBox(5, new Label("No associated lab"));
	            }
	            TitledPane labPane = new TitledPane("Lab", labDetails);
	            labPane.setExpanded(false);

	            // Action buttons
	            Button deleteBtn = new Button("Delete");
	            deleteBtn.getStyleClass().add("delete-btn");
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

                    showToast(root, "Course list updated!", true);


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
	
	public void showToast(StackPane root, String message, boolean success) {
	    Label toast = new Label(message);
	    toast.toFront();
	    toast.setTextFill(Color.WHITE);
	    toast.setFont(Font.font(16));
	    toast.setStyle("-fx-background-radius: 8; -fx-padding: 10px;");
	    toast.setAlignment(Pos.CENTER);

	    // Background color
	    if (success) {
	        toast.setStyle(toast.getStyle() + "-fx-background-color: #4CAF50;"); // green
	    } else {
	        toast.setStyle(toast.getStyle() + "-fx-background-color: #f44336;"); // red
	    }

	    // Center in StackPane
	    StackPane.setAlignment(toast, Pos.BOTTOM_RIGHT);

	    // Add to the root StackPane
	    root.getChildren().add(toast);

	    FadeTransition fade = new FadeTransition(Duration.seconds(0.5), toast);
	    fade.setFromValue(1.0);
	    fade.setToValue(0.0);
	    fade.setDelay(Duration.seconds(1.5));
	    fade.setOnFinished(e -> root.getChildren().remove(toast));
	    fade.play();
	}




}
