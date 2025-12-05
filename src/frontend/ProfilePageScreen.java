package frontend;

import backend.Student;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;

public class ProfilePageScreen extends StackPane{
	private final Student student;
	private final Pane petalPane;
	private final HBox content;
	
	// Constructor
	public ProfilePageScreen(Student student) {
		this.student = student;
        
		petalPane = new Pane(); // Petal animation for Profile Page only
		PetalAnimation.start(petalPane, 50, 0.2);
		petalPane.setMouseTransparent(true);
		petalPane.setPickOnBounds(false);
        
		petalPane.prefWidthProperty().bind(widthProperty());
		petalPane.prefHeightProperty().bind(heightProperty());
		
		content = new HBox();
        content.getStyleClass().add("profile-root");
        
        VBox profileCard = createProfileCard(student);
        VBox profileInfo = createProfileInformation(student);
        
        HBox.setHgrow(profileCard, Priority.ALWAYS); 
        HBox.setHgrow(profileInfo, Priority.ALWAYS);
        
        content.getChildren().addAll(profileCard, profileInfo);
		getChildren().addAll(petalPane, content);
	}
	
	// Constructs a Profile Card on the left side
    private VBox createProfileCard(Student student) {

        VBox profile = new VBox();
        profile.getStyleClass().add("profile-left-panel");
        
        // PFP 
		ImageView profilePic = new ImageView(new Image(getClass().getResourceAsStream("/resources/defaultPFP.png")));
		profilePic.setFitHeight(200);
		profilePic.setFitWidth(200);
		profilePic.setPreserveRatio(true);

		// Correct clipping
		Circle clip = new Circle(100, 100, 100);
		profilePic.setClip(clip);
		
        Label degree = new Label(student.getDegree());
        degree.getStyleClass().add("profile-degree");

        HBox infoBox = createInfoBox("Personal Information", "/resources/personIcon.png");       
        HBox contactInfo = createItem("Contact and Address", "/resources/addressIcon.png");
        HBox grades = createItem("Grades", "/resources/enlistIcon.png");
        
        profile.getChildren().addAll(profilePic, degree, infoBox, contactInfo, grades);

        return profile;
    }
	
	// Constructs the Profile Information on the right side
	private VBox createProfileInformation(Student student) {
		 VBox profileInfo = new VBox(30);
        profileInfo.getStyleClass().add("profile-right-panel");

        Label header = new Label("Personal Information");
        header.getStyleClass().add("profile-header");

        // All form rows
        GridPane infoGrid = new GridPane();
        infoGrid.setHgap(30);
        infoGrid.setVgap(25);

        // Create display fields (WHITE ROUNDED LABELS)
        infoGrid.add(createFieldRow("First Name:", student.getFirstName()), 0, 0);
        infoGrid.add(createFieldRow("Middle Name:", student.getMiddleName()), 1, 0);

        infoGrid.add(createFieldRow("Last Name:", student.getLastName()), 0, 1);
        infoGrid.add(createFieldRow("Generational Suffix:", student.getSuffix()), 1, 1);

        infoGrid.add(createFieldRowWide("Email:", student.getEmail()), 0, 2, 2, 1);
        infoGrid.add(createFieldRow("Date of Birth:", student.getBirthdate()), 0, 3, 2, 1);
        
        infoGrid.add(createFieldRow("Sex:", student.getSex()), 1, 3);

        profileInfo.getChildren().addAll(header, infoGrid);
        return profileInfo;
	}	
	
	// Creates the short field rows
    private VBox createFieldRow(String labelText, String value) { // Short field rows
        Label label = new Label(labelText);
        label.getStyleClass().add("profile-field-label");

        Label field = new Label(value);
        field.setMinHeight(40);
        field.setPrefWidth(300);
        field.getStyleClass().add("profile-field");

        VBox box = new VBox(5, label, field);
        return box;
    }
    
    // Creates the long field rows
    private VBox createFieldRowWide(String labelText, String value) { // Long field rows
        Label label = new Label(labelText);
        label.getStyleClass().add("profile-field-label");

        Label field = new Label(value);
        field.setMinHeight(40);
        field.setPrefWidth(700);
        field.getStyleClass().add("profile-field");

        VBox box = new VBox(5, label, field);
        return box;
    }
	
    private HBox createItem(String title, String iconPath) {
        ImageView icon = new ImageView(new Image(getClass().getResourceAsStream(iconPath)));
        icon.setFitWidth(25);
        icon.setFitHeight(25);

        Label label = new Label(title);
        label.getStyleClass().add("profile-menu");

        HBox box = new HBox(15, icon, label);
        box.getStyleClass().add("profile-items");

        
        return box;
    }
    
    private HBox createInfoBox(String title, String iconPath) {
        ImageView icon = new ImageView(new Image(getClass().getResourceAsStream(iconPath)));
        icon.setFitWidth(25);
        icon.setFitHeight(25);

        Label label = new Label(title);
        label.getStyleClass().add("profile-menu");

        HBox box = new HBox(15, icon, label);
        box.getStyleClass().add("profile-infoBox");

        
        return box;
    }
    
    // Getters
	public Student getStudent() {
		return student;
	}
}
