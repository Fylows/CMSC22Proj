package frontend;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

public class Sidebar {
	
	private VBox sidebar;          // The whole sidebar UI
//	private VBox enlistment;
	private VBox dashboard;
	private ScrollPane enlistmentScroll;

    private Button internalHamburger; // The hamburger button inside the sidebar

    public Sidebar(Font inter, Font poppinsBold, Font japaneseFont, VBox dashboard, ScrollPane enlistmentScroll) {
        sidebar = new VBox(10);
        sidebar.setPrefWidth(320);
        sidebar.setMaxWidth(320);
        
        sidebar.setPadding(new Insets(7));

        sidebar.setStyle(
            "-fx-background-color: #fff0f4;" +
            "-fx-border-color: #fff0f4;" +
            "-fx-background-radius: 0 20 20 0;" +
            "-fx-border-radius: 0 20 20 0;"
        );

        sidebar.setTranslateX(-320);   // Hidden by default
        sidebar.setTranslateX(0);
        sidebar.setAlignment(Pos.TOP_LEFT);
        
    	this.dashboard = dashboard;
//    	this.enlistment = enlistment;
    	this.enlistmentScroll = enlistmentScroll;
    	
    	// --- HEADER WITH HAMBURGER ---
        HBox header = new HBox(10);

        internalHamburger = new Button("☰");
        internalHamburger.setStyle("-fx-font-size: 24px; -fx-background-color: transparent;");

        Label h_kurasu = new Label("クラス");
        h_kurasu.setFont(japaneseFont);

        Label kurasu = new Label("| Kurasu");
        kurasu.setFont(poppinsBold);

        header.getChildren().addAll(internalHamburger, h_kurasu, kurasu);
        header.setPadding(new Insets(5));

        // --- Sidebar items ---

        HBox dashboardBox = createItem("Dashboard", "/resources/dashboardIcon.png", inter);
        dashboardBox.setOnMouseClicked(event -> {
        	this.dashboard.setVisible(true);
            this.enlistmentScroll.setVisible(false);
        });
        
        HBox enlistmentBox = createItem("Enlistment", "/resources/enlistIcon.png", inter);
        enlistmentBox.setOnMouseClicked(event -> {
        	this.dashboard.setVisible(false);
            this.enlistmentScroll.setVisible(true);
        });
        
        HBox aboutBox = createItem("About", "/resources/aboutIcon.png", inter);
        HBox creditsBox = createItem("Credits", "/resources/creditsIcon.png", inter);

        // --- Course List dropdown ---
        HBox courseBox = createItem("Course List", "/resources/courseIcon.png", inter);
        
        VBox courseDropdown = new VBox(
                new Label("- BS Computer Science"),
                new Label("- MS Computer Science"),
                new Label("- MIT"),
                new Label("- PHD")
        );
        courseDropdown.setPadding(new Insets(0, 0, 0, 20));
        courseDropdown.setVisible(false);
        courseDropdown.setManaged(false);

        ImageView dropdownIcon = new ImageView(
            new Image(getClass().getResourceAsStream("/resources/dropdownIcon.png"))
        );
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

        // --- Logout Button ---
        Button logoutBtn = new Button("Log Out");
        logoutBtn.setFont(poppinsBold);
        logoutBtn.setStyle(
            "-fx-background-color: #fb6f92;" +
            "-fx-background-radius: 8;" +
            "-fx-text-fill: #ffe5ec;" +
            "-fx-font-size: 20px;"
        );
        
        logoutBtn.setStyle(
    			"-fx-background-color: #fb6f92;" + // Main color of the button (darker pink)
				"-fx-background-radius: 8;" +
				"-fx-text-fill: #ffe5ec;" +
				"-fx-font-size: 20px;" + // Font size 20
				"-fx-font-weight: bold;"
		);
        
        logoutBtn.setOnMouseEntered(e ->
        	logoutBtn.setStyle(
        			"-fx-background-color: #ff85ac;" + // Main color of the button (darker pink)
					"-fx-background-radius: 8;" +
					"-fx-text-fill: #ffe5ec;" +
					"-fx-font-size: 20px;" + // Font size 20
					"-fx-font-weight: bold;"
        	)
        );
        
        logoutBtn.setOnMouseExited(e ->
    		logoutBtn.setStyle(
    				"-fx-background-color: #fb6f92;" + // Main color of the button (darker pink)
					"-fx-background-radius: 8;" +
					"-fx-text-fill: #ffe5ec;" +
					"-fx-font-size: 20px;" + // Font size 20
					"-fx-font-weight: bold;"
    		)
        );
    
        logoutBtn.setOnAction(e -> {
        	//stage.close();
        	System.exit(0);
        });

        HBox logoutWrapper = new HBox(logoutBtn);
        logoutWrapper.setAlignment(Pos.CENTER);
        logoutWrapper.setPadding(new Insets(20));

        sidebar.getChildren().addAll(
            header,
            dashboardBox,
            enlistmentBox,
            courseListBox,
            aboutBox,
            creditsBox,
            spacer,
            logoutWrapper
        );
    }

    private HBox createItem(String title, String iconPath, Font inter) {
        ImageView icon = new ImageView(new Image(getClass().getResourceAsStream(iconPath)));
        icon.setFitWidth(25);
        icon.setFitHeight(25);

        Label label = new Label(title);
        label.setFont(inter);

        HBox box = new HBox(15, icon, label);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setPadding(new Insets(7));
        return box;
    }

    public VBox getSidebar() {
        return sidebar;
    }

    public Button getInternalHamburger() {
        return internalHamburger;
    }	
}
