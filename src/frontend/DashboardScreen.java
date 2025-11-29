package frontend;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class DashboardScreen {

    private Stage stage;

    public DashboardScreen() {
        stage = new Stage();
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/resources/logo.png"))); // Uploading Kurasu Icon for app 
		stage.setTitle(" クラス | Kurasu"); // Sets the title to Kurasu
        
    }

    public void show() {

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #ffe5ec;");

        // Sidebar
        VBox sidebar = new VBox(10);
        sidebar.setPadding(new Insets(20));
        sidebar.setPrefWidth(230);
        sidebar.setStyle("-fx-background-color: white; -fx-border-color: #ffc2d1;");

        Label dash = new Label("Dashboard");
        Label enlist = new Label("Enlistment");

        // Course List with dropdown
        VBox courseListBox = new VBox(5);
        Label courseHeader = new Label("Course List ▼");

        VBox courseDropdown = new VBox();
        courseDropdown.setPadding(new Insets(0, 0, 0, 20));
        courseDropdown.getChildren().addAll(
                new Label("- BS Computer Science"),
                new Label("- MS Computer Science"),
                new Label("- MIT"),
                new Label("- PHD")
        );
        courseDropdown.setVisible(false); // Hide 

        courseHeader.setOnMouseClicked(e -> {
            courseDropdown.setVisible(!courseDropdown.isVisible());
        });

        courseListBox.getChildren().addAll(courseHeader, courseDropdown);

        Label about = new Label("ℹ About");
        Label credits = new Label("⭐ Credits");

        sidebar.getChildren().addAll(dash, enlist, courseListBox, about, credits);

        // Hamburer Icon
        Button menuBtn = new Button("☰");
        menuBtn.setStyle("-fx-font-size: 24px; -fx-background-color: transparent;");
        menuBtn.setOnAction(e -> sidebar.setVisible(!sidebar.isVisible()));

        // Top Bar
        HBox topBar = new HBox(menuBtn);
        topBar.setPadding(new Insets(10));
        root.setTop(topBar);

        // Content
        VBox content = new VBox(20);
        content.setAlignment(Pos.CENTER);

        Button logoutBtn = new Button("Log Out");
        logoutBtn.setStyle("-fx-background-color: #fb6f92; -fx-text-fill: white; -fx-font-size: 18px;");
        logoutBtn.setOnAction(e -> {
            stage.close();
            System.exit(0);
        });

        content.getChildren().add(logoutBtn);

        // Place regions
        root.setLeft(sidebar);
        root.setCenter(content);

        Scene scene = new Scene(root, 1000, 700);
        stage.setScene(scene);
        stage.show();
    }
}
