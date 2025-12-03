package frontend;

import backend.Student;
import backend.StudentManager;
import javafx.util.Duration;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.scene.text.*;

public class ContentArea {
	private Student student;
	private StudentManager manager;
	
	Font poppinsBold;
	Font inter;
	Font interItalic;
	Font japaneseFont;
	
    private Stage stage;
	static Button externalHamburger = new Button();    

    // -- Different screens
    private HBox topBar;
	private VBox dashboardScreen;
	private EnlistmentScreen enlistmentScreen;	
//	private VBox courseList;
//	private VBox about;	
//	private VBox credits;
		
	private StackPane screens;
	private ScrollPane enlistmentScroll;
	
    public ContentArea(Student student, StudentManager manager) { 
        this.student = student;
        this.manager = manager;
    	
        poppinsBold = Font.loadFont(getClass().getResourceAsStream("/resources/Poppins Bold.ttf"), 28);
        inter = Font.loadFont(getClass().getResourceAsStream("/resources/Inter.ttf"), 25);
        interItalic = Font.loadFont(getClass().getResourceAsStream("/resources/Inter Italic.ttf"), 14);
        japaneseFont = Font.loadFont(getClass().getResourceAsStream("/resources/Seibi Ohkido.otf"), 25);
        
    	stage = new Stage();
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/resources/logo.png"))); // Uploading Kurasu Icon for app 
		stage.setTitle(" クラス | Kurasu"); // Sets the title to Kurasu
		
		topBar = createTopbar();
        
	    dashboardScreen = new DashboardScreen(student, inter, poppinsBold, japaneseFont, interItalic);
	    enlistmentScreen = new EnlistmentScreen();
		    enlistmentScroll = new ScrollPane(enlistmentScreen);
		    enlistmentScroll.setFitToWidth(true);
		    enlistmentScroll.setFitToHeight(true);
		    enlistmentScroll.setStyle("-fx-background-color: transparent;");
//		courseList = new CourseListScreen();
//		about = new AboutSceen();	
//		credits = new CreditsSceen();
	    
	    // STACK PANE LAYERS
	    screens = new StackPane(enlistmentScroll, dashboardScreen);
	    
	    // Binds enlistment scroll size to stack pane
	    enlistmentScroll.prefWidthProperty().bind(screens.widthProperty());
	    enlistmentScroll.prefHeightProperty().bind(screens.heightProperty());
	    
	    // Initial visibility of screens
//	    dashboardScreen.setVisible(true);
//	    enlistmentScroll.setVisible(false);      
    }
    

    private void toggleSidebar(Pane sidebar, Pane content) {
        boolean opening = sidebar.getTranslateX() != 0;

        TranslateTransition anim = new TranslateTransition(Duration.millis(300), sidebar);
        anim.setToX(opening ? 0 : -320);
        anim.play();

        content.setMouseTransparent(opening);
    }

    public void show() {
    	 StackPane root = new StackPane();
         root.setStyle("-fx-background-color: white;");

    	 dashboardScreen.setVisible(true);
    	 enlistmentScroll.setVisible(false);
    	 
         // Create sidebar object
         Sidebar side = new Sidebar(inter, poppinsBold, japaneseFont, dashboardScreen, enlistmentScroll);
         VBox sidebar = side.getSidebar();
         
         sidebar.setTranslateX(-320);
         StackPane.setAlignment(sidebar, Pos.CENTER_LEFT);
         
         // Add topbar, sidebar, and the screens that will be changing
         VBox content = new VBox(5, topBar, screens);
         content.setPadding(new Insets(20));
         VBox.setVgrow(screens, Priority.ALWAYS);

         root.getChildren().addAll(content, sidebar);
         sidebar.toFront();
         topBar.setPickOnBounds(false);
         
         
         // Toggle logic
         externalHamburger.setOnAction(e -> toggleSidebar(sidebar, this.dashboardScreen));
         side.getInternalHamburger().setOnAction(e -> toggleSidebar(sidebar, this.dashboardScreen));

         Scene scene = new Scene(root, 1200, 800);
         stage.setScene(scene);
         stage.show();
         
    }
    
    /***** TOP BAR: DASHBOARD PROFILE *****/
	private HBox createTopbar() {
		HBox bar = new HBox();
        bar.setSpacing(20);
        bar.setAlignment(Pos.CENTER_LEFT);

        // Hamburger Button (icon can be changed)
        externalHamburger = new Button("☰");
        externalHamburger.setStyle("-fx-font-size: 24px; -fx-background-color: transparent;");

        Label title = new Label("Dashboard");
        title.setFont(poppinsBold);
        title.setStyle(
        		"-fx-font-size: 32; " + 
        		"-fx-font-weight: bold;");

        Region space = new Region();
        HBox.setHgrow(space, Priority.ALWAYS);

        // Profile Section
        Label nameLabel = new Label(student.getName());
        nameLabel.setFont(Font.font(poppinsBold.getFamily(), 22));
        
        Label emailLabel = new Label(student.getEmail());
        emailLabel.setFont(interItalic);

        VBox profileTexts = new VBox(nameLabel, emailLabel);
        profileTexts.setAlignment(Pos.CENTER_RIGHT);
	
        ImageView profilePic = new ImageView(
        	    new Image(getClass().getResourceAsStream("/resources/logo.png"))
        );;
        profilePic.setFitHeight(50);
        profilePic.setFitWidth(50);
        profilePic.setClip(new Circle(25));

//        HBox profileBox = new HBox(profileTexts, profilePic);
        HBox profileBox = new HBox(profileTexts);
        profileBox.setSpacing(10);
        profileBox.setAlignment(Pos.CENTER_RIGHT);

        bar.getChildren().addAll(externalHamburger, title, space, profileBox);
        return bar;
        
	}

}