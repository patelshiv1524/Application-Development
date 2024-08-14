package application.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import application.classes.room;
import application.database.JdbcDA;

public class AvailableRoomController {

    @FXML private TableColumn<room, Integer> roomId;
    @FXML private TableColumn<room, String> RoomType;
    @FXML private TableView<room> available;

    private JdbcDA jdbcDA = new JdbcDA();

    @FXML
    void initialize() {
        roomId.setCellValueFactory(new PropertyValueFactory<>("roomId"));
        RoomType.setCellValueFactory(new PropertyValueFactory<>("roomType"));
        refreshAvailableRooms();
    }

    @FXML
    void close(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
    
    public void refreshAvailableRooms() {
        ObservableList<room> availableRooms = FXCollections.observableArrayList(jdbcDA.getAvailableRooms());
        available.setItems(availableRooms);
    }
}
