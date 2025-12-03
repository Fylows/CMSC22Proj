package frontend;

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
import javafx.scene.text.*;

public class ContentArea implements ScreenChangeListener {
	private final Student student;
    private final StudentManager manager;

    // Fonts
    private final Font poppinsBold;
    private final Font inter;
    private final Font interItalic;
    private final Font japaneseFont;

    // Stage & layout
    private final Stage stage;
    private final StackPane screens;
    private final VBox dashboardScreen;
    private final ScrollPane enlistmentScroll;
    private final EnlistmentScreen enlistmentScreen; // your existing class
    private final HBox topBar;
    private final Sidebar sidebar;
    private final VBox content; // topbar + screens container
    private boolean sidebarVisible = false;

    // external hamburger in topbar
    private final javafx.scene.control.Button externalHamburger;

    public ContentArea(Student student, StudentManager manager) {
        this.student = student;
        this.manager = manager;

        poppinsBold = Font.loadFont(getClass().getResourceAsStream("/cssFiles/Poppins Bold.ttf"), 28);
        inter = Font.loadFont(getClass().getResourceAsStream("/cssFiles/Inter.ttf"), 25);
        interItalic = Font.loadFont(getClass().getResourceAsStream("/cssFiles/Inter Italic.ttf"), 14);
        japaneseFont = Font.loadFont(getClass().getResourceAsStream("/cssFiles/Seibi Ohkido.otf"), 25);

        // Stage
        stage = new Stage();
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/resources/logo.png")));
        stage.setTitle(" クラス | Kurasu");

        // Create top bar (uses externalHamburger)
        externalHamburger = new javafx.scene.control.Button("☰");
        externalHamburger.setStyle("-fx-font-size: 24px; -fx-background-color: transparent;");
        topBar = createTopbar();

        // Create screens
        dashboardScreen = new DashboardScreen(student, inter, poppinsBold, japaneseFont, interItalic);
        enlistmentScreen = new EnlistmentScreen(); // keep your implementation
        enlistmentScroll = new ScrollPane(enlistmentScreen);
        enlistmentScroll.setFitToWidth(true);
        enlistmentScroll.setFitToHeight(true);
        enlistmentScroll.setStyle("-fx-background-color: transparent;");

        // StackPane that holds screens (only one visible at a time)
        screens = new StackPane(dashboardScreen, enlistmentScroll);
        StackPane.setAlignment(dashboardScreen, Pos.CENTER);
        StackPane.setAlignment(enlistmentScroll, Pos.CENTER);
        // Ensure both fill area
        enlistmentScroll.prefWidthProperty().bind(screens.widthProperty());
        enlistmentScroll.prefHeightProperty().bind(screens.heightProperty());

        // Content VBox (topbar + screens)
        content = new VBox(5, topBar, screens);
        content.setPadding(new Insets(20));
        VBox.setVgrow(screens, Priority.ALWAYS);

        // Create sidebar and pass this ContentArea as listener
        this.sidebar = new Sidebar(inter, poppinsBold, japaneseFont, this);
        // Sidebar starts hidden by default (Sidebar constructor setTranslateX(-320))

        // Setup initial visibility: dashboard visible, others hidden
        changeScreen("Dashboard");

        // Wire up hamburger buttons and outside-click logic
        setupSidebarToggleLogic();

        // Build scene and stage when show() is called
    }

    private HBox createTopbar() {
        HBox bar = new HBox();
        bar.setSpacing(20);
        bar.setAlignment(Pos.CENTER_LEFT);

        // externalHamburger already created in constructor
        // style is set there; but ensure no duplicate static reference
        // Title
        Label title = new Label("Dashboard");
        title.setFont(poppinsBold);
        title.setStyle("-fx-font-size: 32; -fx-font-weight: bold;");

        Region space = new Region();
        HBox.setHgrow(space, Priority.ALWAYS);

        // Profile section
        Label nameLabel = new Label(student.getName());
        nameLabel.setFont(Font.font(poppinsBold.getFamily(), 22));

        Label emailLabel = new Label(student.getEmail());
        emailLabel.setFont(interItalic);

        VBox profileTexts = new VBox(nameLabel, emailLabel);
        profileTexts.setAlignment(Pos.CENTER_RIGHT);

        ImageView profilePic = new ImageView(new Image(getClass().getResourceAsStream("/resources/logo.png")));
        profilePic.setFitHeight(50);
        profilePic.setFitWidth(50);
        profilePic.setClip(new Circle(25));
        
//        profilePic.setOnMouseClicked(e -> {
//            onScreenChange("Profile");
//        });

        HBox profileBox = new HBox(profileTexts, profilePic);
        profileBox.setSpacing(10);
        profileBox.setAlignment(Pos.CENTER_RIGHT);

        bar.getChildren().addAll(externalHamburger, title, space, profileBox);
        return bar;
    }

