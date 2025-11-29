module CMSC22Proj {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens main to javafx.base, javafx.fxml, javafx.graphics;
    opens frontend to javafx.base, javafx.fxml, javafx.graphics;

}