package application.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import application.classes.DeluxeRoom;
import application.classes.DoubleRoom;
import application.classes.Penthouse;
import application.classes.Reservation;
import application.classes.SingleRoom;
import application.classes.room;

public class JdbcDA {

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    String updateQuery = "UPDATE Rooms SET customer_id = ? WHERE Room_ID = ?";
    public static Integer userId;
    public static Integer cartId;
    public Double totalRate;
    public static Integer numRooms;
    private static final String DB_URL = "jdbc:sqlite:C:\\Users\\patel\\Desktop\\Shiv Patel\\Seneca Studies\\Sem 5\\APD\\Project\\New Project\\Project\\HotelReservationData.db";
    private static final String DB_TABLE = "Users";
    private static final String DB_CUST_TABLE = "customer";
    private static final String DB_RESE_TABLE = "reservation ";

    private static final String CREATE_TBL_QRY = "Create table if not exists " + DB_TABLE +
            " (id integer not null primary key autoincrement, full_name varchar(20) not null,"
            + " email_id varchar(50), password varchar(20))";
    private static final String INSERT_QRY = "insert into " + DB_TABLE + " (full_name,email_id,password) values (?,?,?)";
    private static final String SELECT_QRY = "select * from " + DB_TABLE + " where email_id = ? AND password = ?";
    private static final String CREATE_CUSTOMER_TBL_QRY = "CREATE TABLE if not exists " + DB_CUST_TABLE + " ("
            + "    Guest_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "    Title VARCHAR(5),"
            + "    First_name VARCHAR(20),"
            + "    Last_name VARCHAR(20),"
            + "    Address VARCHAR(100),"
            + "    Phone INTEGER,"
            + "    Email VARCHAR(40)"
            + ");";

    private static final String CREATE_RESERVATION_TBL_QRY = "CREATE TABLE if not exists " + DB_RESE_TABLE + "  ("
            + "    Book_id integer primary key autoincrement,"
            + "    Book_date date,"
            + "    Check_in  date,"
            + "    Check_out  date,"
            + "    customer_id  integer,"
            + "    num_guest  integer,"
            + "    num_days  integer,"
            + " foreign key(customer_id) references " + DB_CUST_TABLE + " (Guest_ID)"
            + ");";
    private static final String SELECT_ROOM_QRY = "select * from Rooms where customer_id = ?";
    private static final String SELECT_CUSTOMER_QRY = "select * from " + DB_CUST_TABLE + " where guest_id = ?";
    private static final String SELECT_RESERVATION_QRY = "select * from " + DB_RESE_TABLE;

    public void insertRecord(String fn, String email, String pass) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            PreparedStatement ps = conn.prepareStatement(CREATE_TBL_QRY);
            ps.execute();

