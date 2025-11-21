package application;

import backend.OfferedCourseManager;
import backend.Course;
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

public class CourseSelectionScreen {
	private Stage ownerStage;
	private Student student;
	private Map<String, List<String>> prerequisites = new HashMap<>();

	public CourseSelectionScreen(Stage ownerStage, Student student) {
		this.ownerStage = ownerStage;
		this.student = student;
		setupPrerequisites(); // define all prereqs here
	}

	private void setupPrerequisites() {
		// BSCS
		prerequisites.put("CMSC 21", List.of("CMSC 12"));
		prerequisites.put("CMSC 57", List.of("CMSC 56"));

		prerequisites.put("CMSC 22", List.of("CMSC 12"));
		prerequisites.put("CMSC 123", List.of("CMSC 21", "CMSC 57"));
		prerequisites.put("CMSC 130", List.of("CMSC 57"));
		prerequisites.put("CMSC 150", List.of("CMSC 21"));

		prerequisites.put("CMSC 23", List.of("CMSC 22"));
		prerequisites.put("CMSC 100", List.of("CMSC 22"));
		prerequisites.put("CMSC 127", List.of("CMSC 22"));
		prerequisites.put("CMSC 131", List.of("CMSC 21"));

		prerequisites.put("CMSC 132", List.of("CMSC 131"));
		prerequisites.put("CMSC 124", List.of("CMSC 123"));
		prerequisites.put("CMSC 125", List.of("CMSC 123"));
		prerequisites.put("CMSC 141", List.of("CMSC 123"));
		prerequisites.put("CMSC 170", List.of("CMSC 123"));

		prerequisites.put("CMSC 128", List.of("CMSC 123"));
		prerequisites.put("CMSC 142", List.of("CMSC 123"));
		prerequisites.put("CMSC 137", List.of("CMSC 123"));
		prerequisites.put("CMSC 173", List.of("CMSC 123"));
		prerequisites.put("CMSC 180", List.of("CMSC 132"));

		// MSCS
		prerequisites.put("CMSC 245", List.of("CMSC 244"));

		// MIT
		prerequisites.put("IT 227", List.of("IT 210", "IT 226"));
		prerequisites.put("IT 280", List.of("IT 238"));
	}

