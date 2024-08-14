package application.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import application.classes.Reservation;
import application.database.JdbcDA;

public class BookedRoomsController {

    @FXML private TableView<Reservation> BookedRooms;
    @FXML private TableColumn<Reservation, String> custname;
    @FXML private TableColumn<Reservation, Integer> id;
    @FXML private TableColumn<Reservation, Integer> numDays;
    @FXML private TableColumn<Reservation, Integer> numRoom;
    @FXML private TableColumn<Reservation, String> roomType;
    @FXML private TextField searchField;
    @FXML private Label numBooked;
    
    private JdbcDA da = new JdbcDA();
    private AvailableRoomController availableRoomController;

    public void setAvailableRoomController(AvailableRoomController availableRoomController) {
        this.availableRoomController = availableRoomController;
    }

    @FXML
    private void initialize() {
        setupColumnBindings();
        loadBookings();
    }

    private void setupColumnBindings() {
        custname.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        id.setCellValueFactory(new PropertyValueFactory<>("bookingId"));
        numDays.setCellValueFactory(new PropertyValueFactory<>("numberOfDays"));
        numRoom.setCellValueFactory(new PropertyValueFactory<>("numberOfRooms"));
        roomType.setCellValueFactory(new PropertyValueFactory<>("typeOfRooms"));
    }

    private void loadBookings() {
        ObservableList<Reservation> bookedRooms = FXCollections.observableArrayList(da.getBookedRooms());
        BookedRooms.setItems(bookedRooms);
        numBooked.setText(String.valueOf(BookedRooms.getItems().size()));
    }

    @FXML
    void searchBooking(ActionEvent event) {
        String searchText = searchField.getText();
        if (!searchText.isEmpty()) {
            ObservableList<Reservation> filteredBookings = FXCollections.observableArrayList(da.searchBookingsByName(searchText));
            BookedRooms.setItems(filteredBookings);
        } else {
            loadBookings();
        }
    }

    @FXML
    void deleteBooking(ActionEvent event) {
        Reservation selected = BookedRooms.getSelectionModel().getSelectedItem();
        if (selected != null) {
            da.removeReservation(selected.getBookingId());
            loadBookings();
            if (availableRoomController != null) {
                availableRoomController.refreshAvailableRooms();
            }
        }
    }

    @FXML
    void close(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}
