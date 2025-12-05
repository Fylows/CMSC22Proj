package frontend;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

public class AboutScreen extends BorderPane {
    // Constructor
	public AboutScreen() {
		this.getStyleClass().add("about-root");

		this.setTop(createTopSection()); // Top section
		this.setCenter(createMainContent()); // Main content
	}

    // Constructs the Top section
	private VBox createTopSection() {
		VBox top = new VBox(10);
		top.getStyleClass().add("about-header-container");

		Label title = new Label("About This Site");
		title.getStyleClass().add("about-title");

		top.getChildren().add(title);
		return top;
	}

	// Constructs the Main Content
	private VBox createMainContent() {
		VBox content = new VBox();
		content.getStyleClass().add("about-content");

		VBox aboutSection = createAboutSection();
		VBox navigationSection = createNavigationSection();
		VBox howToUseSection = createHowToUseSection();

		// Add spacing between sections
		Region space1 = new Region();
		space1.setPrefHeight(40);

		Region space2 = new Region();
		space2.setPrefHeight(40);

		content.getChildren().addAll(aboutSection, space1, navigationSection, space2, howToUseSection);

		return content;
	}

    // About Section
	private VBox createAboutSection() {
		VBox box = new VBox(10);
		box.getStyleClass().add("about-section");

		Label header = new Label("What This Site Is");
		header.getStyleClass().add("section-header");
		header.setAlignment(Pos.CENTER);
		header.setMaxWidth(Double.MAX_VALUE);

		Text text = new Text(
			"This site is designed to help students easily browse programs, view courses, and plan their enrollment for the upcoming semesters.\n\n" +
			"After a successful sign-up, it provides your profile details and current status for the courses you have finished.\n\n" +
			"It also shows the different courses and its details, units, and prerequisites to guide academic planning."
		);
		text.getStyleClass().add("section-text");

		box.getChildren().addAll(header, text);
		
		return box;
	}

    // Navigation Section
	private VBox createNavigationSection() {
		VBox wrapper = new VBox(15);
		wrapper.getStyleClass().add("about-section");
		wrapper.setAlignment(Pos.TOP_CENTER);

		Label header = new Label("How to Navigate");
		header.getStyleClass().add("section-header");
		header.setAlignment(Pos.CENTER);
		header.setMaxWidth(Double.MAX_VALUE);

        // Navigation image helper
		ImageView navImg = new ImageView(new Image(getClass().getResourceAsStream("/resources/navigation.png")));
		navImg.setFitWidth(800);
		navImg.setPreserveRatio(true);

		Text desc = new Text(
			"• Use the sidebar to switch between sections (Programs, Courses, Flowcharts, etc.).\n" +
			"• Click cards to view more information.\n" +
			"• Scroll when the content is long.\n" +
			"• Use search bars (if available) to filter courses.\n"
		);
		desc.getStyleClass().add("section-text");

		// Wrap desc in an HBox so it aligns left even when the section is centered
		HBox descWrapper = new HBox(desc);
		descWrapper.setAlignment(Pos.CENTER_LEFT);

		wrapper.getChildren().addAll(header, navImg, descWrapper);

		return wrapper;
	}

    // How to Use Section
	private VBox createHowToUseSection() {
		VBox box = new VBox(10);
		box.getStyleClass().add("about-section");
		box.setAlignment(Pos.TOP_CENTER);

		Label header = new Label("How to Use the Enrollment System");
		header.getStyleClass().add("section-header");
		header.setAlignment(Pos.CENTER);
		header.setMaxWidth(Double.MAX_VALUE);
		
		// Enlistment image helper
		ImageView enlistImg1 = new ImageView(new Image(getClass().getResourceAsStream("/resources/enlistment1.png")));
		enlistImg1.setFitWidth(800);
		enlistImg1.setPreserveRatio(true);

		ImageView enlistImg2 = new ImageView(new Image(getClass().getResourceAsStream("/resources/enlistment2.png")));
		enlistImg2.setFitWidth(800);
		enlistImg2.setPreserveRatio(true);		

		Text steps = new Text(
			"1. Browse available courses.\n" +
			"2. Ensure prerequisites are completed.\n" +
			"3. Add courses to your plan.\n" +
			"4. Review your planned schedule.\n" +
			"5. Wait for your scheduled enlistment date.\n" +
			"6. Submit — the system validates everything.\n"
		);
		steps.getStyleClass().add("section-text");
		
		// Wrap steps in an HBox so it aligns left even when the section is centered
		HBox stepsWrapper = new HBox(steps);
		stepsWrapper.setAlignment(Pos.CENTER_LEFT);

		box.getChildren().addAll(header, enlistImg1, enlistImg2, stepsWrapper);
		
		return box;
	}
}