    private void setupSidebarToggleLogic() {
        // Toggle when external hamburger is clicked
        externalHamburger.setOnAction(e -> toggleSidebar());

        // Toggle when internal hamburger (inside sidebar) is clicked
        sidebar.getInternalHamburger().setOnAction(e -> toggleSidebar());

        // Clicking outside the sidebar closes it (only when visible)
        // We will add a scene-level filter in show() (after scene is created) to check clicks
        // But we'll also provide a utility that toggles with animation
    }

    private void toggleSidebar() {
        // Animate sidebar in/out; also make content mouse transparent while open
        double targetX = sidebarVisible ? -320 : 0;
        TranslateTransition anim = new TranslateTransition(Duration.millis(260), sidebar.getSidebar());
        anim.setToX(targetX);
        anim.play();

        // Block content interaction when sidebar is visible
        content.setMouseTransparent(!sidebarVisible); // if currently hidden, after opening we want it to be transparent
        sidebarVisible = !sidebarVisible;
    }

    // Called by Sidebar via ScreenChangeListener
    @Override
    public void onScreenChange(String screenName) {
        changeScreen(screenName);
        // When user selects a screen from sidebar, close the sidebar (if open)
        if (sidebarVisible) toggleSidebar();
    }

    // Centralized screen switching
    public void changeScreen(String screen) {
        switch(screen) {
            case "Dashboard":
                dashboardScreen.setVisible(true);
                enlistmentScroll.setVisible(false);
                break;
            case "Enlistment":
                dashboardScreen.setVisible(false);
                enlistmentScroll.setVisible(true);
                break;
                
        }
    }

    // Call this to show the whole UI (this matches your login flow: new ContentArea(...).show();)
    public void show() {
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: white;");

        // Place content and sidebar into root
        root.getChildren().addAll(content, sidebar.getSidebar());
        // Ensure sidebar is aligned left
        StackPane.setAlignment(sidebar.getSidebar(), Pos.CENTER_LEFT);

        // Make content be on bottom so sidebar overlays it
        sidebar.getSidebar().toFront();
        topBar.setPickOnBounds(false);
        sidebar.getSidebar().setPickOnBounds(false);

        Scene scene = new Scene(root, 1200, 800);

        // Register a global click handler to close sidebar when clicking outside
        scene.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            if (!sidebarVisible) return;

            Bounds bounds = sidebar.getSidebar().localToScene(sidebar.getSidebar().getBoundsInLocal());
            double x = event.getSceneX();
            double y = event.getSceneY();

            if (!bounds.contains(x, y)) {
                // Clicked outside
                toggleSidebar();
            }
        });

        stage.setScene(scene);
        stage.show();
    }
}

