package frontend;

import backend.Student;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;

//import backend.Student;

public class DashboardScreen extends VBox{
	private Student student;
	
	public DashboardScreen(Student student, Font inter, Font poppinsBold, Font japaneseFont, Font interItalic) {
		this.student = student;
		
		setSpacing(20);
		setPadding(new Insets(20));
		setStyle("-fx-background-color: white;");

        // ---------- WELCOME CARD ----------
        VBox welcomeCard = createWelcomeCard(student, inter, poppinsBold, japaneseFont, interItalic);

        // ---------- STATS ROW ----------
        HBox statsRow = createStatsRow(inter, poppinsBold, japaneseFont, interItalic);

        // Add all to layout
        getChildren().addAll(welcomeCard, statsRow);
    }
	
	
	/***** WELCOME CARD *****/
	private VBox createWelcomeCard(Student student, Font inter, Font poppinsBold, Font japaneseFont, Font interItalic) {
        VBox card = new VBox();
        card.setPadding(new Insets(30));
        card.setSpacing(10);

        card.setStyle(
            "-fx-background-color: #f7c6d2;" +
            "-fx-background-radius: 30;"
        );
        
        Label title = new Label("Welcome back, " + student.getFirstName() + "!");
        title.setFont(Font.font(poppinsBold.getFamily(), 36));


        Label subtitle = new Label(
            "Welcome to クラス | Karasu! This is where your academic journey blooms, one class at a time."
        );
        subtitle.setFont(Font.font(inter.getFamily(), 18));
        subtitle.setPadding(new Insets(0, 10, 0, 30));

        Hyperlink tutorial = new Hyperlink("Tutorial on how to use Karasu System →");
        tutorial.setFont(Font.font(interItalic.getFamily(), 16));
        tutorial.setPadding(new Insets(0, 10, 0, 30));

        card.getChildren().addAll(title, subtitle, tutorial);
        return card;
	}
	
	
	/***** STATS ROW *****/
    private HBox createStatsRow(Font inter, Font poppinsBold, Font japaneseFont, Font interItalic) {
        HBox row = new HBox();
        row.setSpacing(30);
        row.setAlignment(Pos.CENTER);

        VBox completedCard = createStatCard("[PERCENT OF COMPLETED UNITS]", "of units completed");
        VBox takenCard     = createStatCard("[TOTAL UNITS TAKEN]", "Units Taken (out of 130)");
        VBox allowableCard = createStatCard("21",  "Allowable Units");

        row.getChildren().addAll(completedCard, takenCard, allowableCard);
        return row;
    }

    private VBox createStatCard(String value, String label) {
        VBox card = new VBox();
        card.setPadding(new Insets(20));
        card.setSpacing(10);
        card.setAlignment(Pos.CENTER);

        card.setStyle(
            "-fx-background-color: #f7c6d2;" +
            "-fx-background-radius: 20;"
        );

        Label valueLabel = new Label(value);
        valueLabel.setStyle("-fx-font-size: 32; -fx-font-weight: bold;");

        Label textLabel = new Label(label);
        textLabel.setStyle("-fx-font-size: 16;");

        card.getChildren().addAll(valueLabel, textLabel);
        return card;
    }
	

}


