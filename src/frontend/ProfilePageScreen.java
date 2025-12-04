package frontend;

import backend.Student;
import backend.StudentManager;
import javafx.geometry.*;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

public class ProfilePageScreen extends HBox{
	private final Student student;
	
	public ProfilePageScreen(Student student) {
		this.student = student;
		
        setSpacing(20);
        setPadding(new Insets(60, 20, 20, 20));
        setStyle("-fx-background-color: white;");
        
        
        VBox profileCard = createProfileCard(student);
        VBox profileInfo = createProfileInformation(student);
        
		getChildren().addAll(profileCard, profileInfo);
	}
	
	/***** Left Panel: PROFILE CARD  *****/	
    private VBox createProfileCard(Student student) {

        VBox profile = new VBox();
        profile.setPadding(new Insets(20));
        profile.setSpacing(20);
        profile.setAlignment(Pos.TOP_CENTER);
        profile.setPrefWidth(300);
        profile.setStyle("-fx-background-color: #ffe5ec; -fx-background-radius: 25;");

        // PFP Placeholder
        Region pfpPlaceholder = new Region();
        pfpPlaceholder.setPrefSize(150, 150);
        pfpPlaceholder.setStyle("-fx-background-color: white; -fx-background-radius: 75;");

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

        
        profile.getChildren().addAll(pfpPlaceholder, degree, infoBox, contactInfo, grades);

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
	
}
