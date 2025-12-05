package frontend;

import backend.CourseManager;
import backend.Student;
import javafx.geometry.*;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

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
		HBox statsRow = createStatsRow(student); // Stats Row

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
	private HBox createStatsRow(Student student) {
		HBox row = new HBox();
		row.getStyleClass().add("dashboard-stats-row");
		
		int completedUnits = student.getTotalCompletedUnits();
		int requiredUnits = CourseManager.getTotalRequiredUnits(student.getDegree());

		HBox progressCard = createProgressCard(student);
		HBox takenCard = createStatCard(completedUnits, "Units Taken (out of " + requiredUnits + ")");
		HBox allowableCard = createAllowableCard(21, "Allowable Units");

		row.getChildren().addAll(progressCard, takenCard, allowableCard);

		return row;
	}
	
	// Leftmost box
	private HBox createProgressCard(Student student) {
	    int completed = student.getTotalCompletedUnits();
	    int required = CourseManager.getTotalRequiredUnits(student.getDegree());
	    int percent = (int) Math.round((completed * 100.0) / required); // computes for the percentage
	    
	    // Donut chart
	    StackPane donutChart = createDonutChart(student);


	    // Text beside donut chart
	    Label percentLabel = new Label(percent + "%");
	    percentLabel.getStyleClass().add("stat-value");

	    Label subLabel = new Label("of units completed");
	    subLabel.getStyleClass().add("stat-label");
	    
	    Label subLabel1 = new Label ("(core major courses)");
//	    subLabel.getStyleClass().add("stat-label");

	    VBox textBox = new VBox(5, percentLabel, subLabel, subLabel1);
	    textBox.setAlignment(Pos.CENTER_LEFT);

	    // 
	    HBox card = new HBox(20, donutChart, textBox);
	    card.setAlignment(Pos.CENTER_LEFT);
	    card.setPadding(new Insets(20));
		card.getStyleClass().add("dashboard-stat-card");

	    return card;
	}
	
	// Middle Box
	private HBox createStatCard(int value, String label) {
		HBox card = new HBox();
		card.getStyleClass().add("dashboard-stat-card");
		
		Label textLabel = new Label(label);
		textLabel.getStyleClass().add("stat-label");
		
		Label subLabel1 = new Label ("(core major courses)");
//	    subLabel.getStyleClass().add("stat-label");

	    VBox textBox = new VBox(5, textLabel, subLabel1);
	    textBox.setAlignment(Pos.CENTER_LEFT);
		
		Label valueLabel = new Label(String.valueOf(value));
		valueLabel.getStyleClass().add("stat-value");
		
		ImageView separator = new ImageView(new Image(getClass().getResourceAsStream("/resources/line.png"))); // Hamburger Icon
		separator.setFitWidth(50);
		separator.setFitHeight(50);

		card.getChildren().addAll(textBox, separator, valueLabel);

		return card;
	}
	
	// Rightmost Box
	private HBox createAllowableCard(int value, String label) {
		HBox card = new HBox();
		card.getStyleClass().add("dashboard-stat-card");
		
		Label textLabel = new Label(label);
		textLabel.getStyleClass().add("stat-label");
		
		Label valueLabel = new Label(String.valueOf(value));
		valueLabel.getStyleClass().add("stat-value");
		
		ImageView separator = new ImageView(new Image(getClass().getResourceAsStream("/resources/line.png"))); // Hamburger Icon
		separator.setFitWidth(50);
		separator.setFitHeight(50);

		card.getChildren().addAll(textLabel, separator, valueLabel);

		return card;
	}
	
	
	
	// To create donut chart
		private StackPane createDonutChart(Student student) {
			int completedUnits = student.getTotalCompletedUnits();
			int requiredUnits = CourseManager.getTotalRequiredUnits(student.getDegree());
			int remainingUnits = requiredUnits - completedUnits;
			
			PieChart pieChart = new PieChart();
			PieChart.Data completedData = new PieChart.Data("Completed", completedUnits);
			PieChart.Data remainingData = new PieChart.Data("Remaining", remainingUnits);
			
			pieChart.getData().addAll(completedData, remainingData);
			
			// Apply custom colors per chart segment
			String completedColor = "#6f7df4";
		    String remainingColor = "#b0d1f9";
		    
		    // Removing white border
		    String borderRemovalStyle = "-fx-border-color: #f7c6d2; -fx-stroke: #f7c6d2; -fx-stroke-width: 0;";

		    completedData.getNode().setStyle("-fx-pie-color: " + completedColor + "; " + borderRemovalStyle); 
		    remainingData.getNode().setStyle("-fx-pie-color: " + remainingColor + "; " + borderRemovalStyle);
			

			pieChart.setLabelsVisible(false);   // removes text on slices
			pieChart.setLegendVisible(false); 	// removes legends
			
			// Creates the chart hole
			Circle innerCircle = new Circle();
	        innerCircle.setRadius(30); 
	        innerCircle.setFill(Color.web("#f7c6d2"));
			
			StackPane donutChart = new StackPane(pieChart, innerCircle); // Creates the donut chart
		    donutChart.setPrefSize(120, 120); // Fixed size for chart
			donutChart.setAlignment(Pos.CENTER);
			 	
			return donutChart;
		}

	// Getters
	public Student getStudent() {
		return student;
	}
}