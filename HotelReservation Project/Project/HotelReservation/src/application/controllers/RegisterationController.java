package application.controllers;

import application.database.JdbcDA;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.stage.Window;

public class RegisterationController {

    @FXML
    private Button cancelBtn;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField fullnameTextField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button submitBtn;

    @FXML
    void cancelButtonEvent(ActionEvent event) {
    	Node source = (Node) event.getSource();

        Stage stage = (Stage) source.getScene().getWindow();

        stage.close();
    }

    @FXML
    void submitButtonEvent(ActionEvent event) {
    	Window owner = submitBtn.getScene().getWindow();
    	if(fullnameTextField.getText().isEmpty() || emailTextField.getText().isEmpty() || passwordField.getText().isEmpty()) {
    		showAlert(Alert.AlertType.ERROR, owner, "Form Error", "Please enter the User Name");
    	}
    	else {
    	String fullName = fullnameTextField.getText();
    	String emailId = emailTextField.getText();
    	String password = passwordField.getText();
    	
    	Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirm Bill Payment");
        alert.setHeaderText("Please confirm the bill payment.");
        alert.setContentText("Are you sure you want to confirm and proceed with the payment?");

        ButtonType confirmButtonType = new ButtonType("Confirm");
        ButtonType cancelButtonType = new ButtonType("Cancel");

        alert.getButtonTypes().setAll(confirmButtonType, cancelButtonType);

        alert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == confirmButtonType) {
                Node source = (Node) event.getSource();

                Stage stage = (Stage) source.getScene().getWindow();

                stage.close();
            }
        });
    	JdbcDA da = new JdbcDA();
    	da.insertRecord(fullName, emailId, password);
    	
    	
    	Node source = (Node) event.getSource();

        Stage stage = (Stage) source.getScene().getWindow();

        stage.close();
    	}
    }
    private static void showAlert(Alert.AlertType alertT, Window owner, String title, String message) {
    	Alert alert = new Alert(alertT);
    	alert.setTitle(title);
    	alert.setHeaderText(null);
    	alert.setContentText(message);
    	alert.initOwner(owner);
    	alert.show();
    }
}
