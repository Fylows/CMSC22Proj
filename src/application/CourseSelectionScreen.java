package application;

import backend.OfferedCourseManager;
import backend.Course;
import backend.Student;
import backend.StudentManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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
        popup.initModality(null); // Or APPLICATION_MODAL if you want
        popup.setTitle("Course Selection");

        VBox root = new VBox(10);
        root.setStyle("-fx-padding: 20;");

        Label title = new Label("Select Completed Courses (" + student.getDegree() + ")");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Load courses...
        List<Course> allCourses = new ArrayList<>(OfferedCourseManager.loadCourses().values());
        List<Course> coursesByDegree = new ArrayList<>();
        for (Course c : allCourses) {
            if (c.getType().equalsIgnoreCase(student.getDegree())) {
                coursesByDegree.add(c);
            }
        }

        ObservableList<CheckBox> courseCheckboxes = FXCollections.observableArrayList();
        for (Course c : coursesByDegree) {
            CheckBox cb = new CheckBox(c.getCourseCode());
            cb.selectedProperty().addListener((obs, w, s) -> handlePrerequisites(courseCheckboxes));
            courseCheckboxes.add(cb);
        }

        handlePrerequisites(courseCheckboxes);

        ListView<CheckBox> listView = new ListView<>(courseCheckboxes);
        listView.setPrefHeight(300);

        Button continueBtn = new Button("Continue");
        continueBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");

        continueBtn.setOnAction(e -> {
            List<Course> selectedCourses = new ArrayList<>();
            for (CheckBox cb : courseCheckboxes) {
                if (cb.isSelected()) {
                    Course c = coursesByDegree.stream()
                            .filter(course -> course.getCourseCode().equals(cb.getText()))
                            .findFirst()
                            .orElse(null);
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

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Sign-up complete! You can now log in.");
            alert.showAndWait();

            popup.close(); // close only the course selection, NOT the welcome screen
        });

        root.getChildren().addAll(title, listView, continueBtn);

        Scene scene = new Scene(root, 600, 400);
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

}
