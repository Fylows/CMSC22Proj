package frontend;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class AboutScreen extends VBox {
	public AboutScreen() {
		this.setAlignment(Pos.CENTER);
		this.setSpacing(20);
		this.getStyleClass().add("about-screen");

		Label title = new Label("About Kurasu");
		title.getStyleClass().add("about-title");

		Label description = new Label(
				"Kurasu (クラス | Class) is an academic planning tool designed to help \n" +
				"students visualize, track, and manage their courses and progress."
        );
		description.getStyleClass().add("about-description");

		this.getChildren().addAll(title, description);
	}
}
