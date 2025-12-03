package frontend;

import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;

public class Sidebar {
	private VBox sidebar; // The whole sidebar UI
	private Button internalHamburger; // Hamburger button
	private ScreenChangeListener listener;

	// Constructor
	public Sidebar(ScreenChangeListener listener) {
		this.setListener(listener);
		
		sidebar = new VBox(10);
		sidebar.getStyleClass().add("sidebar-root");
		sidebar.setTranslateX(-320);   // Hidden by default
    	
		// Header
		HBox header = new HBox(10);
		header.getStyleClass().add("sidebar-header");
		
		// Top Bar
		ImageView hamburgerIcon = new ImageView(new Image(getClass().getResourceAsStream("/resources/hamburgerIcon.png"))); // Hamburger Icon
		hamburgerIcon.setFitWidth(28);
		hamburgerIcon.setFitHeight(28);

		internalHamburger = new Button();
		internalHamburger.getStyleClass().add("sidebar-hamburger");
		internalHamburger.setGraphic(hamburgerIcon);

		// Font Styling for the labels
		Label japKurasu = new Label("クラス");
		japKurasu.getStyleClass().add("sidebar-japanese-title");

		Label kurasu = new Label("| Kurasu");
		kurasu.getStyleClass().add("sidebar-english-title");

		header.getChildren().addAll(internalHamburger, japKurasu, kurasu);

		// Side bar Items
		HBox dashboardBox = createItem("Dashboard", "/resources/dashboardIcon.png");
		dashboardBox.setOnMouseClicked(event -> listener.onScreenChange("Dashboard"));
        
		HBox enlistmentBox = createItem("Enlistment", "/resources/enlistIcon.png");
		enlistmentBox.setOnMouseClicked(event -> listener.onScreenChange("Enlistment"));
        
		HBox aboutBox = createItem("About", "/resources/aboutIcon.png");
		HBox creditsBox = createItem("Credits", "/resources/creditsIcon.png");

        // Course List Drop Down
		HBox courseBox = createItem("Course List", "/resources/courseIcon.png");
        
		Label bs = new Label("BS Computer Science");
		bs.getStyleClass().add("sidebar-course-item");

		Label ms = new Label("MS Computer Science");
		ms.getStyleClass().add("sidebar-course-item");

		Label mit = new Label("MS Information Technology");
		mit.getStyleClass().add("sidebar-course-item");

		Label phd = new Label("PhD in Computer Science");
		phd.getStyleClass().add("sidebar-course-item");

		VBox courseDropdown = new VBox(bs, ms, mit, phd);
		courseDropdown.getStyleClass().add("sidebar-course-dropdown");
		courseDropdown.setVisible(false);
		courseDropdown.setManaged(false);

		ImageView dropdownIcon = new ImageView(new Image(getClass().getResourceAsStream("/resources/dropdownIcon.png")));
		dropdownIcon.setFitWidth(30);
		dropdownIcon.setPreserveRatio(true);

		courseBox.getChildren().add(dropdownIcon);
		courseBox.setOnMouseClicked(event -> {
			boolean open = !courseDropdown.isVisible();
			courseDropdown.setVisible(open);
			courseDropdown.setManaged(open);
			dropdownIcon.setRotate(open ? 180 : 0);
		});

		VBox courseListBox = new VBox(courseBox, courseDropdown);

		// Spacer
		Region spacer = new Region();
		VBox.setVgrow(spacer, Priority.ALWAYS);

		// Logout Button
		Button logoutBtn = new Button("Log Out");
		logoutBtn.getStyleClass().add("sidebar-logout");
		logoutBtn.setOnAction(e -> {System.exit(0);}); // Closes the stage

		HBox logoutWrapper = new HBox(logoutBtn);
		logoutWrapper.getStyleClass().add("sidebar-logout-wrapper");

		sidebar.getChildren().addAll(header, dashboardBox, enlistmentBox, courseListBox, aboutBox, creditsBox, spacer, logoutWrapper);
	}

	private HBox createItem(String title, String iconPath) {
		ImageView icon = new ImageView(new Image(getClass().getResourceAsStream(iconPath)));
		icon.setFitWidth(25);
		icon.setFitHeight(25);

		Label label = new Label(title);
		label.getStyleClass().add("sidebar-label");

		HBox box = new HBox(15, icon, label);
		box.getStyleClass().add("sidebar-item");
        
		return box;
	}

	// Getters and Setters
	public VBox getSidebar() {
		return sidebar;
	}

	public Button getInternalHamburger() {
		return internalHamburger;
	}

	public ScreenChangeListener getListener() {
		return listener;
	}

	public void setListener(ScreenChangeListener listener) {
		this.listener = listener;
	}	
}
