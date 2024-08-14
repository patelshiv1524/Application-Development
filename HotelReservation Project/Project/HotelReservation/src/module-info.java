module HotelReservation {
	requires javafx.controls;
	requires javafx.fxml;
	requires java.sql;
	requires javafx.graphics;
	requires javafx.base;
	
	opens application to javafx.graphics, javafx.fxml;
	exports application.controllers to javafx.fxml;
	opens application.controllers to javafx.fxml;
	opens application.classes to javafx.base;
}
