package database.tables;

import com.google.gson.Gson;
import database.DB_Connection;
import models.Admin;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Nick Giovanopoulos
 */
public class AdminTable {

    /**
     Creates a new table named "admins" in the database with the following columns: admin_id (primary key), username, password, firstname, lastname, email, gender, birthDate, telephone, and image.
     @throws SQLException if there is an error executing the SQL statement.
     @throws ClassNotFoundException if the database driver class cannot be found.
     */
    public void createAdminTable() throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String query = "CREATE TABLE admins "
                + "(   admin_id INTEGER not NULL AUTO_INCREMENT,"
                + "    username VARCHAR(30) not null,"
                + "    password VARCHAR(32) not null,"
                + "    firstname VARCHAR(32) not null,"
                + "    lastname VARCHAR(32) not null,"
                + "    email VARCHAR(32) not null,"
                + "    gender VARCHAR(32) not null,"
                + "    birthDate VARCHAR(32) not null,"
                + "    telephone VARCHAR(32) not null,"
                + "    image BLOB,"
                + "    PRIMARY KEY (admin_id))";
        stmt.execute(query);
        stmt.close();
    }

    /**
     Inserts a new Admin object into the "admins" table in the database.
     @param admin the Admin object to be added to the database.
     @throws ClassNotFoundException if the database driver class cannot be found.
     */
    public void addAdmin(Admin admin) throws ClassNotFoundException {
        try {
            Connection con = DB_Connection.getConnection();
            Statement stmt = con.createStatement();
            String insertQuery = "INSERT INTO admins (username,password,firstname,lastname,email,gender,birthDate,telephone)"
                    + " VALUES ("
                    + "'" + admin.getUsername() + "',"
                    + "'" + admin.getPassword() + "',"
                    + "'" + admin.getFirstname() + "',"
                    + "'" + admin.getLastname() + "',"
                    + "'" + admin.getEmail() + "',"
                    + "'" + admin.getGender() + "',"
                    + "'" + admin.getBirthDate() + "',"
                    + "'" + admin.getTelephone() + "'"
                    + ")";
            //stmt.execute(table);
            System.out.println(insertQuery);
            stmt.executeUpdate(insertQuery);
            System.out.println("# The user was successfully added in the database.");
            /* Get the member id from the database and set it to the member */
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(AdminTable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     Retrieves the Admin object with the given username and password from the "admins" table in the database and converts it to a Java object.
     @param username the username of the Admin object to retrieve.
     @param password the password of the Admin object to retrieve.
     @return the Admin object with the given username and password, or null if no such object exists.
     @throws SQLException if there is an error executing the SQL statement.
     @throws ClassNotFoundException if the database driver class cannot be found.
     */
    public Admin databaseToAdmin(String username, String password) throws SQLException, ClassNotFoundException{
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ResultSet rs;
        try {
            String query = "SELECT * FROM admins WHERE username='"+username+"' AND password='"+password+"'";
            rs = stmt.executeQuery(query);
            while(rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                Gson gson = new Gson();
                Admin admin = gson.fromJson(json, Admin.class);
                return admin;
            }
        } catch (Exception e) {
            System.err.println("databaseToAdmin: Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

    /**
     Converts an Admin object to a JSON string.
     @param user the Admin object to be converted to JSON.
     @return the JSON string representing the Admin object.
     */
    public String adminToJSON(Admin user){
        Gson gson = new Gson();
        String json = gson.toJson(user, Admin.class);
        return json;
    }

    /**
     Converts a JSON string to an Admin object.
     @param json a JSON string representing an Admin object
     @return an Admin object
     */
    public Admin jsonToAdmin(String json){
        Gson gson = new Gson();
        Admin admin = gson.fromJson(json, Admin.class);
        return admin;
    }

    /**
     Adds a profile image for an Admin to the database based on its fullname.
     @param admin_id the ID of the Admin to add the profile image for
     @param firstName the first name of the Admin
     @param lastName the last name of the Admin
     @throws SQLException if a database access error occurs
     @throws ClassNotFoundException if the database driver class cannot be found
     */
    public void addAdminProfileImg(int admin_id, String firstName, String lastName) throws SQLException, ClassNotFoundException{
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
            String sql = "UPDATE admins SET image=? WHERE admin_id=?";
            stmt = con.prepareStatement(sql);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            InputStream is = new ByteArrayInputStream(baos.toByteArray());
            stmt.setBinaryStream(1, is, baos.size());
            stmt.setInt(2, admin_id);
            stmt.executeUpdate();
        } catch (Exception e) {
            System.err.println("addAdminProfileImg: Got an exception! ");
            System.err.println(e.getMessage());
        }
    }

    /**
     Retrieves the profile image for an Admin from the database.
     @param admin_id the ID of the Admin to retrieve the profile image for
     @return a BufferedImage object representing the Admin's profile image
     @throws SQLException if a database access error occurs
     @throws ClassNotFoundException if the database driver class cannot be found
     */
    public BufferedImage getAdminProfileImg(int admin_id) throws SQLException, ClassNotFoundException{
        Connection con = DB_Connection.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT image FROM admins WHERE admin_id=?";
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, admin_id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                InputStream is = rs.getBinaryStream("image");
                BufferedImage image = ImageIO.read(is);
                return image;
            }
        } catch (Exception e) {
            System.err.println("getAdminProfileImg: Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

}