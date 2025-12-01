package frontend;

import backend.OfferedCourseManager;
import backend.Course;
import backend.CourseManager;
import backend.Student;
import backend.StudentManager;
import javafx.util.Duration;
import javafx.animation.FadeTransition;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.nio.file.Path;
import java.util.*;

import backend.CourseManager.Degree;

public class CourseSelectionScreen {
	private Stage ownerStage; // The actual WelcomeScreen
	private Student student; // The student created after signing-up
	
	// Constructor
	public CourseSelectionScreen(Stage ownerStage, Student student) {
		this.ownerStage = ownerStage;
		this.student = student;
		
		// There is no this.pop-upStage = new Stage() since this screen replaces the SignUpScreen
	}

	// Running the screen
	public void show() {
		Stage popup = new Stage(); // Create pop-up window
		popup.initOwner(ownerStage); // Attach to WelcomeScreen
		popup.initModality(Modality.APPLICATION_MODAL); // Prevents interaction with other windows
		popup.setResizable(false); // Not resizable
		popup.setTitle("Sign-Up");

		popup.getIcons().add(new Image(getClass().getResourceAsStream("/resources/logo.png")));

		// Prevent the user from closing the popup manually
		popup.setOnCloseRequest(event -> {
			event.consume(); // Makes close button unclosable
		});

		// Layout for background
		BorderPane root = new BorderPane(); // Used a BorderPane since layout is much simpler
		root.setPadding(new Insets(25));
		root.setStyle("-fx-background-color: #ffc2d1;");

		// Semi-white box container
		VBox form = new VBox(20);
		form.setAlignment(Pos.TOP_LEFT);
		form.setPadding(new Insets(30));
		form.setStyle("-fx-background-color: rgba(255,255,255,0.95); -fx-background-radius: 20;"); // RGB + opacity for transparent look

		// Font Styling for the texts
		Font poppinsBold = Font.loadFont(getClass().getResourceAsStream("/resources/Poppins Bold.ttf"), 32);
		Font inter = Font.loadFont(getClass().getResourceAsStream("/resources/Inter.ttf"), 14);
		Font interItalic = Font.loadFont(getClass().getResourceAsStream("/resources/Inter Italic.ttf"), 14);

		Label title = new Label("Sign-Up");
		title.setFont(poppinsBold);

		// Subtitles using Text/TextFlow so custom styling stays intact
		Text subtitleText1 = new Text("Check the courses you have completed as a " + student.getDegree() + " student.");
		subtitleText1.setFont(interItalic);
		subtitleText1.setStyle("-fx-text-fill: #444444;");
		TextFlow subtitle1 = new TextFlow(subtitleText1);

		Text subtitleText2 = new Text("If you haven't completed any courses yet, you may continue without selecting anything.");
		subtitleText2.setFont(interItalic);
		subtitleText2.setStyle("-fx-text-fill: #6a6a6a;");
		TextFlow subtitle2 = new TextFlow(subtitleText2);

		// Load all courses from the CSV file
		// TODO load all courses from CourseMqa
		List<Course> coursesByDegree = CourseManager.getCoursesByDegree(student.getDegree());

		ObservableList<CheckBox> courseCheckboxes = FXCollections.observableArrayList(); // Store all the CheckBoxes (one per course)
		Map<CheckBox, Course> cbToCourse = new HashMap<>(); // Map each CheckBox to the Course it represents

		// Create a CheckBox per degree-specific course
		for (Course c : coursesByDegree) { // Loop through the course per degree
			CheckBox cb = new CheckBox(c.getCourseCode());
			cb.setFont(inter);
			cb.setStyle("-fx-padding: 5 0 5 0;");
			
			// When a course is selected, re-check the prerequisites for all courses
			cb.selectedProperty().addListener((obs, w, s) -> handlePrerequisites(courseCheckboxes));
			
			courseCheckboxes.add(cb); // Add to the list of checked courses
			cbToCourse.put(cb, c);
		}
		
		handlePrerequisites(courseCheckboxes); // Ensure prerequisites logic runs initially

		// Search Bar that filters in real time
		TextField searchField = new TextField();
		searchField.setPromptText("Search courses...");
		searchField.setPrefWidth(380);
		searchField.setFont(inter);
		searchField.setStyle(
			"-fx-background-radius: 8;" +
			"-fx-padding: 6 10 6 10;" +
			"-fx-border-radius: 8;"
		);

		// FilteredList wraps the master observable list
		FilteredList<CheckBox> filteredList = new FilteredList<>(courseCheckboxes, p -> true);

		// Live filter as user types -> filter by course code OR course title (case-insensitive)
		searchField.textProperty().addListener((obs, oldVal, newVal) -> {
			String filter = (newVal == null) ? "" : newVal.toLowerCase().trim(); // Normalizes the search text by ignoring spacing and case
			
			if (filter.isEmpty()) { // If search bar is empty, show all courses
				filteredList.setPredicate(cb -> true);
			} else { 
				filteredList.setPredicate(cb -> {
					Course c = cbToCourse.get(cb);
					if (c == null) return false;
					
					String code = c.getCourseCode() == null ? "" : c.getCourseCode().toLowerCase(); // For course code
					String titleStr = c.getCourseTitle() == null ? "" : c.getCourseTitle().toLowerCase(); // For course name
					
					return code.contains(filter) || titleStr.contains(filter); // Match if the text appears anywhere in the code or title
				});
			}
		});

		// ListView to visually show filtered courses
		ListView<CheckBox> listView = new ListView<>(filteredList);
		listView.setPrefHeight(350);
		listView.setStyle(
			"-fx-border-radius: 8; " +
			"-fx-background-radius: 8; " +
			"-fx-padding: 6;"
		);

		// Custom ListCell to display each course with a checkbox, the course title, and units
		listView.setCellFactory(lv -> new ListCell<CheckBox>() {
			@Override
			protected void updateItem(CheckBox cb, boolean empty) {
				super.updateItem(cb, empty);

				if (empty || cb == null) { // If the cell is empty, clear content
					setGraphic(null);
					setText(null);
					return;
				}

				Course course = cbToCourse.get(cb); // Retrieve the Course associated with this CheckBox

				// Container for checkbox and course details
				VBox container = new VBox(2);
				container.setPadding(new Insets(6, 4, 6, 4));

				// Style the checkbox
				cb.setFont(inter);
                cb.setMaxWidth(Double.MAX_VALUE);
                container.getChildren().add(cb);

                if (course != null) { // Add course title and units, if the course exists
                	String title = course.getCourseTitle();
                	int units = course.getUnits();

                	// Style the details
                	Label details = new Label(title + " (" + units + " units)");
                	details.setStyle("-fx-font-size: 12px; -fx-text-fill: #555;");
                	details.setFont(inter);
                	details.setWrapText(true); // Wrap long titles

                	container.getChildren().add(details);
                }
                setGraphic(container); // Set the custom layout as the cell's content
			}
		});

		// Continue button and styling
		Button continueBtn = new Button("Continue");
		continueBtn.setPrefWidth(300);
		continueBtn.setStyle("-fx-font-size: 20px;");
		
		continueBtn.setStyle(
				"-fx-background-color: #fb6f92;" + // Main color of the button (darker pink)
				"-fx-background-radius: 8;" +
				"-fx-text-fill: #ffe5ec;" +
				"-fx-font-size: 20px;" + // Font size 20
				"-fx-font-weight: bold;"
        );

		// Event Handlers (when hovered and exit hovering)
		continueBtn.setOnMouseEntered(e ->
			continueBtn.setStyle(
				"-fx-background-color: #ff85ac;" + // Lighter color when hovered
				"-fx-background-radius: 8;" +
				"-fx-text-fill: #ffe5ec;" +
				"-fx-font-size: 20px;" + // Font size 20
				"-fx-font-weight: bold;"
			)
		);

		continueBtn.setOnMouseExited(e ->
			continueBtn.setStyle(
				"-fx-background-color: #fb6f92;" + // Revert back to original when not hovered anymore
				"-fx-background-radius: 8;" +
				"-fx-text-fill: #ffe5ec;" +
				"-fx-font-size: 20px;" + // Font size 20
				"-fx-font-weight: bold;"
			)
		);

		// Events when sign-up button is clicked
		continueBtn.setOnAction(e -> {
			List<Course> selectedCourses = new ArrayList<>(); // List that will store all selected courses
			for (CheckBox cb : courseCheckboxes) { // Loop through every checkbox in the list
				if (cb.isSelected()) {
					Course c = cbToCourse.get(cb);
					if (c != null) selectedCourses.add(c); // Convert checkbox to Course object using map
				}
			}

			// Update the student's completed courses list
			student.getCompletedCourses().clear();
			student.getCompletedCourses().addAll(selectedCourses);

			// Save updated student info to students.txt
			StudentManager studentManager = StudentManager.load(); // Load current manager
			if (studentManager != null) {
				studentManager.updateStudent(student); // Write updated student to file
			}

			showToast(ownerStage, "Sign-up complete! You may now log-in."); // Show a temporary fading message
			popup.close(); // Close the course selection pop-up
		});
		
		// Centered layout box for the button
		HBox buttonContainer = new HBox(continueBtn);
		buttonContainer.setAlignment(Pos.CENTER);

		// Assembles everything into the form
		form.getChildren().addAll(title, subtitle1, subtitle2, searchField, listView, buttonContainer);
		root.setCenter(form);

		Scene scene = new Scene(root, 600, 620);
		popup.setScene(scene); // Apply to stage
		popup.show(); // Show the pop-up screen
	}



