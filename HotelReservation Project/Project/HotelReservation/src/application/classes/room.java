package application.classes;

public class room {
    private int roomId;         // Unique identifier for the room
    private String roomType;    // Type of the room (e.g., Single, Double, Deluxe)
    private double rate;        // Rate for the room per night

    // Constructor to initialize the room object
    public room(int roomId, String roomType, double rate) {
        this.roomId = roomId;
        this.roomType = roomType;
        this.rate = rate;
    }

    // Getter for roomId
    public int getRoomId() {
        return roomId;
    }

    // Setter for roomId
    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    // Getter for roomType
    public String getRoomType() {
        return roomType;
    }

    // Setter for roomType
    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    // Getter for rate
    public double getRate() {
        return rate;
    }
    
    public double getRoomPrice() {
    	return rate;
    };

    // Setter for rate
    public void setRate(double rate) {
        this.rate = rate;
    }
}