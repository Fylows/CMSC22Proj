package frontend;

import backend.Student;
import javafx.geometry.*;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;

public class ProfilePageScreen extends StackPane{
	private final Student student;
	private final Pane petalPane;
	private final HBox content;
	
	public ProfilePageScreen(Student student) {
		this.student = student;
        
		petalPane = new Pane(); // Petal animation for DashBoard only
		PetalAnimation.start(petalPane, 50, 0.2);
		petalPane.setMouseTransparent(true);
		petalPane.setPickOnBounds(false);
        
		petalPane.prefWidthProperty().bind(widthProperty());
		petalPane.prefHeightProperty().bind(heightProperty());
		
		content = new HBox();
        content.setSpacing(20);
        setPadding(new Insets(80, 20, 20, 20));
//      setStyle("-fx-background-color: white;");
        
        VBox profileCard = createProfileCard(student);
        VBox profileInfo = createProfileInformation(student);
        
        HBox.setHgrow(profileCard, Priority.ALWAYS); 
        HBox.setHgrow(profileInfo, Priority.ALWAYS);
        
        content.getChildren().addAll(profileCard, profileInfo);
		getChildren().addAll(petalPane, content);
	}
	
	/***** Left Panel: PROFILE CARD  *****/	
    private VBox createProfileCard(Student student) {

        VBox profile = new VBox();
        profile.setPadding(new Insets(20));
        profile.setSpacing(20);
        profile.setAlignment(Pos.TOP_CENTER);
//        profile.setPrefWidth(300);
        profile.setStyle("-fx-background-color: #ffe5ec; -fx-background-radius: 25;");

        // PFP 
		ImageView profilePic = new ImageView(new Image(getClass().getResourceAsStream("/resources/defaultPFP.png")));
		profilePic.setFitHeight(200);
		profilePic.setFitWidth(200);
		profilePic.setPreserveRatio(true);

		// Correct clipping
		Circle clip = new Circle(100, 100, 100);
		profilePic.setClip(clip);
		
        Label degree = new Label(student.getDegree());
        degree.setFont(Font.font(20));

        HBox infoBox = createItem("Personal Information", "/resources/personIcon.png");       
        HBox contactInfo = createItem("Contact and Address", "/resources/addressIcon.png");
        HBox grades = createItem("Grades", "/resources/enlistIcon.png");
        
        // BOX STYLES
        infoBox.setStyle("-fx-background-color: #f9a8c4; -fx-background-radius: 8;");
        contactInfo.setStyle("-fx-background-radius: 8;");
        contactInfo.setOnMouseEntered(e ->
        	contactInfo.setStyle("-fx-background-color: #f9a8c4; -fx-background-radius: 8;"));
        contactInfo.setOnMouseExited(e ->
    		contactInfo.setStyle("-fx-background-color: #ffe5ec; -fx-background-radius: 8;"));
        grades.setStyle("-fx-background-radius: 8;");
        grades.setOnMouseEntered(e ->
        	grades.setStyle("-fx-background-color: #f9a8c4; -fx-background-radius: 8;"));
        grades.setOnMouseExited(e ->
    		grades.setStyle("-fx-background-color: #ffe5ec; -fx-background-radius: 8;"));

        
        profile.getChildren().addAll(profilePic, degree, infoBox, contactInfo, grades);

        return profile;
    }
	
	/***** Right Panel: PROFILE INFORMATION *****/
	private VBox createProfileInformation(Student student) {
		 VBox profileInfo = new VBox(30);
        profileInfo.setPadding(new Insets(40));
        profileInfo.setStyle("-fx-background-color: #ffe5ec; "
                          + "-fx-background-radius: 25;");

        Label header = new Label("Personal Information");
        header.setFont(Font.font(28));

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

        profileInfo.getChildren().addAll(header, infoGrid);
        return profileInfo;
	}	
	
    private VBox createFieldRow(String labelText, String value) {
        Label label = new Label(labelText);

        Label field = new Label(value);
        field.setMinHeight(40);
        field.setPrefWidth(300);
        field.setPadding(new Insets(5, 15, 5, 15));
        field.setStyle("-fx-background-color: white; -fx-background-radius: 20; "
                + "-fx-border-color: #f7a8c9; -fx-border-radius: 20;");
//        HBox.setHgrow(field, Priority.ALWAYS);

        VBox box = new VBox(5, label, field);
        return box;
    }
    
    private VBox createFieldRowWide(String labelText, String value) {
        Label label = new Label(labelText);

        Label field = new Label(value);
        field.setMinHeight(40);
        field.setPrefWidth(700);
        field.setPadding(new Insets(5, 15, 5, 15));
        field.setStyle("-fx-background-color: white; -fx-background-radius: 20; "
                + "-fx-border-color: #f7a8c9; -fx-border-radius: 20;");
//        HBox.setHgrow(field, Priority.ALWAYS);

        VBox box = new VBox(5, label, field);
        return box;
    }
	
    private HBox createItem(String title, String iconPath) {
        ImageView icon = new ImageView(new Image(getClass().getResourceAsStream(iconPath)));
        icon.setFitWidth(25);
        icon.setFitHeight(25);

        Label label = new Label(title);
//        label.setFont(inter);

        HBox box = new HBox(15, icon, label);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setPadding(new Insets(7));
        return box;
    }

    // Getters
	public Student getStudent() {
		return student;
	}
	
}
