package frontend;

import javafx.util.Duration;
import javafx.animation.TranslateTransition;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.scene.text.*;

public class ContentArea {
	Font poppinsBold = Font.loadFont(getClass().getResourceAsStream("/resources/Poppins Bold.ttf"), 28);
	Font inter = Font.loadFont(getClass().getResourceAsStream("/resources/Inter.ttf"), 25);
	Font interItalic = Font.loadFont(getClass().getResourceAsStream("/resources/Inter Italic.ttf"), 14);
	Font japaneseFont = Font.loadFont(getClass().getResourceAsStream("/resources/Seibi Ohkido.otf"), 25);
	
    private Stage stage;
	static Button externalHamburger = new Button();

    // -- Different screens
	private HBox topBar = createTopbar();
    private VBox dashboardScreen = new DashboardScreen();
    
	private EnlistmentScreen enlistmentScreen = new EnlistmentScreen();	
			
//	private VBox courseList = new CourseListScreen();
//	private VBox about = new AboutSceen();	
//	private VBox credits = new CreditsSceen();	
		
	private StackPane screens = new StackPane(dashboardScreen, enlistmentScreen);
    

    public ContentArea() { //ITSURA NG WINDOW
        stage = new Stage();
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/resources/logo.png"))); // Uploading Kurasu Icon for app 
		stage.setTitle(" クラス | Kurasu"); // Sets the title to Kurasu
        
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
    	 
    	 dashboardScreen.setVisible(true);
    	 enlistmentScreen.setVisible(false);
    	 
    	 
         // Create sidebar object
         Sidebar side = new Sidebar(inter, poppinsBold, japaneseFont, dashboardScreen, enlistmentScreen);
         VBox sidebar = side.getSidebar();

         sidebar.setTranslateX(-320);
         StackPane.setAlignment(sidebar, Pos.CENTER_LEFT);
        
         
         // Add topbar, sidebar, and the screens that will be changing
         root.getChildren().addAll(screens, sidebar,topBar);
         
         StackPane.setAlignment(topBar, Pos.TOP_LEFT); // pin topBar to top
         topBar.prefWidthProperty().bind(root.widthProperty()); // stretch across window
         
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
        VBox profileTexts = new VBox(
        	new Label("Name"),
            new Label("email@up.edu.ph")
        );
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