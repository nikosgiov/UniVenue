package database;

import com.google.gson.JsonObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
/**
 *
 * @author Nick Giovanopoulos
 */
public class DB_Connection {
    
    private static final String url = "jdbc:mysql://localhost";
    private static final String databaseName = "HY351_PROJECT";
    private static final int port = 3306;
    private static final String username = "root";
    private static final String password = "";

    /**
     * Attempts to establish a database connection
     *
     * @return a connection to the database
     * @throws SQLException
     * @throws java.lang.ClassNotFoundException
     */
    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(url + ":" + port + "/" + databaseName, username, password);
    }

    /**
     Establishes a connection to a MySQL database without specifying a specific database name.
     @return the Connection object representing the connection to the database.
     @throws SQLException if a database access error occurs.
     @throws ClassNotFoundException if the JDBC driver class is not found.
     */
    public static Connection getInitialConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(url + ":" + port, username, password);
    }

    /**
     Prints the results of a ResultSet to the console.
     @param rs the ResultSet containing the data to print.
     @throws SQLException if a database access error occurs.
     */
    public static void printResults(ResultSet rs) throws SQLException {
        ResultSetMetaData metadata = rs.getMetaData();
        int columnCount = metadata.getColumnCount();
        String row = "";
        for (int i = 1; i <= columnCount; i++) {
            String name = metadata.getColumnName(i);
            String value = rs.getString(i);
            System.out.println(name + " " + value);
        }
    }

    /**
     Converts the results of a ResultSet to a JSON string.
     @param rs the ResultSet containing the data to convert.
     @return a JSON string representing the ResultSet data.
     @throws SQLException if a database access error occurs.
     */
    public static String getResultsToJSON(ResultSet rs) throws SQLException {
        ResultSetMetaData metadata = rs.getMetaData();
        int columnCount = metadata.getColumnCount();
        JsonObject object = new JsonObject();
        String row = "";
        for (int i = 1; i <= columnCount; i++) {
            String name = metadata.getColumnName(i);
            String value = rs.getString(i);
            object.addProperty(name,value);
        }
        return object.toString();
    }

    /**
     Returns a JsonObject representing a single row of the provided ResultSet object.
     The JsonObject includes the column names as keys and the column values as values.
     @param rs the ResultSet object to convert to a JsonObject
     @return a JsonObject representing a single row of the provided ResultSet object
     @throws SQLException if an error occurs while accessing the ResultSet object
     */
     public static JsonObject getResultsToJSONObject(ResultSet rs) throws SQLException {
        ResultSetMetaData metadata = rs.getMetaData();
        int columnCount = metadata.getColumnCount();
        JsonObject object = new JsonObject();
        String row = "";
        for (int i = 1; i <= columnCount; i++) {
            String name = metadata.getColumnName(i);
            String value = rs.getString(i);
            object.addProperty(name,value);
        }
        return object;
    }
}
