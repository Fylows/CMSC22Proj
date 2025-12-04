package frontend;

import backend.Student;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class DashboardScreen extends StackPane {
	private final Student student;
	private final Pane petalPane;
	private final VBox content;
    
	private final ScreenChangeListener listener;

	// Constructor
	public DashboardScreen(Student student, ScreenChangeListener listener) {
		this.student = student;
		this.listener = listener;
		
		petalPane = new Pane(); // Petal animation for DashBoard only
		PetalAnimation.start(petalPane, 50, 0.2);
		petalPane.setMouseTransparent(true);
		petalPane.setPickOnBounds(false);
        
		petalPane.prefWidthProperty().bind(widthProperty());
		petalPane.prefHeightProperty().bind(heightProperty());

		content = new VBox(20);
		getStyleClass().add("dashboard-root");

		VBox welcomeCard = createWelcomeCard(student); // Welcome Card
		HBox statsRow = createStatsRow(); // Stats Row

		content.getChildren().addAll(welcomeCard, statsRow);
		getChildren().addAll(petalPane, content);
	}

	// Welcome Box
	private VBox createWelcomeCard(Student student) {
		VBox card = new VBox();
		card.getStyleClass().add("dashboard-welcome-card");

		Label title = new Label("Welcome back, " + student.getFirstName() + "!");
		title.getStyleClass().add("dashboard-title");

		Label subtitle = new Label("Welcome to クラス | Kurasu! This is where your academic journey blooms, one class at a time.");
		subtitle.getStyleClass().add("dashboard-subtitle");

		Hyperlink tutorial = new Hyperlink("Tutorial on how to use Kurasu System →");
		tutorial.getStyleClass().add("dashboard-tutorial");
		
		tutorial.setOnAction(e -> listener.onScreenChange("About"));

		card.getChildren().addAll(title, subtitle, tutorial);
		
		return card;
	}

	// Stats Boxes
	private HBox createStatsRow() {
		HBox row = new HBox();
		row.getStyleClass().add("dashboard-stats-row");

		VBox completedCard = createStatCard("[PERCENT OF COMPLETED UNITS]", "of units completed");
		VBox takenCard = createStatCard("[TOTAL UNITS TAKEN]", "Units Taken (out of 130)");
		VBox allowableCard = createStatCard("21", "Allowable Units");

		row.getChildren().addAll(completedCard, takenCard, allowableCard);

		return row;
	}

	private VBox createStatCard(String value, String label) {
		VBox card = new VBox();
		card.getStyleClass().add("dashboard-stat-card");

		Label valueLabel = new Label(value);
		valueLabel.getStyleClass().add("stat-value");

		Label textLabel = new Label(label);
		textLabel.getStyleClass().add("stat-label");

		card.getChildren().addAll(valueLabel, textLabel);

		return card;
	}

	// Getters
	public Student getStudent() {
		return student;
	}
}