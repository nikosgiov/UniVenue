package database.tables;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import database.DB_Connection;
import models.Reservation;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Nick Giovanopoulos
 */
public class ReservationTable {

    /**
     Creates a new table in the database for storing reservations. The table has columns for reservation ID, date, time slot, state, room ID, customer ID, and transaction ID. Foreign keys are set for linking the room, customer, and transaction tables.
     @throws SQLException if there is an error in the SQL syntax or the database connection is closed
     @throws ClassNotFoundException if the database driver class cannot be found
     */
    public void createReservationTable() throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String query = "CREATE TABLE reservations "
                + "(   reservationId INTEGER not NULL AUTO_INCREMENT,"
                + "    date VARCHAR(32) not null,"
                + "    slot INTEGER not null,"
                + "    state VARCHAR(32) not null,"
                + "    roomId INTEGER not null,"
                + "    costumerId INTEGER not null,"
                + "    transactionId INTEGER not null,"
                + "    FOREIGN KEY (roomId) REFERENCES rooms(roomId), "
                + "    FOREIGN KEY (costumerId) REFERENCES costumers(costumerId), "
                + "    FOREIGN KEY (transactionId) REFERENCES transactions(transactionId), "
                + "    PRIMARY KEY (reservationId))";
;
        stmt.execute(query);
        stmt.close();
    }

    /**
     Returns an ArrayList of time slots that are reserved for a given room and date.
     @param roomId the ID of the room to check
     @param date the date to check reservations for (in the format "yyyy-MM-dd")
     @return an ArrayList of integers representing the reserved time slots for the specified room and date
     */
    public ArrayList<Integer> getSlots(int roomId, String date) {
        ArrayList<Integer> reservedSlots = new ArrayList<>();
        try {
            Connection con = DB_Connection.getConnection();
            Statement stmt = con.createStatement();
            String query = "SELECT slot FROM reservations WHERE roomId = " + roomId + " AND date = '" + date + "'";
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()) {
                reservedSlots.add(rs.getInt("slot"));
            }
            con.close();
        } catch(SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return reservedSlots;
    }

    /**
     Adds a new reservation to the reservations table in the database.
     @param reservation the Reservation object to add to the table
     @throws ClassNotFoundException if the database driver class cannot be found
     */
    public void addReservation(Reservation reservation) throws ClassNotFoundException {
        try {
            Connection con = DB_Connection.getConnection();
            Statement stmt = con.createStatement();
            String insertQuery = "INSERT INTO reservations (date,slot,roomId,state,costumerId,transactionId)"
                    + " VALUES ("
                    + "'" + reservation.getDate() + "',"
                    + "'" + reservation.getSlot() + "',"
                    + "'" + reservation.getRoomId() + "',"
                    + "'" + reservation.getState() + "',"
                    + "'" + reservation.getCostumerId() + "',"
                    + "'" + reservation.getTransactionId() + "'"
                    + ")";
            //stmt.execute(table);
            stmt.executeUpdate(insertQuery);
            System.out.println("# The reservation was successfully added in the database.");
            /* Get the member id from the database and set it to the member */
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(ReservationTable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     Deletes a reservation record from the database and updates the customer's money field if applicable.
     @param reservationId The ID of the reservation to be deleted.
     @throws ClassNotFoundException If the class for the database driver cannot be found.
     @throws SQLException If a database access error occurs or this method is called on a closed connection.
     */
    public void deleteReservation(int reservationId) throws ClassNotFoundException {
        try {
            Connection con = DB_Connection.getConnection();
            Statement stmt = con.createStatement();
            // Retrieve the transactionId and costumerId associated with the reservation
            String selectIdsQuery = "SELECT transactionId, costumerId FROM reservations WHERE reservationId = " + reservationId;
            ResultSet rs = stmt.executeQuery(selectIdsQuery);
            int transactionId = -1;
            int costumerId = -1;
            if (rs.next()) {
                transactionId = rs.getInt("transactionId");
                costumerId = rs.getInt("costumerId");
            }
            rs.close();
            // Retrieve the amount associated with the transaction, if it exists
            int amount = -1;
            if (transactionId != -1) {
                String selectAmountQuery = "SELECT amount FROM transactions WHERE transactionId = " + transactionId;
                rs = stmt.executeQuery(selectAmountQuery);
                if (rs.next()) amount = rs.getInt("amount");
                rs.close();
            }
            // Delete the reservation record
            String deleteReservationQuery = "DELETE FROM reservations WHERE reservationId = " + reservationId;
            stmt.executeUpdate(deleteReservationQuery);
            System.out.println("# The reservation was successfully deleted from the database.");
            // Update the costumer's money field if the transaction exists and has a positive amount
            if (transactionId != -1 && amount > 0) {
                String updateCostumerQuery = "UPDATE costumers SET money = money + " + amount + " WHERE costumerId = " + costumerId;
                stmt.executeUpdate(updateCostumerQuery);
                System.out.println("# The costumer's money was successfully updated in the database.");
            }
            // Delete the transaction record if it exists
            if (transactionId != -1) {
                String deleteTransactionQuery = "DELETE FROM transactions WHERE transactionId = " + transactionId;
                stmt.executeUpdate(deleteTransactionQuery);
                System.out.println("# The associated transaction was successfully deleted from the database.");
            }
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(ReservationTable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     Converts reservation records in the database associated with a given customer ID to Reservation objects and returns them in an ArrayList.
     @param costumerId The ID of the customer whose reservations are to be retrieved.
     @return An ArrayList of Reservation objects containing the customer's reservations.
     @throws ClassNotFoundException If the class for the database driver cannot be found.
     @throws SQLException If a database access error occurs or this method is called on a closed connection.
     */
    public ArrayList<Reservation> databaseToReservations(int costumerId) throws SQLException, ClassNotFoundException{
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ArrayList<Reservation> reservations = new ArrayList<>();
        ResultSet rs;
        try {
            String query = "SELECT * FROM reservations WHERE costumerId='"+costumerId+"'";
            rs = stmt.executeQuery(query);
            while(rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                Gson gson = new Gson();
                Reservation reservation = gson.fromJson(json, Reservation.class);
                reservations.add(reservation);
            }
        } catch (Exception e) {
            System.err.println("databaseToReservations: Got an exception! ");
            System.err.println(e.getMessage());
        }
        return reservations;
    }

    /**
     Retrieves pending reservation records from the database and returns them as a JSON string.
     @return A JSON string containing information about pending reservations.
     @throws ClassNotFoundException If the class for the database driver cannot be found.
     @throws SQLException If a database access error occurs or this method is called on a closed connection.
     */
    public String pendingReservations() throws SQLException, ClassNotFoundException{
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        JsonArray jsonArray = new JsonArray();
        ResultSet rs;
        try {
            String query = "SELECT reservations.reservationId, reservations.date, reservations.slot, rooms.name AS room_name, costumers.username, costumers.firstname, costumers.lastname " +
                    "FROM reservations " +
                    "JOIN rooms ON reservations.roomId = rooms.roomId " +
                    "JOIN costumers ON reservations.costumerId = costumers.costumerId " +
                    "WHERE reservations.state = 'PENDING'";
            rs = stmt.executeQuery(query);
            while(rs.next()) {
                JsonObject reservation = new JsonObject();
                reservation.addProperty("reservationId", rs.getInt("reservationId"));
                reservation.addProperty("date", rs.getString("date"));
                reservation.addProperty("slot", rs.getString("slot"));
                reservation.addProperty("name", rs.getString("room_name"));
                reservation.addProperty("username", rs.getString("username"));
                String fullName = rs.getString("firstname") + " " + rs.getString("lastname");
                reservation.addProperty("fullName", fullName);
                jsonArray.add(reservation);
            }
        } catch (Exception e) {
            System.err.println("pendingReservations: Got an exception! ");
            System.err.println(e.getMessage());
        }
        return jsonArray.toString();
    }

    /**
     Updates a reservation's state to "COMPLETED" in the database.
     @param reservationId the ID of the reservation to be updated
     @return 0 if the reservation was successfully updated, 1 otherwise
     @throws SQLException if a database access error occurs
     @throws ClassNotFoundException if the JDBC driver class is not found
     */
    public int updateReservationToCompleted(int reservationId) throws SQLException, ClassNotFoundException {
        int result = 1;
        try (Connection con = DB_Connection.getConnection()) {
            String updateQuery = "UPDATE reservations SET state = 'COMPLETED' WHERE reservationId = " + reservationId;
            int rowsAffected = con.createStatement().executeUpdate(updateQuery);
            if (rowsAffected == 1) {
                result = 0;
                System.out.println("Reservation with reservationId " + reservationId + " updated to COMPLETED.");
            } else { System.out.println("Reservation update failed."); }
        } catch (SQLException e) { System.err.println("Error updating reservation: " + e.getMessage()); }
        return result;
    }

    /**
     Retrieves a JSON string representation of all reservations made by a given customer ID from the database.
     @param costumerId the ID of the customer whose reservations are to be retrieved
     @return a JSON string representation of the reservations made by the specified customer
     @throws SQLException if a database access error occurs
     @throws ClassNotFoundException if the JDBC driver class is not found
     */
    public String databaseToReservationsJSON(int costumerId) throws SQLException, ClassNotFoundException{
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ResultSet rs;
        String json = "";
        try {
            String query = "SELECT reservations.reservationId, reservations.date AS reservationDate, reservations.state, reservations.slot,  " +
                    "rooms.roomId, rooms.name AS roomName, rooms.image, " +
                    "transactions.date AS transactionDate, transactions.amount, transactions.paymentType " +
                    "FROM reservations " +
                    "INNER JOIN rooms ON reservations.roomId = rooms.roomId " +
                    "INNER JOIN transactions ON reservations.transactionId = transactions.transactionId " +
                    "WHERE reservations.costumerId = '"+costumerId+"'";
            rs = stmt.executeQuery(query);
            JSONArray jsonArray = new JSONArray();
            while(rs.next()) {
                JSONObject obj = new JSONObject();
                obj.put("reservationId", rs.getInt("reservationId"));
                obj.put("reservationDate", rs.getString("reservationDate"));
                obj.put("state", rs.getString("state"));
                obj.put("roomId", rs.getInt("roomId"));
                obj.put("roomName", rs.getString("roomName"));
                obj.put("image", rs.getString("image"));
                obj.put("transactionDate", rs.getString("transactionDate"));
                obj.put("amount", rs.getDouble("amount"));
                obj.put("paymentType", rs.getString("paymentType"));
                obj.put("slot",rs.getInt("slot"));
                jsonArray.put(obj);
            }
            json = jsonArray.toString();
        } catch (Exception e) {
            System.err.println("databaseToReservationsJSON: Got an exception! ");
            System.err.println(e.getMessage());
        }
        return json;
    }

    /**
     Retrieves a JSON string representation of all reservations from the database.
     @return a JSON string representation of all reservations in the database
     @throws SQLException if a database access error occurs
     @throws ClassNotFoundException if the JDBC driver class is not found
     */
    public String databaseToReservations() throws SQLException, ClassNotFoundException{
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ResultSet rs;
        ArrayList<Reservation> reservations = new ArrayList<>();
        Gson gson = new Gson();
        try {
            String query = "SELECT * FROM reservations";
            rs = stmt.executeQuery(query);
            JSONArray jsonArray = new JSONArray();
            while(rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                Reservation reservation = gson.fromJson(json, Reservation.class);
                reservations.add(reservation);
            }
        } catch (Exception e) {
            System.err.println("databaseToReservations: Got an exception! ");
            System.err.println(e.getMessage());
        }
        return gson.toJson(reservations);
    }

    /**
     Converts a Reservation object to a JSON string.
     @param reservation the Reservation object to be converted
     @return a JSON string representation of the Reservation object
     */
    public String reservationToJSON(Reservation reservation){
        Gson gson = new Gson();
        String json = gson.toJson(reservation, Reservation.class);
        return json;
    }

    /**
     Takes a JSON string representing a Reservation object, converts it to a Reservation object using Gson,
     and adds the Reservation to the database by calling addReservation().
     @param json A JSON string representing a Reservation object.
     @throws ClassNotFoundException If the JDBC driver class cannot be found.
     */
    public void addReservationJSON(String json) throws ClassNotFoundException{
        Reservation reservation =jsonToReservation(json);
        addReservation(reservation);
    }

    /**
     Takes a JSON string representing a Reservation object and converts it to a Reservation object using Gson.
     @param json A JSON string representing a Reservation object.
     @return A Reservation object created from the JSON string.
     */
    public Reservation jsonToReservation(String json){
        Gson gson = new Gson();
        Reservation reservation = gson.fromJson(json, Reservation.class);
        return reservation;
    }

}