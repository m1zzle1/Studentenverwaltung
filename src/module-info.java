module studentApp1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
	requires javafx.base;

    opens application to javafx.graphics, javafx.fxml;
    opens view to javafx.fxml; 
    opens controller to javafx.fxml; 
}