	// Enables/disables CheckBoxes depending on whether their prer`equisites are checked
	private void handlePrerequisites(ObservableList<CheckBox> checkboxes) {
		for (CheckBox cb : checkboxes) { // Loop through every course checkbox
			String code = cb.getText(); // Get the course code shown on the checkbox
			List<String> prereqs = CourseManager.prereqMap.get(code); // Look up its prerequisites

			if (prereqs == null || prereqs.isEmpty()) { // If there are no prerequisite
				cb.setDisable(false); // Always enabled
			} else {
				boolean allSatisfied = prereqs.stream().allMatch(prereqCode -> // Check if all prerequisite are satisfied
					checkboxes.stream().anyMatch(c -> c.getText().equals(prereqCode) && c.isSelected())
				);

				cb.setDisable(!allSatisfied); // Disable the course if prerequisite are NOT met

				if (!allSatisfied) cb.setSelected(false); // Disabled courses must NOT stay selected
			}
		}
	}

	// Toast popup animation (fades after a few seconds)
	private void showToast(Stage owner, String message) {
		Stage toast = new Stage();
		toast.initOwner(owner);
		toast.setAlwaysOnTop(true);
		toast.initStyle(StageStyle.TRANSPARENT);

		Label msg = new Label(message);
		msg.setStyle(
			"-fx-background-color: #fb6f92; " +
			"-fx-text-fill: #ffe5ec; " +
			"-fx-padding: 12px 20px; " +
			"-fx-background-radius: 20; " +
			"-fx-font-size: 14px; " +
			"-fx-font-family: 'Inter';"
		);

		StackPane root = new StackPane(msg);
		root.setStyle("-fx-background-color: transparent;");
		root.setPadding(new Insets(10));

		Scene scene = new Scene(root);
		scene.setFill(Color.TRANSPARENT);

		// Center bottom of WelcomeScreen
		toast.setScene(scene);
		toast.setX(owner.getX() + owner.getWidth()/2 - 150);
		toast.setY(owner.getY() + owner.getHeight() - 80);
		toast.show();

		// Fade out effect transition
		FadeTransition fade = new FadeTransition(Duration.seconds(5), root);
		fade.setFromValue(1.0);
		fade.setToValue(0.0);
		fade.setOnFinished(e -> toast.close());
		fade.play();
	}
}

