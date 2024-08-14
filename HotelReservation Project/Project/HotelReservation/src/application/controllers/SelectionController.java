package application.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class SelectionController {

    @FXML
    private void handleGuestBooking(ActionEvent event) throws Exception {
        // Load the WelcomeKiosk.fxml when guest self booking is chosen
        Parent root = FXMLLoader.load(getClass().getResource("/application/WelcomeKiosk.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    @FXML
    private void handleAdminLogin(ActionEvent event) throws Exception {
        // Load the Login.fxml for admin login
        Parent root = FXMLLoader.load(getClass().getResource("/application/Login.fxml")); 
        Scene scene = new Scene(root);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
}