//	private Student student;
//	private StudentManager manager;
//	
//	Font poppinsBold;
//	Font inter;
//	Font interItalic;
//	Font japaneseFont;
//	
//    private Stage stage;
//	static Button externalHamburger = new Button();    
//    private boolean hidden = true;
//
//    // -- Different screens
//    private HBox topBar;
//	private VBox dashboardScreen;
//	private EnlistmentScreen enlistmentScreen;	
////	private VBox courseList;
////	private VBox about;	
////	private VBox credits;
//		
//	private StackPane screens;
//	private ScrollPane enlistmentScroll;
//	
//    public ContentArea(Student student, StudentManager manager) { 
//        this.student = student;
//        this.manager = manager;
//    	
//        poppinsBold = Font.loadFont(getClass().getResourceAsStream("/cssFiles/Poppins Bold.ttf"), 28);
//        inter = Font.loadFont(getClass().getResourceAsStream("/cssFiles/Inter.ttf"), 25);
//        interItalic = Font.loadFont(getClass().getResourceAsStream("/cssFiles/Inter Italic.ttf"), 14);
//        japaneseFont = Font.loadFont(getClass().getResourceAsStream("/cssFiles/Seibi Ohkido.otf"), 25);
//        
//    	stage = new Stage();
//        stage.getIcons().add(new Image(getClass().getResourceAsStream("/resources/logo.png"))); // Uploading Kurasu Icon for app 
//		stage.setTitle(" クラス | Kurasu"); // Sets the title to Kurasu
//		
//		topBar = createTopbar();
//        
//	    dashboardScreen = new DashboardScreen(student, inter, poppinsBold, japaneseFont, interItalic);
//	    enlistmentScreen = new EnlistmentScreen();
//		    enlistmentScroll = new ScrollPane(enlistmentScreen);
//		    enlistmentScroll.setFitToWidth(true);
//		    enlistmentScroll.setFitToHeight(true);
//		    enlistmentScroll.setStyle("-fx-background-color: transparent;");
////		courseList = new CourseListScreen();
////		about = new AboutSceen();	
////		credits = new CreditsSceen();
//	    
//	    // STACK PANE LAYERS
//	    screens = new StackPane(enlistmentScroll, dashboardScreen);
//	    
//	    // Binds enlistment scroll size to stack pane
//	    enlistmentScroll.prefWidthProperty().bind(screens.widthProperty());
//	    enlistmentScroll.prefHeightProperty().bind(screens.heightProperty());
//	    
// 
//    }
//    
//
//    private void toggleSidebar(Pane sidebar, Pane content) {
//        boolean opening = sidebar.getTranslateX() != 0;
//
//        TranslateTransition anim = new TranslateTransition(Duration.millis(300), sidebar);
//        anim.setToX(opening ? 0 : -320);
//        anim.play();
//
//        content.setMouseTransparent(opening);
//    }
//
//    public void show() {
//    	 StackPane root = new StackPane();
//         root.setStyle("-fx-background-color: white;");
//
//    	 dashboardScreen.setVisible(true);
//    	 enlistmentScroll.setVisible(false);
//    	 
//         // Create sidebar object
//         Sidebar side = new Sidebar(inter, poppinsBold, japaneseFont, dashboardScreen, enlistmentScroll);
//         VBox sidebar = side.getSidebar();
//         
//         sidebar.setTranslateX(-320);
//         StackPane.setAlignment(sidebar, Pos.CENTER_LEFT);
//         
//         // Add topbar, sidebar, and the screens that will be changing
//         VBox content = new VBox(5, topBar, screens);
//         content.setPadding(new Insets(20));
//         VBox.setVgrow(screens, Priority.ALWAYS);
//
//         root.getChildren().addAll(content, sidebar);
//         sidebar.toFront();
//         topBar.setPickOnBounds(false);
//         sidebar.setPickOnBounds(false);
//
//         
//         // Toggle logic
//         externalHamburger.setOnAction(e -> {
//        	 toggleSidebar(sidebar, this.dashboardScreen);
//        	 hidden = false;
//         });
//         side.getInternalHamburger().setOnAction(e -> {
//        	 toggleSidebar(sidebar, this.dashboardScreen); 
//        	 hidden = true;
//         });
//
//    	 root.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
//    		if (!hidden) {
//    			Bounds bounds = sidebar.localToScene(sidebar.getBoundsInLocal());
//          	    double x = event.getSceneX();
//          	    double y = event.getSceneY();
//
//          	    if (!bounds.contains(x, y)) {
//          	        System.out.println("Clicked outside the sidebar!");
//          	        // Optionally, hide sidebar here
//          	        toggleSidebar(sidebar, dashboardScreen);
//          	        hidden = true;
//          	    }
//    		}
//      	    
//      	});
//
//    	 
//
//        
//         
//         Scene scene = new Scene(root, 1200, 800);
//         stage.setScene(scene);
//         stage.show();
//         
//    }
//    
//    /***** TOP BAR: DASHBOARD PROFILE *****/
//	private HBox createTopbar() {
//		HBox bar = new HBox();
//        bar.setSpacing(20);
//        bar.setAlignment(Pos.CENTER_LEFT);
//
//        // Hamburger Button (icon can be changed)
//        externalHamburger = new Button("☰");
//        externalHamburger.setStyle("-fx-font-size: 24px; -fx-background-color: transparent;");
//
//        Label title = new Label("Dashboard");
//        title.setFont(poppinsBold);
//        title.setStyle(
//        		"-fx-font-size: 32; " + 
//        		"-fx-font-weight: bold;");
//
//        Region space = new Region();
//        HBox.setHgrow(space, Priority.ALWAYS);
//
//        // Profile Section
//        Label nameLabel = new Label(student.getName());
//        nameLabel.setFont(Font.font(poppinsBold.getFamily(), 22));
//        
//        Label emailLabel = new Label(student.getEmail());
//        emailLabel.setFont(interItalic);
//
//        VBox profileTexts = new VBox(nameLabel, emailLabel);
//        profileTexts.setAlignment(Pos.CENTER_RIGHT);
//	
//        ImageView profilePic = new ImageView(
//        	    new Image(getClass().getResourceAsStream("/resources/logo.png"))
//        );;
//        profilePic.setFitHeight(50);
//        profilePic.setFitWidth(50);
//        profilePic.setClip(new Circle(25));
//
////        HBox profileBox = new HBox(profileTexts, profilePic);
//        HBox profileBox = new HBox(profileTexts);
//        profileBox.setSpacing(10);
//        profileBox.setAlignment(Pos.CENTER_RIGHT);
//
//        bar.getChildren().addAll(externalHamburger, title, space, profileBox);
//        return bar;
//        
//	}
//
//}