package application.controllers;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Objects;

import application.database.JdbcDA;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;

public class LoginController {

    @FXML
    private TextField emailIdTextFiled;

    @FXML
    private Button loginButton;

    @FXML
    private TextField passwordTextField;

    @FXML
    private Button registerButton;

    private Socket socket;
    private DataOutputStream dout;
    private DataInputStream din;
    
    @FXML
    void loginButtonEvent(ActionEvent event) {
        Window owner = loginButton.getScene().getWindow();
        if(emailIdTextFiled.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error", "Please enter the user email");
            return;
        }
        if(passwordTextField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error", "Please enter the password");
            return;
        }

        String emailId = emailIdTextFiled.getText();
        String password = passwordTextField.getText();

        JdbcDA da = new JdbcDA();
        boolean flag = da.validation(emailId, password);

        if(flag) {
            try {
                // Create a socket connection to the server
                socket = new Socket("localhost", 8000);
                dout = new DataOutputStream(socket.getOutputStream());
                din = new DataInputStream(socket.getInputStream());

                // Send login credentials to the server
                dout.writeUTF("LOGIN_SUCCESS");
                dout.writeUTF("LOGIN " + emailId + " " + password);

                // Receive response from the server
                String response = din.readUTF();
                if (Objects.equals(response, "LOGIN_SUCCESS")) {
                    showAlert(Alert.AlertType.CONFIRMATION, owner, "Login Successful", "Welcome!");

                    // Proceed to the shopping cart page
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/Sample.fxml"));
                    Parent cartLoad = loader.load();
                    Stage cartStage = new Stage();
                    cartStage.setTitle("Hotel registration");
                    Scene cartScene = new Scene(cartLoad);
                    cartStage.setResizable(false);
                    cartStage.setScene(cartScene);
                    cartStage.show();

                    // Close the socket when the shopping cart stage is closed
                    cartStage.setOnCloseRequest(e -> {
                        try {
                            socket.close();
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    });
                } else {
                    showAlert(Alert.AlertType.ERROR, owner, "Login Failed", "Invalid credentials.");
                    socket.close();
                }
            } catch (ConnectException e) {
                showAlert(Alert.AlertType.ERROR, owner, "Connection Failed", "Cannot connect to the server. Please try again later.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    private static void showAlert(Alert.AlertType alt, Window win, String title, String msg) {
    	Alert alert = new Alert(alt);
    	alert.setTitle(title);
    	alert.setHeaderText(null);
    	alert.setContentText(msg);
    	alert.initOwner(win);
    	alert.showAndWait();
    }

    @FXML
    void registerButtonEvent(ActionEvent event) throws IOException {

    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/Register.fxml"));
    	Parent regLoad = loader.load();
    	
    	Stage st = new Stage();
    	st.setTitle("Registration Page");
    	Scene sc = new Scene(regLoad, 800,500);
    	st.setScene(sc);
    	st.show();
    }

}