            ps = conn.prepareStatement(INSERT_QRY);
            ps.setString(1, fn);
            ps.setString(2, email);
            ps.setString(3, pass);

            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public boolean validation(String email, String pass) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = conn.prepareStatement(SELECT_QRY)) {

            ps.setString(1, email);
            ps.setString(2, pass);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                userId = rs.getInt("id");
                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public String getBookedRoomTypesForCustomer(int cutomerId) {
        List<String> roomType = new ArrayList<>();
        String TypesOfRoom = "";
        totalRate = 0.00;
        try (Connection connection = DriverManager.getConnection(DB_URL)) {
            PreparedStatement ps = connection.prepareStatement(SELECT_ROOM_QRY);
            ps.setInt(1, cutomerId);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String type = rs.getString("roomType");
                double rate = rs.getInt("roomPrice");
                System.out.println(totalRate);
                roomType.add(type);
                totalRate += rate;
                System.out.println(totalRate);
            }
            numRooms = roomType.size();
            TypesOfRoom = roomType.stream().distinct().collect(Collectors.joining(", "));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return TypesOfRoom;
    }

    public List<Reservation> getBookedRooms() {
        List<Reservation> bookedRooms = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(DB_URL)) {
            PreparedStatement ps = connection.prepareStatement(SELECT_RESERVATION_QRY);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int bookingid = rs.getInt("Book_id");
                LocalDate bookDate = rs.getDate("Book_date").toLocalDate();
                LocalDate checkInDate = rs.getDate("Check_in").toLocalDate();
                LocalDate checkOutDate = rs.getDate("Check_out").toLocalDate();
                int customerId = rs.getInt("customer_id");
                int numGuest = rs.getInt("num_guest");
                int numDays = rs.getInt("num_days");
                String customerName = "";
                PreparedStatement pss = connection.prepareStatement(SELECT_CUSTOMER_QRY);
                pss.setInt(1, customerId);
                ResultSet result = pss.executeQuery();
                if (result.next()) {
                    String firstName = result.getString("First_name");
                    String lastName = result.getString("Last_name");
                    customerName = firstName + lastName;
                }

                String typeOfRoom = getBookedRoomTypesForCustomer(customerId);
                System.out.println(bookDate);

                Reservation reservation = new Reservation();
                reservation.setBookingDate(bookDate);
                reservation.setCheckInDate(checkInDate);
                reservation.setCheckOutDate(checkOutDate);
                reservation.setCustomerName(customerName);
                reservation.setNumberOfGuests(numGuest);
                reservation.setNumberOfDays(numDays);
                reservation.setTypeOfRooms(typeOfRoom);
                reservation.setBookingId(bookingid);
                reservation.setNumberOfRooms(numRooms);
                bookedRooms.add(reservation);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bookedRooms;
    }

    public List<room> getAvailableRooms() {
        List<room> availableRooms = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            String sql = "SELECT * FROM Rooms WHERE available = true";
            ResultSet resultSet = stmt.executeQuery(sql);
            while (resultSet.next()) {
                int roomId = resultSet.getInt("roomID");
                String roomType = resultSet.getString("roomType");
                double roomPrice = resultSet.getDouble("roomPrice");
                if ("Single".equals(roomType)) {
                    availableRooms.add(new SingleRoom(roomId, roomPrice));
                } else if ("Double".equals(roomType)) {
                    availableRooms.add(new DoubleRoom(roomId, roomPrice));
                } else if ("Deluxe".equals(roomType)) {
                    availableRooms.add(new DeluxeRoom(roomId, roomPrice));
                } else if ("Penthouse".equals(roomType)) {
                    availableRooms.add(new Penthouse(roomId, roomPrice));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return availableRooms;
    }

    public int insertCustomer(String title, String firstName, String lastName, String address, int phone, String email) {
        int guestId = -1;
        String insertQuery = "INSERT INTO customer (Title, First_name, Last_name, Address, Phone, Email) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, title);
            ps.setString(2, firstName);
            ps.setString(3, lastName);
            ps.setString(4, address);
            ps.setInt(5, phone);
            ps.setString(6, email);

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        guestId = generatedKeys.getInt(1);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return guestId;
    }

    public void updateReservation(LocalDate bookDate, LocalDate checkInDate, LocalDate checkOutDate, int customerId, int numGuests, int numDays) {
        String insertQuery = "INSERT INTO reservation (Book_date, Check_in, Check_out, customer_id, num_guest, num_days) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = conn.prepareStatement(insertQuery)) {

            ps.setDate(1, java.sql.Date.valueOf(bookDate));
            ps.setDate(2, java.sql.Date.valueOf(checkInDate));
            ps.setDate(3, java.sql.Date.valueOf(checkOutDate));
            ps.setInt(4, customerId);
            ps.setInt(5, numGuests);
            ps.setInt(6, numDays);

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateRoomAvailabilityForGuest(int guestId, List<Integer> roomIds) {
        String updateQuery = "UPDATE Rooms SET customer_id = ? WHERE Room_ID = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = conn.prepareStatement(updateQuery)) {

            for (int roomId : roomIds) {
                ps.setInt(1, guestId);  // Set the customer ID for the room
                ps.setInt(2, roomId);   // Set the room ID
                ps.executeUpdate();     // Execute the update query for each room
            }

        } catch (SQLException e) {
            e.printStackTrace();  // Print stack trace in case of SQL exception
        }
    }

    public Reservation getReservationById(int bookingId) {
        Reservation reservation = null;
        String query = "SELECT * FROM reservation WHERE Book_id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, bookingId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                LocalDate bookDate = rs.getDate("Book_date").toLocalDate();
                LocalDate checkInDate = rs.getDate("Check_in").toLocalDate();
                LocalDate checkOutDate = rs.getDate("Check_out").toLocalDate();
                int customerId = rs.getInt("customer_id");
                int numGuest = rs.getInt("num_guest");
                int numDays = rs.getInt("num_days");
                String customerName = "";
                PreparedStatement pss = conn.prepareStatement(SELECT_CUSTOMER_QRY);
                pss.setInt(1, customerId);
                ResultSet result = pss.executeQuery();
                if (result.next()) {
                    String firstName = result.getString("First_name");
                    String lastName = result.getString("Last_name");
                    customerName = firstName + lastName;
                }
                String typeOfRoom = getBookedRoomTypesForCustomer(customerId);

                List<room> customerRooms = getRoomsForCustomer(customerId);

                reservation = new Reservation();
                reservation.setBookingDate(bookDate);
                reservation.setCheckInDate(checkInDate);
                reservation.setCheckOutDate(checkOutDate);
                reservation.setCustomerName(customerName);
                reservation.setNumberOfGuests(numGuest);
                reservation.setNumberOfDays(numDays);
                reservation.setTypeOfRooms(typeOfRoom);
                reservation.setBookingId(bookingId);
                reservation.setNumberOfRooms(numRooms);
                reservation.setRate(totalRate);
                reservation.setSelectedRooms(customerRooms);
                reservation.setCustomerId(customerId);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reservation;
    }

    private List<room> getRoomsForCustomer(int customerId) {
        List<room> customerRooms = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(DB_URL)) {
            String selectRoomsQuery = "SELECT * FROM Rooms WHERE customer_id = ?";
            PreparedStatement ps = connection.prepareStatement(selectRoomsQuery);
            ps.setInt(1, customerId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int roomId = rs.getInt("roomID");
                String roomType = rs.getString("roomType");
                double roomPrice = rs.getDouble("roomPrice");

                if ("Single".equals(roomType)) {
                    customerRooms.add(new SingleRoom(roomId, roomPrice));
                } else if ("Double".equals(roomType)) {
                    customerRooms.add(new DoubleRoom(roomId, roomPrice));
                } else if ("Deluxe".equals(roomType)) {
                    customerRooms.add(new DeluxeRoom(roomId, roomPrice));
                } else if ("Penthouse".equals(roomType)) {
                    customerRooms.add(new Penthouse(roomId, roomPrice));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customerRooms;
    }

    public void removeReservation(int bookingId) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DriverManager.getConnection(DB_URL);
            conn.setAutoCommit(false);

            String findRoomsQuery = "SELECT roomID FROM Rooms WHERE customer_id = (SELECT customer_id FROM reservation WHERE Book_id = ?)";
            ps = conn.prepareStatement(findRoomsQuery);
            ps.setInt(1, bookingId);
            ResultSet rs = ps.executeQuery();
            List<Integer> roomIds = new ArrayList<>();
            while (rs.next()) {
                roomIds.add(rs.getInt("roomID"));
            }

            String deleteReservationQuery = "DELETE FROM reservation WHERE Book_id = ?";
            ps = conn.prepareStatement(deleteReservationQuery);
            ps.setInt(1, bookingId);
            ps.executeUpdate();

            if (!roomIds.isEmpty()) {
                String updateRoomAvailabilityQuery = "UPDATE Rooms SET available = true, customer_id = NULL WHERE roomID = ?";
                ps = conn.prepareStatement(updateRoomAvailabilityQuery);
                for (Integer roomId : roomIds) {
                    ps.setInt(1, roomId);
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            conn.commit();
        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) ps.close();
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void updateRoomAvailability(List<Integer> roomIds, int custId) {
        try (Connection connection = DriverManager.getConnection(DB_URL)) {
            String updateRoomQuery = "UPDATE Rooms SET available = ?, customer_id = ? WHERE roomid = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateRoomQuery);

            preparedStatement.setBoolean(1, true);
            preparedStatement.setNull(2, Types.INTEGER);
            for (int roomId : roomIds) {
                preparedStatement.setInt(3, roomId);
                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Reservation> searchBookingsByName(String name) {
        List<Reservation> results = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM " + DB_RESE_TABLE + " r JOIN " + DB_CUST_TABLE + " c ON r.customer_id = c.Guest_ID WHERE c.First_name LIKE ? OR c.Last_name LIKE ?");
            ps.setString(1, "%" + name + "%");
            ps.setString(2, "%" + name + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                results.add(new Reservation());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    private static final String CREATE_ROOMS_TBL_QRY = "CREATE TABLE IF NOT EXISTS Rooms ("
            + "    roomID INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "    roomType VARCHAR(20) NOT NULL,"
            + "    roomPrice DOUBLE NOT NULL,"
            + "    available BOOLEAN DEFAULT TRUE,"
            + "    customer_id INTEGER,"
            + "    FOREIGN KEY(customer_id) REFERENCES customer(Guest_ID)"
            + ");";

    public void insertRoom(int roomId, String roomType, double roomPrice, boolean available, Integer customerId) {
        try (Connection connection = DriverManager.getConnection(DB_URL)) {
            String insertQuery = "INSERT OR IGNORE INTO Rooms (roomID, roomType, roomPrice, available, customer_id) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);

            preparedStatement.setInt(1, roomId);
            preparedStatement.setString(2, roomType);
            preparedStatement.setDouble(3, roomPrice);
            preparedStatement.setBoolean(4, available);
            preparedStatement.setObject(5, customerId, Types.INTEGER);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate(CREATE_TBL_QRY);
            }

            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate(CREATE_CUSTOMER_TBL_QRY);
            }

            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate(CREATE_RESERVATION_TBL_QRY);
            }

            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate(CREATE_ROOMS_TBL_QRY);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}