package application.controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class BookingIdController {

    @FXML
    private TextField bookingID;

    @FXML
    void confirm(ActionEvent event) {
        String bookingIdValue = bookingID.getText();

        if (isInteger(bookingIdValue)) {
            int bookingId = Integer.parseInt(bookingIdValue);

            try {
    	        FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/BillService.fxml"));
    	        Parent root = loader.load();
    	        
    	        // Set the "Book a Room" view as the root of the scene
    	        BillServiceController billID = loader.getController();
    	        billID.setBookingId(bookingId);
    	        Scene scene = new Scene(root);
    	        billID.initialize();
    	        // Send the reservationInfo to the CustomerInfo controller and subsequently generate a new stage for the "Book a Room" display.
    	        Stage stage = new Stage();
    	        stage.setScene(scene);
    	        stage.setTitle("Customer Information");
    	        
    	        // Show the new stage
    	        stage.show();
    	    } catch (IOException e) {
    	        e.printStackTrace();
    	    }
            Node source = (Node) event.getSource();

            Stage stage = (Stage) source.getScene().getWindow();

            stage.close();
        } else {
            // Show an alert if the booking ID is not a valid integer
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Invalid Booking ID");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a valid booking ID (integer).");
            alert.showAndWait();
        }
    }

    private boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
