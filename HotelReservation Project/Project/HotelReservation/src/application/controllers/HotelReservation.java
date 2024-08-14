package application.controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HotelReservation {

    @FXML
    void availableBookings(ActionEvent event) {
    	try {
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/AvailableRooms.fxml"));
	        Parent root = loader.load();
	        
	        Scene scene = new Scene(root);
	        
	        Stage stage = new Stage();
	        stage.setScene(scene);
	        stage.setTitle("Available Rooms");
	        
	        stage.show();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
    	
    }

    @FXML
    void billService(ActionEvent event) {
    	try {
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/BookingId.fxml"));
	        Parent root = loader.load();
	        
	        Scene scene = new Scene(root);
	        
	        Stage stage = new Stage();
	        stage.setScene(scene);
	        stage.setTitle("Bill Service");
	        
	        stage.show();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
    }

    @FXML
    void bookaRoom(ActionEvent event) {
    	    try {
    	        FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/bookaroom.fxml"));
    	        Parent root = loader.load();
    	        
    	        // Set the "Book a Room" view like the root of the scene
    	        Scene scene = new Scene(root);
    	        
    	        // Create a new stage for the "Book a Room" view
    	        Stage stage = new Stage();
    	        stage.setScene(scene);
    	        stage.setTitle("Book a Room");
    	        
    	        // Show the new stage
    	        stage.show();
    	    } catch (IOException e) {
    	        e.printStackTrace();
    	    }
    	
    }

    @FXML
    void currentBookings(ActionEvent event) {
    	try {
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/CurrentBookings.fxml"));
	        Parent root = loader.load();
	        
	        Scene scene = new Scene(root);
	        
	        Stage stage = new Stage();
	        stage.setScene(scene);
	        stage.setTitle("Available Rooms");
	        
	        stage.show();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
    }

    @FXML
    void exit(ActionEvent event) {
    	Node source = (Node) event.getSource();

        Stage stage = (Stage) source.getScene().getWindow();

        stage.close();
    }

}
