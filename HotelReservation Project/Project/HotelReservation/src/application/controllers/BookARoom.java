package application.controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import application.classes.Reservation;
import application.classes.room;
import application.database.JdbcDA;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class BookARoom {

    @FXML
    private TableView<room> available;

    @FXML
    private TableColumn<room, Integer> id;

    @FXML
    private TextField num_days;

    @FXML
    private DatePicker checkin;

    @FXML
    private DatePicker checkout;
    
    @FXML
    private TextField num_guest;

    @FXML
    private TableColumn<room, Double> price;

    @FXML
    private TextField rate;

    @FXML
    private TableView<room> selected;

    @FXML
    private TableColumn<room, Integer> sid;

    @FXML
    private TableColumn<room, Double> sprice;

    @FXML
    private TableColumn<room, String> stype;

    @FXML
    private TableColumn<room, String> type;

    private ObservableList<room> availableRooms;
    private ObservableList<room> SelectedRooms;
    private StringProperty formattedTotalRate = new SimpleStringProperty("0.00");
    //    private int numSingleRooms = 0;
    @FXML
    public void initialize() {
    	System.out.println("hey there");
    	SelectedRooms = FXCollections.observableArrayList();
    	
        rate.textProperty().bind(formattedTotalRate);

        checkin.valueProperty().addListener((observable, oldValue, newValue) -> updateNumDays());
        checkout.valueProperty().addListener((observable, oldValue, newValue) -> updateNumDays());
        
    }
    
    private void updateNumDays() {
        LocalDate checkinDate = checkin.getValue();
        LocalDate checkoutDate = checkout.getValue();

        if (checkinDate != null && checkoutDate != null) {
            long daysBetween = calculateDaysBetween(checkinDate, checkoutDate);
            num_days.setText(String.valueOf(daysBetween));
        }
    }
    
    private long calculateDaysBetween(LocalDate startDate, LocalDate endDate) {
        long days = 0;

        while (!startDate.isEqual(endDate)) {
            startDate = startDate.plusDays(1);
            days++;
        }

        return days;
    }
    
    @FXML
    void cancel(ActionEvent event) {
    	Node source = (Node) event.getSource();

        Stage stage = (Stage) source.getScene().getWindow();

        stage.close();
    }
    
    @FXML
    void next(ActionEvent event) {
    	if(num_days.getText() != null && !num_days.getText().isEmpty()
                && checkin.getValue() != null && checkout.getValue() != null
                && num_guest.getText() != null && !num_guest.getText().isEmpty() 
                && !SelectedRooms.isEmpty()) {
    		Reservation reservationInfo = new Reservation();
            reservationInfo.setSelectedRooms(SelectedRooms);
            reservationInfo.setCheckInDate(checkin.getValue());
            reservationInfo.setCheckOutDate(checkout.getValue());
            reservationInfo.setRate(Double.parseDouble(rate.getText()));
            reservationInfo.setNumberOfGuests(Integer.parseInt(num_guest.getText()));
            reservationInfo.setNumberOfDays(Integer.parseInt(num_days.getText()));
            reservationInfo.setBookingDate(LocalDate.now());
            
    		try {
    	        FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/customerRegistration.fxml"));
    	        Parent root = loader.load();
    	        
    	        // Set the "Book a Room" view as the root of the scene
    	        Scene scene = new Scene(root);
    	        CustomerInfo customerInfoController = loader.getController();

    	        // Pass the reservationInfo to the CustomerInfo controller
    	        customerInfoController.setReservationInfo(reservationInfo);
    	        // Create a new stage for the "Book a Room" view
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
    		Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Invalid Input");
            alert.setHeaderText(null);
            alert.setContentText("Please make sure all fields are entered and at least one room is selected.");
            alert.showAndWait();
    	}
    }

    @FXML
    void selectRoom(ActionEvent event) {
        room selectedRoom = available.getSelectionModel().getSelectedItem();
        int numGuests = Integer.parseInt(num_guest.getText());
        int numSingleRooms = 0;
        // Check if the selected room is of type "single"
        for (room room : SelectedRooms) {
			if (room.getRoomType().equalsIgnoreCase("single") || room.getRoomType().equalsIgnoreCase("deluxe")) {
				numSingleRooms++;
				}
			if (room.getRoomType().equalsIgnoreCase("double") ) {
            	numSingleRooms += 2 ;
            }
			if (room.getRoomType().equalsIgnoreCase("penthouse") ) {
            	numSingleRooms += 3 ;
            }
			}
        if (numGuests >= 1 && numGuests <= 2) {
        	if (selectedRoom.getRoomType().equalsIgnoreCase("single") || selectedRoom.getRoomType().equalsIgnoreCase("deluxe")) {
        		// Check if adding another single room would exceed the limit
        		if (numSingleRooms >= 1) {
	                // Display an alert indicating that only one "single" room is allowed
	                Alert alert = new Alert(AlertType.WARNING);
	                alert.setTitle("Room Limit Exceeded");
	                alert.setHeaderText(null);
	                alert.setContentText("Only one 'Single' or 'Deluxe' room is allowed for 1 or 2 guests.");
	                alert.showAndWait();
	                return; // Do not add the room to SelectedRooms
	                }
        		}
        	}
        if(numGuests >= 3 && numGuests <= 4) {
	        if (selectedRoom.getRoomType().equalsIgnoreCase("double") ) {
	        	if (numSingleRooms != 0) {
		            
		            
		            // Check if adding a double room would exceed the limit
		            if (numSingleRooms >= 1 ) {
		                // Display an alert indicating that only one "double" room is allowed
		                Alert alert = new Alert(AlertType.WARNING);
		                alert.setTitle("Room Limit Exceeded");
		                alert.setHeaderText(null);
		                alert.setContentText("Either one 'Double' room or two 'Single' or 'Deluxe' rooms are allowed for 3 or 4 guests.");
		                alert.showAndWait();
		                return; // Do not add the room to SelectedRooms
		            }
	            }
	        }
	        if (selectedRoom.getRoomType().equalsIgnoreCase("single") || selectedRoom.getRoomType().equalsIgnoreCase("deluxe")) {
        		// Check if adding another single room would exceed the limit
        		if (numSingleRooms >= 2) {
	                // Display an alert indicating that only one "single" room is allowed
	                Alert alert = new Alert(AlertType.WARNING);
	                alert.setTitle("Room Limit Exceeded");
	                alert.setHeaderText(null);
	                alert.setContentText("Only two 'Single' or 'Deluxe' room is allowed for 3 or 4 guests.");
	                alert.showAndWait();
	                return; 
	                }
        		}
        	
        }
        if(numGuests >= 5 && numGuests <= 6) {
	        if (selectedRoom.getRoomType().equalsIgnoreCase("double") ) {
	        	// Check if adding a double room would exceed the limit
		            if (numSingleRooms >= 2 ) {
		                // Display an alert indicating that only one "double" room is allowed
		                Alert alert = new Alert(AlertType.WARNING);
		                alert.setTitle("Room Limit Exceeded");
		                alert.setHeaderText(null);
		                alert.setContentText("Either one 'double' room or two 'single' rooms are allowed for 5 or 6 guests.");
		                alert.showAndWait();
		                return; // Do not add the room to SelectedRooms
		            }
	            
	        }
	        if (selectedRoom.getRoomType().equalsIgnoreCase("single")) {
        		// Check if adding another single room would exceed the limit
        		if (numSingleRooms >= 3) {
	                // Display an alert indicating that only one "single" room is allowed
	                Alert alert = new Alert(AlertType.WARNING);
	                alert.setTitle("Room Limit Exceeded");
	                alert.setHeaderText(null);
	                alert.setContentText("Only three 'Single' or 'Deluxe' room is allowed for 5 or 6 guests.");
	                alert.showAndWait();
	                return; 
	                }
        		}
	        if (selectedRoom.getRoomType().equalsIgnoreCase("penthouse")) {
        		// Check if adding another single room would exceed the limit
        		if (numSingleRooms >= 1) {
	                // Display an alert indicating that only one "single" room is allowed
	                Alert alert = new Alert(AlertType.WARNING);
	                alert.setTitle("Room Limit Exceeded");
	                alert.setHeaderText(null);
	                alert.setContentText("Either a combination of 'Single' and/or 'Double' rooms or a single 'Penthouse' is allowed ");
	                alert.showAndWait();
	                return; 
	                }
        		}
        	
        }
        if(numGuests >= 7) {
        	if (selectedRoom.getRoomType().equalsIgnoreCase("penthouse")){
        		if(((int)Math.ceil((double)numGuests/2) - numSingleRooms) < 3 ) {
		        	Alert alert = new Alert(AlertType.WARNING);
	                alert.setTitle("Room Limit Exceeded");
	                alert.setHeaderText(null);
	                alert.setContentText("Please select appropriane combination of rooms ");
	                alert.showAndWait();
	                return; 
		        }
        	}
        	if (selectedRoom.getRoomType().equalsIgnoreCase("single") || selectedRoom.getRoomType().equalsIgnoreCase("deluxe")){
        		if(((int)Math.ceil((double)numGuests/2) - numSingleRooms) < 1 ) {
		        	Alert alert = new Alert(AlertType.WARNING);
	                alert.setTitle("Room Limit Exceeded");
	                alert.setHeaderText(null);
	                alert.setContentText("Please select appropriane combination of rooms ");
	                alert.showAndWait();
	                return; 
		        }
        	}
        	if (selectedRoom.getRoomType().equalsIgnoreCase("double")){
        		if(((int)Math.ceil((double)numGuests/2) - numSingleRooms) < 2 ) {
		        	Alert alert = new Alert(AlertType.WARNING);
	                alert.setTitle("Room Limit Exceeded");
	                alert.setHeaderText(null);
	                alert.setContentText("Please select appropriane combination of rooms ");
	                alert.showAndWait();
	                return; 
		        }
        	}
        	
        }
     // Should the criteria be satisfied, include the chosen room in the SelectedRooms collection.
        SelectedRooms.add(selectedRoom);
        availableRooms.remove(selectedRoom);
        
        sid.setCellValueFactory(new PropertyValueFactory<>("roomId"));
        stype.setCellValueFactory(new PropertyValueFactory<>("roomType"));
        sprice.setCellValueFactory(new PropertyValueFactory<>("roomPrice"));
        
        selected.setItems(SelectedRooms);
        available.getSelectionModel().clearSelection();
        
        double calculatedTotalRate = SelectedRooms.stream()
                .mapToDouble(room -> room.getRoomPrice())
                .sum();
        
        // Format the total rate with two decimal places
        String formattedRate = String.format("%.2f", calculatedTotalRate);
        
        // Update the formattedTotalRate property
        formattedTotalRate.set(formattedRate);
        
    }

    @FXML
    void showAvailableRooms(ActionEvent event) {
    	if (num_guest.getText().isEmpty()) {
            // Create and configure an alert
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Empty Field");
            alert.setHeaderText(null);
            alert.setContentText("Please enter the number of guests.");
            
            // Show the alert
            alert.showAndWait();
            return;
        }
    	JdbcDA da = new JdbcDA();
    	int numGuests = Integer.parseInt(num_guest.getText());
    	List<room> availableRoomData = da.getAvailableRooms();
    	availableRooms = FXCollections.observableArrayList();
    	System.out.println("hey there");
    	
    	 for (room room : availableRoomData) {
    		// Verify whether the room category is "single" and the guest count is either 1 or 2.
    	        if ((room.getRoomType().equalsIgnoreCase("single") || room.getRoomType().equalsIgnoreCase("deluxe"))&& numGuests >= 1 && numGuests <= 2) {
    	            availableRooms.add(room);
    	        }
    	        if ((room.getRoomType().equalsIgnoreCase("single") || room.getRoomType().equalsIgnoreCase("deluxe") || room.getRoomType().equalsIgnoreCase("double")) && numGuests >= 3 && numGuests <= 4) {
    	        	availableRooms.add(room);
    	        }
    	        if (numGuests >= 5 ) {
    	        	availableRooms.add(room);
    	        }
    	        
    	    }
    	 for (room selectedRoom : SelectedRooms) {
    	        availableRooms.removeIf(room -> room.getRoomId() == selectedRoom.getRoomId());
    	    }
    	
    	id.setCellValueFactory(new PropertyValueFactory<>("roomId"));
    	type.setCellValueFactory(new PropertyValueFactory<>("roomType"));
    	price.setCellValueFactory(new PropertyValueFactory<>("roomPrice"));
    	
    	available.setItems(availableRooms);
    	    
    	}

}

