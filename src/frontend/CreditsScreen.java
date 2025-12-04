package frontend;

import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CreditsScreen extends BorderPane {
	// Constructor
	public CreditsScreen() {
		this.getStyleClass().addAll("credits-root", "credits-padding");
		this.setCenter(createDeveloperSection()); // Center content
		this.setBottom(createReferencesSection()); // Bottom content
	}
	
	// Constructs the Developers section
	private VBox createDeveloperSection() {
		// Content and title
		VBox content = new VBox(15);
		content.getStyleClass().add("credits-content");

		Label devTitle = new Label("Meet the Developers");
		devTitle.getStyleClass().add("credits-title");

		// 2x2 Grid that is wrapped with respective developer cards
		VBox gridWrapper = new VBox(10);
		gridWrapper.getStyleClass().add("credits-grid-wrapper");

		GridPane grid = new GridPane();
		grid.getStyleClass().add("credits-grid");

		grid.add(createDeveloperCard(
				"LJIEL SAPLAN",
				"Backend Developer",
				"Institute of Computer Science\n UP Los Baños",
				"yel.png",
				"ldsaplan@up.edu.ph",
				"2024-02076",
				"Fylos"
		), 0, 0); // Top left

		grid.add(createDeveloperCard(
				"CARLOS ALQUINTO",
				"Backend Developer",
				"Institute of Computer Science\n UP Los Baños",
				"carlos.png",
				"cealquinto@up.edu.ph",
				"2024-XXXXX",
				"dokudokunomu"
		), 1, 0); // Top right

		grid.add(createDeveloperCard(
				"MIKHAIL PERLAS",
				"UI/UX Designer",
				"Institute of Computer Science\n UP Los Baños",
				"mikhail.png",
				"msperlas@up.edu.ph",
				"2024-06607",
				"allhailmikeyl"
		), 0, 1); // Bottom left

		grid.add(createDeveloperCard(
				"ALETHEA CASTAÑEDA",
				"UI/UX Designer",
				"Institute of Statistics\n UP Los Baños",
				"thea.png",
				"arcastaneda@up.edu.ph",
				"2022-11645",
				"acastaneda45"
		), 1, 1); // Bottom right

		gridWrapper.getChildren().add(grid);
		content.getChildren().addAll(devTitle, gridWrapper);

		return content;
	}

	// Constructs the references section
	private VBox createReferencesSection() {
		// Bottom part and title
		VBox bottom = new VBox(10);
		bottom.getStyleClass().add("credits-bottom");

		Label refSectionTitle = new Label("References");
		refSectionTitle.getStyleClass().add("credits-title");

		VBox refBox = new VBox(10);
		refBox.getStyleClass().add("references-box");

		Label refs = new Label(
			"Baeldung. (n.d.). Java email validation with regex. https://www.baeldung.com/java-email-validation-regex\n\n" +
			"Coolors. (n.d.). Trending color palettes. https://coolors.co/palettes/trending\n\n" +
			"Icons8. (n.d.). Free icons. https://icons8.com\n\n" +
			"Institute of Computer Science, University of the Philippines Los Baños. (n.d.). Degree programs. https://ics.uplb.edu.ph/degree-programs/\n\n" +
			"Land Book. (n.d.). UI Design Gallery. https://land-book.com"
		);
		
		refs.setWrapText(true);
		refs.getStyleClass().add("references-list");
		refBox.getChildren().add(refs);

		bottom.getChildren().addAll(refSectionTitle, refBox);

		return bottom;
	}

	// Developer Card Component
	private HBox createDeveloperCard(
			String name, String role, String school, String imageFile,
			String email, String studentNum, String github
	) {
		
		StackPane circlePane = new StackPane();
		circlePane.getStyleClass().add("dev-image-wrapper");

		ImageView pic = new ImageView();
		pic.setFitWidth(150);
		pic.setFitHeight(150);
		pic.setPreserveRatio(false);

		Circle clip = new Circle(75, 75, 75);
		pic.setClip(clip);
		circlePane.getChildren().add(pic);

		try {
			Image img = new Image(getClass().getResource("/resources/" + imageFile).toExternalForm());
			pic.setImage(img);
		} catch (Exception e) {
			System.out.println("⚠ Could not load image: " + imageFile);
		}

		// Labels for developer's details
		Label nameLabel = new Label(name);
		nameLabel.getStyleClass().add("dev-name");

		Label roleLabel = new Label(role);
		roleLabel.getStyleClass().add("dev-details");

		Label schoolLabel = new Label(school);
		schoolLabel.getStyleClass().add("dev-school");

		Label emailLabel = new Label("Email: " + email);
		emailLabel.getStyleClass().add("dev-details");

		Label studLabel = new Label("Student No: " + studentNum);
		studLabel.getStyleClass().add("dev-details");

		Label gitLabel = new Label("GitHub: " + github);
		gitLabel.getStyleClass().add("dev-details");

		VBox details = new VBox(5, nameLabel, roleLabel, schoolLabel, emailLabel, studLabel, gitLabel);
		details.getStyleClass().add("dev-details-box");

		HBox card = new HBox(20, circlePane, details);
		card.getStyleClass().add("dev-card");

		return card;
	}
}
