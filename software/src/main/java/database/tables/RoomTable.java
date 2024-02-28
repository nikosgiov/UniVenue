package database.tables;

import com.google.gson.Gson;
import database.DB_Connection;
import models.Room;

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
public class RoomTable {

    /**
     Creates a table for rooms in the database if it does not already exist.
     @throws SQLException if there is an error executing the SQL statement
     @throws ClassNotFoundException if the JDBC driver class is not found
     */
    public void createRoomTable() throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String query = "CREATE TABLE rooms "
                + "(   roomId INTEGER not NULL AUTO_INCREMENT,"
                + "    name VARCHAR(33) not null,"
                + "    capacity INTEGER not null,"
                + "    pricePerHour INTEGER not null,"
                + "    description TEXT not null,"
                + "    slogan VARCHAR(100) not null,"
                + "    location VARCHAR(32) not null,"
                + "    image VARCHAR(200),"
                + "    PRIMARY KEY (roomId))";
        stmt.execute(query);
        stmt.close();
    }

    /**
     Adds a room to the database.
     @param room the Room object to be added to the database
     @throws ClassNotFoundException if the JDBC driver class is not found
     */
    public void addRoom(Room room) throws ClassNotFoundException {
        try {
            Connection con = DB_Connection.getConnection();
            Statement stmt = con.createStatement();
            String insertQuery = "INSERT INTO rooms (name,capacity,pricePerHour,description,slogan,location,image)"
                    + " VALUES ("
                    + "'" + room.getName() + "',"
                    + "'" + room.getCapacity() + "',"
                    + "'" + room.getPricePerHour() + "',"
                    + "'" + room.getDescription() + "',"
                    + "'" + room.getSlogan() + "',"
                    + "'" + room.getLocation() + "',"
                    + "'" + room.getImage() + "'"
                    + ")";
            //stmt.execute(table);
            System.out.println(insertQuery);
            stmt.executeUpdate(insertQuery);
            System.out.println("# The room was successfully added in the database.");
            /* Get the member id from the database and set it to the member */
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(RoomTable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     Retrieves a Room object from the database based on its room ID.
     @param roomId the ID of the room to be retrieved
     @return the Room object retrieved from the database, or null if the room was not found
     @throws SQLException if there is an error executing the SQL statement
     @throws ClassNotFoundException if the JDBC driver class is not found
     */
    public Room databaseToRoom(int roomId) throws SQLException, ClassNotFoundException{
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ResultSet rs;
        try {
            String query = "SELECT * FROM rooms WHERE roomId ='"+roomId+"'";
            rs = stmt.executeQuery(query);
            while(rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                Gson gson = new Gson();
                Room room = gson.fromJson(json, Room.class);
                return room;
            }
        } catch (Exception e) {
            System.err.println("databaseToRoom: Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

    /**
     Retrieves a list of all Room objects stored in the database.
     @return an ArrayList of Room objects retrieved from the database
     @throws SQLException if there is an error executing the SQL statement
     @throws ClassNotFoundException if the JDBC driver class is not found
     */
    public ArrayList<Room> databaseToRooms() throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ResultSet rs;
        ArrayList<Room> rooms = new ArrayList<>();
        try {
            String query = "SELECT * FROM rooms";
            rs = stmt.executeQuery(query);
            while(rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                Gson gson = new Gson();
                Room room = gson.fromJson(json, Room.class);
                rooms.add(room);
            }
        } catch (Exception e) {
            System.err.println("databaseToRooms: Got an exception! ");
            System.err.println(e.getMessage());
        }
        return rooms;
    }

    /**
     Converts a Room object to its corresponding JSON string representation using the Gson library.
     @param room The Room object to be converted.
     @return The JSON string representation of the Room object.
     */
    public String roomToJSON(Room room){
        Gson gson = new Gson();
        String json = gson.toJson(room, Room.class);
        return json;
    }

    /**
     Parses a JSON string representation of a Room object and adds the Room to the database.
     @param json The JSON string representation of a Room object.
     @throws ClassNotFoundException if the JDBC driver class cannot be found.
     */
    public void addRoomJSON(String json) throws ClassNotFoundException{
        Room room =jsonToRoom(json);
        addRoom(room);
    }

    /**
     Parses a JSON string representation of a Room object and returns the corresponding Room object.
     @param json The JSON string representation of a Room object.
     @return The Room object represented by the JSON string.
     */
    public Room jsonToRoom(String json){
        Gson gson = new Gson();
        Room room = gson.fromJson(json, Room.class);
        return room;
    }

}