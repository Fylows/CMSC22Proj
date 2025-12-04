package frontend;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class CreditsScreen extends VBox {
	public CreditsScreen() {
		this.setAlignment(Pos.CENTER);
		this.setSpacing(20);
		this.getStyleClass().add("credits-screen");

		Label title = new Label("Credits");
		title.getStyleClass().add("credits-title");

		Label description = new Label(
			"Developed by:\n" +
			"Name" +
			"ICS | University of the Philippines"
		);
		description.getStyleClass().add("credits-description");

		this.getChildren().addAll(title, description);
	}
}
