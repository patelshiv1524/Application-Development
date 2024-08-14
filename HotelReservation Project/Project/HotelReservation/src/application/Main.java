package application;

import application.database.JdbcDA;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/Options.fxml"));
            Parent root = loader.load();
            primaryStage.setTitle("Patel Hotel Group");
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();

            JdbcDA jdbcDA = new JdbcDA();
            // Initialize various room types
            initializeRoomData(jdbcDA);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
    	JdbcDA jdbcDA = new JdbcDA();
        jdbcDA.createDatabase();
        launch(args);
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    private void initializeRoomData(JdbcDA jdbcDA) {
        // Example rooms
        jdbcDA.insertRoom(1, "Single", 100.00, true, null);
        jdbcDA.insertRoom(2, "Double", 150.00, true, null);
        jdbcDA.insertRoom(3, "Deluxe", 200.00, true, null);
        jdbcDA.insertRoom(4, "Penthouse", 500.00, true, null);
        jdbcDA.insertRoom(5, "Single", 100.00, false, null);  // Room not available
        jdbcDA.insertRoom(6, "Double", 150.00, true, null);
        jdbcDA.insertRoom(7, "Deluxe", 220.00, true, null);
        jdbcDA.insertRoom(8, "Penthouse", 550.00, true, null);
        jdbcDA.insertRoom(9, "Double", 160.00, true, null);
        jdbcDA.insertRoom(10, "Single", 110.00, true, null);
        // Additional rooms can be added similarly
    }
}














