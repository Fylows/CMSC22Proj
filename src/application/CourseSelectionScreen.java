package application;

import backend.OfferedCourseManager;
import backend.Course;
import backend.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.*;

public class CourseSelectionScreen {

    private Stage popupStage;
    private Student student;
    private Map<String, List<String>> prerequisites = new HashMap<>();

    public CourseSelectionScreen(Stage popupStage, Student student) {
        this.popupStage = popupStage;
        this.student = student;
        setupPrerequisites(); // define all prereqs here
    }

    private void setupPrerequisites() {
        // Example: single prereq
        prerequisites.put("CMSC 21", List.of("CMSC 12"));
        prerequisites.put("CMSC 57", List.of("CMSC 56"));
        
        prerequisites.put("CMSC 22", List.of("CMSC 12"));
        prerequisites.put("CMSC 123", List.of("CMSC 21", "CMSC 57"));
        prerequisites.put("CMSC 130", List.of("CMSC 57"));
        prerequisites.put("CMSC 150", List.of("CMSC 21"));
        
        // Example: multiple prereqs
        
        // Add all your other prerequisites here
    }

    public void show() {
        VBox root = new VBox(10);
        root.setStyle("-fx-padding: 20;");

        Label title = new Label("Select Completed Courses (" + student.getDegree() + ")");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Load courses for the student's degree
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
            cb.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
                handlePrerequisites(courseCheckboxes);
            });
            courseCheckboxes.add(cb);
        }

        // Initially disable courses with unmet prerequisites
        handlePrerequisites(courseCheckboxes);

        ListView<CheckBox> listView = new ListView<>(courseCheckboxes);
        listView.setPrefHeight(300);

        Button continueBtn = new Button("Continue");
        continueBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");

        continueBtn.setOnAction(e -> {
            // Collect selected courses
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

            // Save to student's completed courses
            student.getCompletedCourses().clear();
            student.getCompletedCourses().addAll(selectedCourses);

            // Show success alert
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Sign-up complete! You can now log in.");
            alert.showAndWait();

            // Close popup and return to welcome/login
            popupStage.close();
        });

        root.getChildren().addAll(title, listView, continueBtn);

        Scene scene = new Scene(root, 600, 400);
        popupStage.setScene(scene);
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
