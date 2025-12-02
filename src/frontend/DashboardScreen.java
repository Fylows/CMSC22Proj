package frontend;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;

//import backend.Student;

public class DashboardScreen extends VBox{

	Font poppinsBold = Font.loadFont(getClass().getResourceAsStream("/resources/Poppins Bold.ttf"), 28);
	Font inter = Font.loadFont(getClass().getResourceAsStream("/resources/Inter.ttf"), 25);
	Font interItalic = Font.loadFont(getClass().getResourceAsStream("/resources/Inter Italic.ttf"), 14);
	Font japaneseFont = Font.loadFont(getClass().getResourceAsStream("/resources/Seibi Ohkido.otf"), 25);

	//private Student student;
	
	public DashboardScreen() {
		
		setSpacing(20);
		setPadding(new Insets(20));
		setStyle("-fx-background-color: white;");


        // ---------- WELCOME CARD ----------
        VBox welcomeCard = createWelcomeCard();

        // ---------- STATS ROW ----------
        HBox statsRow = createStatsRow();

        // Add all to layout
        getChildren().addAll(welcomeCard, statsRow);
    }
	
	
	/***** WELCOME CARD *****/
	private VBox createWelcomeCard() {
        VBox card = new VBox();
        card.setPadding(new Insets(30));
        card.setSpacing(10);

        card.setStyle(
            "-fx-background-color: #f7c6d2;" +
            "-fx-background-radius: 30;"
        );
        
        Label title = new Label("Welcome back, [NAME]!");
        title.setStyle("-fx-font-size: 36; -fx-font-weight: bold;");

        Label subtitle = new Label(
            "Welcome to クラス | Karasu! This is where your academic journey blooms, one class at a time "
        );
        subtitle.setStyle("-fx-font-size: 16;");

        Label tutorial = new Label("Tutorial on how to use Karasu System →");
        tutorial.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

        card.getChildren().addAll(title, subtitle, tutorial);
        return card;
	}
	
	
	/***** STATS ROW *****/
    private HBox createStatsRow() {
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


