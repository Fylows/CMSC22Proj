package frontend;

import backend.Course;
import backend.CourseManager;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

public class ProgramCoursesScreen extends BorderPane {
	private final String degree;
	private final VBox courseListContainer;  
	private final List<VBox> allCourseBoxes = new ArrayList<>();

	private final ScrollPane scrollPane;
	private TextField searchField;

	public ProgramCoursesScreen(String degree) {
		this.degree = degree;
		this.getStyleClass().add("program-courses-root");

		// Top container for search bar
		VBox topContainer = new VBox(10);
		topContainer.getStyleClass().add("top-container");

		// Search bar
		searchField = new TextField();
		searchField.setPromptText("Search courses by code or title...");
		searchField.getStyleClass().add("search-bar");

		topContainer.getChildren().addAll(searchField);
		this.setTop(topContainer);
		BorderPane.setAlignment(topContainer, Pos.TOP_LEFT);

		// Container for all course boxes
		courseListContainer = new VBox(15);
		courseListContainer.getStyleClass().add("course-list-container");

		scrollPane = new ScrollPane(courseListContainer);
		scrollPane.setFitToWidth(true);
		scrollPane.getStyleClass().add("scroll-pane");

		this.setCenter(scrollPane);

		loadCourses(degree); // Load all courses per degree

		// Apply live searching
		searchField.textProperty().addListener((obs, oldVal, newVal) -> {
			String filter = (newVal == null) ? "" : newVal.toLowerCase().trim();
			courseListContainer.getChildren().clear();

			boolean anyMatch = false; // Track if we added any course

			for (VBox box : allCourseBoxes) {
				Label codeLabel = (Label) box.getChildren().get(0); // course code
				Label titleLabel = (Label) box.getChildren().get(1); // course title

				String code = codeLabel.getText().toLowerCase();
				String title = titleLabel.getText().toLowerCase();

				if (code.contains(filter) || title.contains(filter)) {
					courseListContainer.getChildren().add(box);
					anyMatch = true;
				}
			}

			// Show "no results" message if nothing matched
			if (!anyMatch) {
				Label noResults = new Label("No courses found :(");
				noResults.getStyleClass().add("no-results-label"); // Make sure you have this in your CSS

				StackPane placeholder = new StackPane(noResults);
				placeholder.setPrefHeight(scrollPane.getViewportBounds().getHeight());
				placeholder.setAlignment(Pos.CENTER);

				courseListContainer.getChildren().add(placeholder);
			}
		});
	}

	// Loads and displays all courses belonging to a degree
	private void loadCourses(String degree) {
		List<Course> courses = CourseManager.getCoursesByDegree(degree);
		allCourseBoxes.clear();
		courseListContainer.getChildren().clear();

		// Loop through all courses and add to the container
		for (Course c : courses) {
			VBox box = createCourseBox(c);
			allCourseBoxes.add(box);
			courseListContainer.getChildren().add(box);
		}
	}

    // Creates UI item for one course (subject)
	private VBox createCourseBox(Course c) {
		VBox box = new VBox(5);
		box.getStyleClass().add("course-box");

		Label code = new Label(c.getCourseCode());
		code.getStyleClass().add("course-code");

		Label title = new Label(c.getCourseTitle());
		title.getStyleClass().add("course-title");

		Label units = new Label(c.getUnits() + " units");
		units.getStyleClass().add("course-units");

		Text desc = new Text(c.getDescription());
		desc.getStyleClass().add("course-description");
		desc.setWrappingWidth(820); // Wrap text for presentability

		// Prerequisites
		List<String> prereq = c.getPrerequisites();
		Label prereqLabel = new Label(
				prereq.isEmpty() ? "Prerequisites: None" : "Prerequisites: " + String.join(", ", prereq)
		);
		prereqLabel.getStyleClass().add("course-prereq");

		box.getChildren().addAll(code, title, units, desc, prereqLabel);
		return box;
	}
    
	// Resets the view to the original view
	public void resetView() {
		searchField.clear(); // Reset search      
		scrollPane.setVvalue(0); // Reset scroll
		courseListContainer.getChildren().setAll(allCourseBoxes); // Restore full course list
	}

	// Getters
	public String getDegree() {
		return degree;
	}
}
