package frontend;

import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

import java.util.ArrayList;


import backend.CourseManager;
import backend.OfferedCourse;
import backend.RegSystem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class EnlistmentScreen extends VBox {
	private final int ITEMS_PER_PAGE = 10;
	private int currentPage = 0;
	private ObservableList<OfferedCourse> allCourses;
	private TableView<OfferedCourse> table;
	private ArrayList<OfferedCourse> offered = RegSystem.getAllCourses();
	private OfferedCourse oc = offered.get(0);
	private int cou = 0;
	
	public EnlistmentScreen() {
		setSpacing(20);
		setPadding(new Insets(20));
		setStyle("-fx-background-color: white;");

        		
        // ---------- CALENDAR AND ERROR SCREEN ----------
		
		
		GridPane calendar = new GridPane();
		calendar.setPadding(new Insets(20));
		for (int row = 0; row < 24; row++) {
		        for (int col = 0; col < 6; col++) {
		        // Example: a rectangle in each cell
				Rectangle cell = new Rectangle(80, 30); // width, height
				cell.setFill(Color.WHITE);
				cell.setStroke(Color.BLACK);
				
				StackPane cellPane = new StackPane();
		        cellPane.getChildren().add(cell);  // Rectangle is at bottom
		        calendar.add(cellPane, col, row);
	        }
		}
		
		HBox warnings = new HBox();
		warnings.setPadding(new Insets(20));
		Label lab = new Label("Warnings");
		warnings.getChildren().add(lab);
		
		HBox calendarAndWarnings = new HBox(calendar, warnings);
		 

        // ---------- ACTIVE ENLISTMENTS ----------
        Button b = new Button("Active Enlistment");
        
        b.setOnAction(e -> { RegSystem.fillTime(calendar, oc);});
        
        Button d = new Button("Increment course");
        d.setOnAction(e -> {
        	oc = offered.get(++cou);
        });
        // ---------- COURSE SEARCH ----------
        Button c = new Button("Course Search");
        VBox courseSearch = createCourseSearch(RegSystem.getAllCourses());
        c.setOnAction(e -> RegSystem.resetTime(calendar, oc));
        // Add all to layout
        getChildren().addAll(calendarAndWarnings,courseSearch, b,c,d);
	}
	
	
	private VBox createCourseSearch(ArrayList<OfferedCourse> allOfferedCourses) {
//		System.out.println(allOfferedCourses.size());

		// CREATE A TABLE
		 // Store full list
	    allCourses = FXCollections.observableArrayList(allOfferedCourses);

	    // --- Table Setup ---
	    table = new TableView<>();
	    table.setPrefHeight(400);
	    table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

	    TableColumn<OfferedCourse, String> codeCol = new TableColumn<>("Code");
	    codeCol.setCellValueFactory(new PropertyValueFactory<>("courseCode"));

	    TableColumn<OfferedCourse, String> sectionCol = new TableColumn<>("Section");
	    sectionCol.setCellValueFactory(new PropertyValueFactory<>("section"));

	    table.getColumns().addAll(codeCol, sectionCol);

	    for (TableColumn<OfferedCourse, ?> col : table.getColumns()) {
	        col.setReorderable(false);
	    }

	    // --- Pagination Controls ---
	    Button prevBtn = new Button("Previous");
	    Button nextBtn = new Button("Next");
	    Label pageLabel = new Label();

	    HBox pagination = new HBox(10, prevBtn, pageLabel, nextBtn);
	    pagination.setPadding(new Insets(10));

	    prevBtn.setOnAction(e -> {
	        if (currentPage > 0) {
	            currentPage--;
	            showPage(currentPage);
	            updatePageLabel(pageLabel);
	        }
	    });

	    nextBtn.setOnAction(e -> {
	        if ((currentPage + 1) * ITEMS_PER_PAGE < allCourses.size()) {
	            currentPage++;
	            showPage(currentPage);
	            updatePageLabel(pageLabel);
	        }
	    });

	    // --- Initial Page ---
	    currentPage = 0;
	    showPage(currentPage);
	    updatePageLabel(pageLabel);

	    // --- Layout ---
	    VBox box = new VBox(10, table, pagination);
	    VBox.setVgrow(table, Priority.ALWAYS);
	    box.setPadding(new Insets(10));

	    return box;
	}
	
	// --- Helper: show a page in the table ---
	private void showPage(int page) {
	    int fromIndex = page * ITEMS_PER_PAGE;
	    int toIndex = Math.min(fromIndex + ITEMS_PER_PAGE, allCourses.size());

	    if (fromIndex >= allCourses.size() || fromIndex < 0) return;

	    ObservableList<OfferedCourse> pageData =
	            FXCollections.observableArrayList(allCourses.subList(fromIndex, toIndex));
	    table.setItems(pageData);
	}
	
	// --- Helper: update page number label ---
	private void updatePageLabel(Label pageLabel) {
	    int totalPages = (int) Math.ceil((double) allCourses.size() / ITEMS_PER_PAGE);
	    pageLabel.setText("Page " + (currentPage + 1) + " of " + totalPages);
	}
}
