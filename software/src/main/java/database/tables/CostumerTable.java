package database.tables;

import com.google.gson.Gson;
import database.DB_Connection;
import models.Costumer;
import org.json.JSONArray;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Nick Giovanopoulos
 */
public class CostumerTable {

    /**
     Creates a new table named "costumers" in the database with the following fields:
     costumerId (auto-incremented integer, primary key)
     username (varchar(30), not null)
     password (varchar(32), not null)
     firstname (varchar(32), not null)
     lastname (varchar(32), not null)
     email (varchar(32), not null)
     gender (varchar(32), not null)
     birthDate (varchar(32), not null)
     telephone (varchar(32), not null)
     money (integer, not null)
     image (BLOB)
     @throws SQLException if a database access error occurs
     @throws ClassNotFoundException if the driver class is not found
     */
    public void createCostumerTable() throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String query = "CREATE TABLE costumers "
                + "(   costumerId INTEGER not NULL AUTO_INCREMENT,"
                + "    username VARCHAR(30) not null,"
                + "    password VARCHAR(32) not null,"
                + "    firstname VARCHAR(32) not null,"
                + "    lastname VARCHAR(32) not null,"
                + "    email VARCHAR(32) not null,"
                + "    gender VARCHAR(32) not null,"
                + "    birthDate VARCHAR(32) not null,"
                + "    telephone VARCHAR(32) not null,"
                + "    money INTEGER not null,"
                + "    image BLOB,"
                + "    PRIMARY KEY (costumerId))";
        stmt.execute(query);
        stmt.close();
    }

    /**
     Retrieves a list of all customers from the database and returns it as a JSON string.
     @return a JSON string representing a list of all customers in the database
     @throws SQLException if a database access error occurs
     @throws ClassNotFoundException if the database driver class cannot be found
     */
    public String databaseToCostumers() throws SQLException, ClassNotFoundException{
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ResultSet rs;
        ArrayList<Costumer> costumers = new ArrayList<>();
        Gson gson = new Gson();
        try {
            String query = "SELECT * FROM costumers";
            rs = stmt.executeQuery(query);
            JSONArray jsonArray = new JSONArray();
            while(rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                Costumer costumer = gson.fromJson(json, Costumer.class);
                costumers.add(costumer);
            }
        } catch (Exception e) {
            System.err.println("databaseToCostumers: Got an exception! ");
            System.err.println(e.getMessage());
        }
        return gson.toJson(costumers);
    }

    public Costumer checkError(String pedio,int number) throws SQLException, ClassNotFoundException{
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ResultSet rs = null;
        try {

            String query1 = "SELECT * FROM costumers WHERE username='"+pedio+"'";
            String query2 = "SELECT * FROM costumers WHERE email='"+pedio+"'";
            String query3 = "SELECT * FROM costumers WHERE telephone='"+pedio+"'";
            if(number==1) rs = stmt.executeQuery(query1);
            else if (number==2) rs = stmt.executeQuery(query2);
            else if (number==3)  rs= stmt.executeQuery(query3);
            while(rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                Gson gson = new Gson();
                return gson.fromJson(json, Costumer.class);
            }
        } catch (Exception e) {
            System.err.println("databaseToCostumer: Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

    /**
     Adds a profile image for the costumer with the given ID to the database. The profile image is a 200x200
     BufferedImage with the user's initials drawn on it, and is saved as a PNG image in a BLOB field in the
     database.
     @param costumerId the ID of the costumer to add the profile image for
     @param firstName the first name of the costumer
     @param lastName the last name of the costumer
     @throws SQLException if a database access error occurs
     @throws ClassNotFoundException if the driver class is not found
     */
    public void addProfileImg(int costumerId, String firstName, String lastName) throws SQLException, ClassNotFoundException{
        Connection con = DB_Connection.getConnection();
        PreparedStatement stmt = null;
        int size = 200;
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        // Draw the user's initials on the image
        Graphics2D g2d = image.createGraphics();
        g2d.setColor(Color.BLUE);
        g2d.fillRect(0, 0, size, size);
        g2d.setColor(Color.WHITE);
        Font font = new Font("Arial", Font.BOLD, 100);
        g2d.setFont(font);
        String initials = String.valueOf(firstName.charAt(0)) + String.valueOf(lastName.charAt(0));
        int stringWidth = g2d.getFontMetrics().stringWidth(initials);
        int x = (size - stringWidth) / 2;
        int y = (size + font.getSize()) / 2;
        g2d.drawString(initials, x, y);
        g2d.dispose();
        try {
            String sql = "UPDATE costumers SET image=? WHERE costumerId=?";
            stmt = con.prepareStatement(sql);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            InputStream is = new ByteArrayInputStream(baos.toByteArray());
            stmt.setBinaryStream(1, is, baos.size());
            stmt.setInt(2, costumerId);
            stmt.executeUpdate();
        } catch (Exception e) {
            System.err.println("addProfileImg: Got an exception! ");
            System.err.println(e.getMessage());
        }
    }

    /**
     Adds a new costumer to the database using the information provided in the Costumer object. The costumer
     is inserted into the "costumers" table in the database, and a profile image is added using the
     addProfileImg() method.
     @param costumer the Costumer object to add to the database
     @throws ClassNotFoundException if the driver class is not found
     @throws RuntimeException if a SQLException occurs
     */
    public void addCostumer(Costumer costumer) throws ClassNotFoundException {
        try {
            Connection con = DB_Connection.getConnection();
            Statement stmt = con.createStatement();
            String insertQuery = "INSERT INTO costumers (username,password,firstname,lastname,email,gender,birthDate,telephone,money)"
                    + " VALUES ("
                    + "'" + costumer.getUsername() + "',"
                    + "'" + costumer.getPassword() + "',"
                    + "'" + costumer.getFirstname() + "',"
                    + "'" + costumer.getLastname() + "',"
                    + "'" + costumer.getEmail() + "',"
                    + "'" + costumer.getGender() + "',"
                    + "'" + costumer.getBirthDate() + "',"
                    + "'" + costumer.getTelephone() + "',"
                    + "'" + costumer.getMoney() + "'"
                    + ")";
            System.out.println(insertQuery);
            int rowsAffected = stmt.executeUpdate(insertQuery, Statement.RETURN_GENERATED_KEYS);
            if (rowsAffected == 1) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    int costumerId = rs.getInt(1);
                    System.out.println("# The costumer with ID " + costumerId + " was successfully added in the database.");
                    addProfileImg(costumerId,costumer.getFirstname(),costumer.getLastname());
                }
            }
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     Retrieves a customer object from the database by their username.
     @param username the username of the customer to retrieve
     @return a Costumer object representing the customer with the given username, or null if no such customer exists
     @throws SQLException if an error occurs while accessing the database
     @throws ClassNotFoundException if the Costumer class cannot be found
     */
    public Costumer getCostumerByUsername(String username) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ResultSet rs;
        try {
            String query = "SELECT * FROM costumers WHERE username='"+username+"'";
            rs = stmt.executeQuery(query);
            while(rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                Gson gson = new Gson();
                Costumer costumer = gson.fromJson(json, Costumer.class);
                return costumer;
            }
        } catch (Exception e) {
            System.err.println("getCostumerByUsername: Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

    /**
     Retrieves a customer object from the database by their email address.
     @param email the email address of the customer to retrieve
     @return a Costumer object representing the customer with the given email address, or null if no such customer exists
     @throws SQLException if an error occurs while accessing the database
     @throws ClassNotFoundException if the Costumer class cannot be found
     */
    public Costumer getCostumerByEmail(String email) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ResultSet rs;
        try {
            String query = "SELECT * FROM costumers WHERE email='"+email+"'";
            rs = stmt.executeQuery(query);
            while(rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                Gson gson = new Gson();
                Costumer costumer = gson.fromJson(json, Costumer.class);
                return costumer;
            }
        } catch (Exception e) {
            System.err.println("getCostumerByEmail: Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

    /**
     Retrieves a customer object from the database by their telephone number.
     @param telephone the telephone number of the customer to retrieve
     @return a Costumer object representing the customer with the given telephone number, or null if no such customer exists
     @throws SQLException if an error occurs while accessing the database
     @throws ClassNotFoundException if the Costumer class cannot be found
     */
    public Costumer getCostumerByTelephone(String telephone) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ResultSet rs;
        try {
            String query = "SELECT * FROM costumers WHERE telephone='" + telephone + "'";
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                Gson gson = new Gson();
                Costumer costumer = gson.fromJson(json, Costumer.class);
                return costumer;
            }
        } catch (Exception e) {
            System.err.println("getCostumerByTelephone: Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

    /**
     Retrieves a Costumer object from the database using the given username and password.
     @param username the username of the customer to retrieve
     @param password the password of the customer to retrieve
     @return the Costumer object retrieved from the database, or null if no customer was found
     @throws SQLException if a database access error occurs
     @throws ClassNotFoundException if the database driver class cannot be found
     */
    public Costumer databaseToCostumer(String username, String password) throws SQLException, ClassNotFoundException{
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ResultSet rs;
        try {
            String query = "SELECT * FROM costumers WHERE username='"+username+"' AND password='"+password+"'";
            rs = stmt.executeQuery(query);
            while(rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                Gson gson = new Gson();
                Costumer costumer = gson.fromJson(json, Costumer.class);
                return costumer;
            }
        } catch (Exception e) {
            System.err.println("databaseToCostumer: Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

    /**
     Decreases the amount of money in the given customer's account by the given amount.
     @param costumerId the ID of the customer whose account is to be updated
     @param money_arg the amount of money to subtract from the customer's account
     @return 0 if the money was successfully decreased, or -1 if the decrease was unsuccessful
     */
    public int decreaseMoney(int costumerId, int money_arg) {
        int result = -1;
        try {
            Connection con = DB_Connection.getConnection();
            PreparedStatement stmt = con.prepareStatement("UPDATE costumers SET money=money-? WHERE costumerId=? AND money >= ?");
            stmt.setInt(1, money_arg);
            stmt.setInt(2, costumerId);
            stmt.setInt(3, money_arg);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Money successfully decreased.");
                result = 0;
            } else System.out.println("Money could not be decreased.");
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(Costumer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Costumer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    /**
     Retrieves the profile image of a specific costumer from the database.
     @param costumerId the ID of the costumer whose profile image is to be retrieved
     @return the profile image of the costumer as a BufferedImage object, or null if the costumer ID is invalid or if an error occurred while retrieving the image
     @throws SQLException if an error occurred while accessing the database
     @throws ClassNotFoundException if the database driver class cannot be found
     */
    public BufferedImage getProfileImg(int costumerId) throws SQLException, ClassNotFoundException{
        Connection con = DB_Connection.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT image FROM costumers WHERE costumerId=?";
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, costumerId);
            rs = stmt.executeQuery();
            if (rs.next()) {
                InputStream is = rs.getBinaryStream("image");
                BufferedImage image = ImageIO.read(is);
                return image;
            }
        } catch (Exception e) {
            System.err.println("getCustomerImage: Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

    /**
     Converts a Costumer object to a JSON string using the Gson library.
     @param costumer the Costumer object to be converted to a JSON string
     @return the JSON representation of the Costumer object
     */
    public String CostumerToJSON(Costumer costumer){
        Gson gson = new Gson();
        String json = gson.toJson(costumer, Costumer.class);
        return json;
    }

    /**
     Adds a new costumer to the database using a JSON string as input.
     @param json the JSON string containing the information of the costumer to be added
     @throws ClassNotFoundException if the database driver class cannot be found
     @throws SQLException if an error occurred while accessing the database
     */
    public void addCostumerJSON(String json) throws ClassNotFoundException{
        Costumer costumer =jsonToCostumer(json);
        addCostumer(costumer);
    }

    /**
     Converts a JSON string to a Costumer object using the Gson library.
     @param json the JSON string to be converted to a Costumer object
     @return the Costumer object represented by the JSON string
     */
    public Costumer jsonToCostumer(String json){
        Gson gson = new Gson();
        Costumer costumer = gson.fromJson(json, Costumer.class);
        return costumer;
    }

}