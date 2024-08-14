package application.controllers;

import java.util.ArrayList;
import java.util.List;

import application.classes.Reservation;
import application.classes.room;
import application.database.JdbcDA;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CustomerInfo {

    @FXML
    private TextField Title;

    @FXML
    private TextField address;

    @FXML
    private TextField email;

    @FXML
    private TextField fname;

    @FXML
    private TextField lname;

    @FXML
    private TextField phone;

    private Reservation reservationInfo;

    @FXML
    void cancel(ActionEvent event) {
        closeWindow(event);
    }

    public void setReservationInfo(Reservation reservationInfo) {
        this.reservationInfo = reservationInfo;
    }

    @FXML
    void confirm(ActionEvent event) {
        if (validateFields()) {  // Validate the input fields to ensure all data is entered correctly
            JdbcDA da = new JdbcDA();  // Create an instance of JdbcDA to interact with the database
            try {
                // Clean up the phone number input to remove any non-digit characters
                String phoneNumber = phone.getText().replaceAll("[^\\d]", "");
                
                // Insert customer data into the database and retrieve the generated guestId
                int guestId = da.insertCustomer(Title.getText(), fname.getText(), lname.getText(),
                        address.getText(), Integer.parseInt(phoneNumber), email.getText());

                if (guestId != -1) {  // If guestId is valid, proceed with storing the reservation
                    // Update the reservation details in the database
                    da.updateReservation(reservationInfo.getBookingDate(), reservationInfo.getCheckInDate(),
                            reservationInfo.getCheckOutDate(), guestId, reservationInfo.getNumberOfGuests(),
                            reservationInfo.getNumberOfDays());

                    // Prepare a list of room IDs to update their availability
                    List<Integer> roomIds = new ArrayList<>();
                    for (room selectedRoom : reservationInfo.getSelectedRooms()) {
                        roomIds.add(selectedRoom.getRoomId());
                    }
                    da.updateRoomAvailabilityForGuest(guestId, roomIds);  // Update room availability in the database

                    // Calculate the total rate for the booked rooms
                    double totalRate = reservationInfo.getSelectedRooms().stream()
                                                      .mapToDouble(room -> room.getRate())
                                                      .sum();
                    // Calculate the tax (for example, 13% tax)
                    double tax = totalRate * 0.13;  
                    // Calculate the final amount including tax
                    double finalAmount = totalRate + tax;

                    // Show a confirmation dialog with detailed booking information
                    Alert confirmationAlert = new Alert(AlertType.CONFIRMATION);
                    confirmationAlert.setTitle("Booking Confirmation");
                    confirmationAlert.setHeaderText("Booking Details");
                    confirmationAlert.setContentText(
                            "Rooms Booked: " + roomIds.size() +
                            "\nTotal Price: $" + String.format("%.2f", totalRate) +
                            "\nTax: $" + String.format("%.2f", tax) +
                            "\nFinal Amount: $" + String.format("%.2f", finalAmount) +
                            "\n\nDo you want to proceed?");

                    // Add Confirm and Cancel buttons to the alert
                    ButtonType confirmButton = new ButtonType("Confirm");
                    ButtonType cancelButton = new ButtonType("Cancel");
                    confirmationAlert.getButtonTypes().setAll(confirmButton, cancelButton);

                    // Handle the user's response to the confirmation dialog
                    if (confirmationAlert.showAndWait().orElse(ButtonType.CANCEL) == confirmButton) {
                        // If the user confirms, show a success message and close the window
                        Alert successAlert = new Alert(AlertType.INFORMATION);
                        successAlert.setTitle("Booking Successful");
                        successAlert.setHeaderText(null);
                        successAlert.setContentText("Thank you for booking with us! We look forward to your stay.");
                        successAlert.showAndWait();
                        closeWindow(event);  // Close the window after confirmation
                    }
                }
            } catch (NumberFormatException e) {
                // Handle any number format exceptions, such as invalid phone number input
                showAlert("Invalid Phone Number", "Phone number should be a numeric value.");
            }
        }
    }

    private boolean validateFields() {
        if (!phone.getText().matches("\\d{10}")) {
            showAlert("Invalid Phone Number", "Phone number should be a 10-digit number.");
            return false;
        }

        if (!email.getText().matches("\\b[\\w.%-]+@[-.\\w]+\\.[A-Za-z]{2,4}\\b")) {
            showAlert("Invalid Email Address", "Email should be in the format 'something@something.com'.");
            return false;
        }

        return true; // All fields are valid
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Invalid Input");
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void closeWindow(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}