	public void show() {
		Stage popup = new Stage();
		popup.initOwner(ownerStage);
		popup.initModality(Modality.APPLICATION_MODAL);
		popup.setResizable(false);
		popup.setTitle("Sign-Up");

		popup.getIcons().add(new Image(getClass().getResourceAsStream("/resources/logo.png")));

		// Prevent the user from closing the popup manually
		popup.setOnCloseRequest(event -> {
			event.consume(); // ignores the close button
		});

		// Root
		BorderPane root = new BorderPane();
		root.setPadding(new Insets(25));
		root.setStyle("-fx-background-color: #ffc2d1;");

		// WHITE FORM CONTAINER
		VBox form = new VBox(20);
		form.setAlignment(Pos.TOP_LEFT);
		form.setPadding(new Insets(30));
		form.setStyle("-fx-background-color: rgba(255,255,255,0.95); -fx-background-radius: 20;");

		// CENTER THE FORM
		root.setCenter(form);

		// FONTS
		Font poppinsBold = Font.loadFont(getClass().getResourceAsStream("/resources/Poppins Bold.ttf"), 32);
		Font inter = Font.loadFont(getClass().getResourceAsStream("/resources/Inter.ttf"), 14);
		Font interItalic = Font.loadFont(getClass().getResourceAsStream("/resources/Inter Italic.ttf"), 14);

		// TITLE + SUBTITLE
		Label title = new Label("Sign-Up");
		title.setFont(poppinsBold);

		// subtitle as TextFlow to maintain styling parity
		Text subtitleText1 = new Text("Check the courses you have completed as a " + student.getDegree() + " student.");
		subtitleText1.setFont(interItalic);
		subtitleText1.setFill(Color.web("#444444"));
		TextFlow subtitle1 = new TextFlow(subtitleText1);

		Text subtitleText2 = new Text("If you haven't completed any courses yet, you may continue without selecting anything.");
		subtitleText2.setFont(interItalic);
		subtitleText2.setFill(Color.web("#6a6a6a"));
		TextFlow subtitle2 = new TextFlow(subtitleText2);

		// Load courses...
		List<Course> allCourses = new ArrayList<>(OfferedCourseManager.loadCourses().values());
		List<Course> coursesByDegree = new ArrayList<>();
		for (Course c : allCourses) {
			if (c.getType().equalsIgnoreCase(student.getDegree())) {
				coursesByDegree.add(c);
			}
		}

		// Keep a master list of CheckBoxes and a mapping from CheckBox -> Course
		ObservableList<CheckBox> courseCheckboxes = FXCollections.observableArrayList();
		Map<CheckBox, Course> cbToCourse = new HashMap<>();

		for (Course c : coursesByDegree) {
			CheckBox cb = new CheckBox(c.getCourseCode());
			cb.setFont(inter);
			cb.setStyle("-fx-padding: 5 0 5 0;");
			cb.selectedProperty().addListener((obs, w, s) -> handlePrerequisites(courseCheckboxes));
			courseCheckboxes.add(cb);
			cbToCourse.put(cb, c);
		}

		// Ensure prerequisites logic runs initially
		handlePrerequisites(courseCheckboxes);

		// Search Bar
		TextField searchField = new TextField();
		searchField.setPromptText("Search courses...");
		searchField.setPrefWidth(380);
		searchField.setFont(inter);
		searchField.setStyle(
			"-fx-font-family: 'Inter';" +
			"-fx-font-size: 14px;" +
			"-fx-background-radius: 8;" +
			"-fx-padding: 6 10 6 10;" +
			"-fx-border-radius: 8;"
		);

		// FilteredList wraps the master observable list
		FilteredList<CheckBox> filteredList = new FilteredList<>(courseCheckboxes, p -> true);

		// Live filter as user types -> filter by course code OR course title (case-insensitive)
		searchField.textProperty().addListener((obs, oldVal, newVal) -> {
			String filter = (newVal == null) ? "" : newVal.toLowerCase().trim();
			if (filter.isEmpty()) {
				filteredList.setPredicate(cb -> true);
			} else {
				filteredList.setPredicate(cb -> {
					Course c = cbToCourse.get(cb);
					if (c == null) return false;
					String code = c.getCourseCode() == null ? "" : c.getCourseCode().toLowerCase();
					String titleStr = c.getCourseTitle() == null ? "" : c.getCourseTitle().toLowerCase();
					return code.contains(filter) || titleStr.contains(filter);
				});
			}
		});

		// === LISTVIEW (uses filtered list) ===
		ListView<CheckBox> listView = new ListView<>(filteredList);
		listView.setPrefHeight(350);
		listView.setStyle(
			"-fx-border-radius: 8; " +
			"-fx-background-radius: 8; " +
			"-fx-padding: 6;"
		);

		// Custom cell to show checkbox + courseTitle (units shown below code)
		listView.setCellFactory(lv -> new ListCell<CheckBox>() {
			@Override
			protected void updateItem(CheckBox cb, boolean empty) {
				super.updateItem(cb, empty);

				if (empty || cb == null) {
					setGraphic(null);
					setText(null);
					return;
				}

				Course course = cbToCourse.get(cb);

				// Checkbox + course code
				VBox container = new VBox(2);
				container.setPadding(new Insets(6, 4, 6, 4));

				// Ensure the checkbox node is the same instance (keeps selection state)
				cb.setFont(inter);
                cb.setMaxWidth(Double.MAX_VALUE);

                container.getChildren().add(cb);

                if (course != null) {
                	// show ONLY title + units
                	String title = course.getCourseTitle();
                	int units = course.getUnits();

                	Label details = new Label(title + " (" + units + " units)");
                	details.setStyle("-fx-font-size: 12px; -fx-text-fill: #555;");
                	details.setFont(inter);
                	details.setWrapText(true);

                	container.getChildren().add(details);
                }

                setGraphic(container);
			}
		});

		// Continue button
		Button continueBtn = new Button("Continue");
		continueBtn.setPrefWidth(300);
		continueBtn.setFont(Font.font("Inter", 20));
		continueBtn.setStyle(
				"-fx-background-color: #fb6f92;" + // Main color of the button (darker pink)
				"-fx-background-radius: 8;" +
				"-fx-text-fill: #ffe5ec;" +
				"-fx-font-size: 20px;" +
				"-fx-font-weight: bold;"
		);

		continueBtn.setOnMouseEntered(e ->
		continueBtn.setStyle(
			"-fx-background-color: #ff85ac;" + // Main color of the button (darker pink)
			"-fx-background-radius: 8;" +
			"-fx-text-fill: #ffe5ec;" +
			"-fx-font-size: 20px;" + // Font size 26
			"-fx-font-weight: bold;"
		)
	);

		continueBtn.setOnMouseExited(e ->
			continueBtn.setStyle(
				"-fx-background-color: #fb6f92;" + // Main color of the button (darker pink)
				"-fx-background-radius: 8;" +
				"-fx-text-fill: #ffe5ec;" +
				"-fx-font-size: 20px;" + // Font size 26
				"-fx-font-weight: bold;"
			)
		);

		continueBtn.setOnAction(e -> {
			List<Course> selectedCourses = new ArrayList<>();
			for (CheckBox cb : courseCheckboxes) {
				if (cb.isSelected()) {
					Course c = cbToCourse.get(cb);
					if (c != null) selectedCourses.add(c);
				}
			}

			student.getCompletedCourses().clear();
			student.getCompletedCourses().addAll(selectedCourses);

			// save updated student list
			StudentManager studentManager = StudentManager.load(Path.of("students.txt")); // load current manager
			if (studentManager != null) {
				studentManager.updateStudent(student); // save courses to the file
			}

			showToast(ownerStage, "Sign-up complete! You may now log in.");
			popup.close(); // close only the course selection, NOT the welcome screen
		});

		// ASSEMBLE FORM: title, subtitle, searchField, listView, continue button
		form.getChildren().addAll(title, subtitle1, subtitle2, searchField, listView, continueBtn);

		Scene scene = new Scene(root, 600, 620);
		popup.setScene(scene);
		popup.show();
	}

	private void handlePrerequisites(ObservableList<CheckBox> checkboxes) {
		for (CheckBox cb : checkboxes) {
			String code = cb.getText();
			List<String> prereqs = prerequisites.get(code);

			if (prereqs == null || prereqs.isEmpty()) {
				cb.setDisable(false); // no prerequisites, always enabled
			} else {
				// check if all prereqs are selected
				boolean allSatisfied = prereqs.stream().allMatch(prereqCode ->
					checkboxes.stream()
						.anyMatch(c -> c.getText().equals(prereqCode) && c.isSelected())
				);

				cb.setDisable(!allSatisfied);

				// if disabled, also unselect it
				if (!allSatisfied) cb.setSelected(false);
			}
		}
	}

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

		toast.setScene(scene);
		toast.setX(owner.getX() + owner.getWidth()/2 - 150);
		toast.setY(owner.getY() + owner.getHeight() - 80);
		toast.show();

		// Fade out
		FadeTransition fade = new FadeTransition(Duration.seconds(5), root);
		fade.setFromValue(1.0);
		fade.setToValue(0.0);
		fade.setOnFinished(e -> toast.close());
		fade.play();
	}
}

