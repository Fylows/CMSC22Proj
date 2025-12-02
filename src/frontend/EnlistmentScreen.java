package frontend;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class EnlistmentScreen extends VBox {
	
	public EnlistmentScreen() {
		setSpacing(20);
		setPadding(new Insets(20));
		setStyle("-fx-background-color: white;");
        		
        // ---------- CALENDAR ----------
        Button a = new Button("Calendar");

        // ---------- ACTIVE ENLISTMENTS ----------
        Button b = new Button("Active Enlistment");
        
        // ---------- COURSE SEARCH ----------
        Button c = new Button("Course Search");

        // Add all to layout
        getChildren().addAll(a,b,c);
	}
	
}
