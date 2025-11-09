module CMSC22Proj {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens application to javafx.base, javafx.fxml, javafx.graphics;
}