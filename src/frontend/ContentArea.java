package frontend;

import backend.OfferedCourse;
import backend.Student;
import backend.StudentManager;
import javafx.util.Duration;
import javafx.animation.TranslateTransition;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class ContentArea implements ScreenChangeListener {
	private static Student student = null;
	private final StudentManager manager;

	// Stage and layout screens
	private final Stage stage;
	private final StackPane screens;
	private final DashboardScreen dashboardScreen;
	
	private final EnlistmentScreen enlistmentScreen;
	private final ScrollPane enlistmentScroll;
	
	private final ProgramCoursesScreen bsScreen;
	private final ScrollPane bsScroll;
	
	private final ProgramCoursesScreen msScreen;
	private final ScrollPane msScroll;
	
	private final ProgramCoursesScreen mitScreen;
	private final ScrollPane mitScroll;
	
	private final ProgramCoursesScreen phdScreen;
	private final ScrollPane phdScroll;
	
	private final AboutScreen aboutScreen;
	private final CreditsScreen creditsScreen;
	
	private final ProfilePageScreen profileScreen;
	private final HBox topBar;
	private final Sidebar sidebar;
	private final VBox content; // Top Bar + Screen containers
	private boolean sidebarVisible = false;
	
	private final Button externalHamburger; // Hamburger button
	private Label topBarTitle; // Top Bar Titles

	// Constructor
	public ContentArea(Student student, StudentManager manager) {
		ContentArea.student = student;
		this.manager = manager;

		// Stage setting
		this.stage = new Stage();
		stage.getIcons().add(new Image(getClass().getResourceAsStream("/resources/logo.png"))); // Setting the Kurasu Icon
		stage.setTitle(" クラス | Kurasu");

		// Top Bar
		ImageView hamburgerIcon = new ImageView(new Image(getClass().getResourceAsStream("/resources/hamburgerIcon.png"))); // Hamburger Icon
		hamburgerIcon.setFitWidth(28);
		hamburgerIcon.setFitHeight(28);

		externalHamburger = new Button();
		externalHamburger.getStyleClass().add("hamburger-btn");
		externalHamburger.setGraphic(hamburgerIcon);

		topBar = createTopbar(); // Creates the Top Bar

		// Screens (Dashboard, Enlistment, Course, About, Credits)
		dashboardScreen = new DashboardScreen(student, this); 

		enlistmentScreen = new EnlistmentScreen();
		enlistmentScroll = new ScrollPane(enlistmentScreen);
		enlistmentScroll.setFitToWidth(true);
		enlistmentScroll.setFitToHeight(true);
		enlistmentScroll.getStyleClass().add("transparent-scroll");
		
		bsScreen = new ProgramCoursesScreen("BSCS");
		bsScroll = new ScrollPane(bsScreen);
		bsScroll.setFitToWidth(true);
		bsScroll.setFitToHeight(true);
		bsScroll.getStyleClass().add("transparent-scroll");

		msScreen = new ProgramCoursesScreen("MSCS");
		msScroll = new ScrollPane(msScreen);
		msScroll.setFitToWidth(true);
		msScroll.setFitToHeight(true);
		msScroll.getStyleClass().add("transparent-scroll");

		mitScreen = new ProgramCoursesScreen("MSIT");
		mitScroll = new ScrollPane(mitScreen);
		mitScroll.setFitToWidth(true);
		mitScroll.setFitToHeight(true);
		mitScroll.getStyleClass().add("transparent-scroll");

		phdScreen = new ProgramCoursesScreen("PHD");
		phdScroll = new ScrollPane(phdScreen);
		phdScroll.setFitToWidth(true);
		phdScroll.setFitToHeight(true);
		phdScroll.getStyleClass().add("transparent-scroll");
		
		aboutScreen = new AboutScreen();
		creditsScreen = new CreditsScreen();
		
		profileScreen = new ProfilePageScreen(student);

		// StackPane that holds screens (only one visible at a time)
		screens = new StackPane(
			dashboardScreen, 
			enlistmentScroll, 
			bsScroll, 
			msScroll, 
			mitScroll, 
			phdScroll, 
			aboutScreen, 
			creditsScreen, 
			profileScreen
		);
		
		StackPane.setAlignment(dashboardScreen, Pos.CENTER);
		StackPane.setAlignment(enlistmentScroll, Pos.CENTER);
		StackPane.setAlignment(bsScroll, Pos.CENTER);
		StackPane.setAlignment(msScroll, Pos.CENTER);
		StackPane.setAlignment(mitScroll, Pos.CENTER);
		StackPane.setAlignment(phdScroll, Pos.CENTER);
		StackPane.setAlignment(aboutScreen, Pos.CENTER);
		StackPane.setAlignment(creditsScreen, Pos.CENTER);
		StackPane.setAlignment(profileScreen, Pos.CENTER);

		// Ensures scrolling works properly
		enlistmentScroll.prefWidthProperty().bind(screens.widthProperty());
		enlistmentScroll.prefHeightProperty().bind(screens.heightProperty());

		// Content VBox (Top Bar + Screens)
		content = new VBox(5, topBar, screens);
		content.setPadding(new Insets(20));
		VBox.setVgrow(screens, Priority.ALWAYS);

		// Create Side Bar and pass this ContentArea as listener
		this.sidebar = new Sidebar(this); // Side Bar is hidden by default
		setupSidebarToggleLogic(); // Wire up hamburger buttons and outside-click logic
		
		changeScreen("Dashboard"); // Dashboard is the first screen to be seen, others are hidden
	}

	// Constructs the Top Bar
	private HBox createTopbar() {
		HBox bar = new HBox();
		bar.getStyleClass().add("topbar");
		
		bar.setPickOnBounds(false); 
	    bar.setPrefHeight(70);
	    bar.setMinHeight(70);
	    bar.setMaxHeight(70);
	    bar.setPrefWidth(Region.USE_COMPUTED_SIZE);
	    bar.setMaxWidth(Double.MAX_VALUE);

		// Font Styling for the texts
	    topBarTitle = new Label("Dashboard");
	    topBarTitle.getStyleClass().add("dashboard-title");

		Region space = new Region();
		HBox.setHgrow(space, Priority.ALWAYS);

		// Profile Section
		Label nameLabel = new Label(student.getName());
		nameLabel.getStyleClass().add("profile-name");

		Label emailLabel = new Label(student.getEmail());
		emailLabel.getStyleClass().add("profile-email");

		VBox profileTexts = new VBox(nameLabel, emailLabel);
		profileTexts.setAlignment(Pos.CENTER_RIGHT);

		ImageView profilePic = new ImageView(new Image(getClass().getResourceAsStream("/resources/defaultPFP.png")));
		profilePic.setFitHeight(50);
		profilePic.setFitWidth(50);
		profilePic.setPreserveRatio(true);

		// Correct clipping
		Circle clip = new Circle(25, 25, 25);
		profilePic.setClip(clip);

        HBox profileBox = new HBox(profilePic, profileTexts);
		profileBox.getStyleClass().add("profile-box");
        profileBox.setOnMouseClicked(e -> { onScreenChange("Profile"); });

		topBarTitle.setPickOnBounds(true);
	    profileBox.setPickOnBounds(true);
	    externalHamburger.setPickOnBounds(true);
		bar.getChildren().addAll(externalHamburger, topBarTitle, space, profileBox);

		return bar;
	}

	// Set ups the Side Bar and its toggle logic
	private void setupSidebarToggleLogic() {
		externalHamburger.setOnAction(e -> toggleSidebar()); // Toggle when external hamburger is clicked
		sidebar.getInternalHamburger().setOnAction(e -> toggleSidebar()); // Toggle when internal hamburger (inside sidebar) is clicked

		// Clicking outside the side bar closes it (only when visible)
    }

	// Helper method for toggling the side bar
	private void toggleSidebar() {
		double targetX;
		if (sidebarVisible) {
			targetX = -320; // Hide side bar by moving it left off-screen
		} else {
			targetX = 0; // Show side bar by moving it to its visible position
		}

		// Animates the side bar sliding in/out
		TranslateTransition animation = new TranslateTransition(Duration.millis(260), sidebar.getSidebar());
		animation.setToX(targetX); 
		animation.play();

		// Block content interaction when side bar is visible
		content.setMouseTransparent(!sidebarVisible); // If currently hidden, after opening we want it to be transparent
		sidebarVisible = !sidebarVisible;
	}

	// Called by Side Bar via ScreenChangeListener, changes the screen
	@Override
	public void onScreenChange(String screenName) {
		changeScreen(screenName);
		if (sidebarVisible) toggleSidebar(); // When user selects a screen from side bar, close the side bar (if open)
	}
	
	// Hides all screens by default
	private void hideAllScreens() {
		dashboardScreen.setVisible(false);
		enlistmentScroll.setVisible(false);
		bsScroll.setVisible(false);
		msScroll.setVisible(false);
		mitScroll.setVisible(false);
		phdScroll.setVisible(false);
		aboutScreen.setVisible(false);
		creditsScreen.setVisible(false);
		profileScreen.setVisible(false);
	}

	// Centralized screen switching
	public void changeScreen(String screen) {
		hideAllScreens();

		switch(screen) {
			case "Dashboard":
				dashboardScreen.setVisible(true);
				topBarTitle.setText("Dashboard");
				break;

			case "Enlistment":
				enlistmentScroll.setVisible(true);
				topBarTitle.setText("Enlistment Module");
				break;

			case "BSCS":
				bsScreen.resetView();
				bsScroll.setVisible(true);
				topBarTitle.setText("BS Computer Science Courses");
				break;

			case "MSCS":
				msScreen.resetView();
				msScroll.setVisible(true);
				topBarTitle.setText("MS Computer Science Courses");
				break;

			case "MSIT":
				mitScreen.resetView();
				mitScroll.setVisible(true);
				topBarTitle.setText("MS Information Technology Courses");
				break;

			case "PHD":
				mitScreen.resetView();
				phdScroll.setVisible(true);
				topBarTitle.setText("PhD in Computer Science Courses");
				break;
			
			case "About":
				aboutScreen.setVisible(true);
				topBarTitle.setText("About");
				break;

			case "Credits":
				creditsScreen.setVisible(true);
				topBarTitle.setText("Credits");
				break;

			case "Profile":
				profileScreen.setVisible(true);
				topBarTitle.setText("Profile Page");
				break;
		}
	}

	// Running the screen
	public void show() {
		for (OfferedCourse oc : this.student.getEnrolledCourses()) {
			System.out.println(oc);
		}
		StackPane root = new StackPane();
		root.getStyleClass().add("content-root");
		root.getChildren().addAll(content, sidebar.getSidebar(), topBar);

		// Anchor elements with correct layering
		StackPane.setAlignment(topBar, Pos.TOP_CENTER);
		StackPane.setAlignment(sidebar.getSidebar(), Pos.CENTER_LEFT);

		topBar.toFront();
		sidebar.getSidebar().toFront();

		Scene scene = new Scene(root, 1200, 800);
		scene.getStylesheets().add(getClass().getResource("/cssFiles/contentArea.css").toExternalForm()); // Add CSS Styling
		scene.getStylesheets().add(getClass().getResource("/cssFiles/sidebar.css").toExternalForm());
		scene.getStylesheets().add(getClass().getResource("/cssFiles/dashboard.css").toExternalForm());
		scene.getStylesheets().add(getClass().getResource("/cssFiles/programCourses.css").toExternalForm());

		// Close sidebar when clicking outside
		scene.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
			if (!sidebarVisible) return;

			Bounds bounds = sidebar.getSidebar().localToScene(sidebar.getSidebar().getBoundsInLocal());
			double x = event.getSceneX();
			double y = event.getSceneY();

			// If clicked outside
			if (!bounds.contains(x, y)) {
				toggleSidebar();
			}
		});

		stage.setScene(scene);
		stage.show();
	}

	// Getters
	public StudentManager getManager() {
		return manager;
	}
	
	public static Student getStudent() {
		return student;
	}
}