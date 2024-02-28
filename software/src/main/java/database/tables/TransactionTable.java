package database.tables;

import com.google.gson.Gson;
import database.DB_Connection;
import models.Transaction;
import org.json.JSONArray;

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
public class TransactionTable {

    /**
     Creates the 'transactions' table in the database with columns: transactionId, date, amount, and paymentType.
     @throws SQLException if a database access error occurs or this method is called on a closed connection.
     @throws ClassNotFoundException if the class cannot be found.
     */
    public void createTransactionTable() throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String query = "CREATE TABLE transactions "
                + "(   transactionId INTEGER not NULL AUTO_INCREMENT,"
                + "    date VARCHAR(33) not null,"
                + "    amount INTEGER not null,"
                + "    paymentType VARCHAR(33) not null,"
                + "    PRIMARY KEY (transactionId))";
        stmt.execute(query);
        stmt.close();
    }

    /**
     Adds a new transaction to the 'transactions' table in the database.
     @param transaction the Transaction object to be added to the table.
     @return the auto-generated transactionId of the added transaction, or -1 if the transaction was not added.
     @throws ClassNotFoundException if the class cannot be found.
     */
    public int addTransaction(Transaction transaction) throws ClassNotFoundException {
        int transactionId = -1;
        try {
            Connection con = DB_Connection.getConnection();
            Statement stmt = con.createStatement();
            String insertQuery = "INSERT INTO transactions (date,amount,paymentType)"
                    + " VALUES ("
                    + "'" + transaction.getDate() + "',"
                    + "'" + transaction.getAmount() + "',"
                    + "'" + transaction.getPaymentType() + "'"
                    + ")";
            System.out.println(insertQuery);
            stmt.executeUpdate(insertQuery, Statement.RETURN_GENERATED_KEYS);

            // Retrieve the generated keys and get the transactionId
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                transactionId = generatedKeys.getInt(1);
            }

            System.out.println("# The transaction was successfully added in the database with transactionId " + transactionId);
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(Transaction.class.getName()).log(Level.SEVERE, null, ex);
        }
        return transactionId;
    }

    /**
     Retrieves all transactions from the 'transactions' table in the database and converts them to a JSON string.
     @return a JSON string representation of an ArrayList of Transaction objects.
     @throws SQLException if a database access error occurs or this method is called on a closed connection.
     @throws ClassNotFoundException if the class cannot be found.
     */
    public String databaseToTransactions() throws SQLException, ClassNotFoundException{
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ResultSet rs;
        ArrayList<Transaction> transactions = new ArrayList<>();
        Gson gson = new Gson();
        try {
            String query = "SELECT * FROM transactions";
            rs = stmt.executeQuery(query);
            JSONArray jsonArray = new JSONArray();
            while(rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                Transaction transaction = gson.fromJson(json, Transaction.class);
                transactions.add(transaction);
            }
        } catch (Exception e) {
            System.err.println("databaseToTransactions: Got an exception! ");
            System.err.println(e.getMessage());
        }
        return gson.toJson(transactions);
    }

    /**
     Retrieves a transaction with the specified ID from the "transactions" table in the connected database and returns it as a Transaction object.
     @param id the transaction ID to search for in the table.
     @return the Transaction object with the specified ID, or null if the transaction was not found.
     @throws SQLException if there is a database access error or if the SQL syntax is invalid.
     @throws ClassNotFoundException if the JDBC driver class cannot be found.
     */
    public Transaction databaseToTransaction(int id) throws SQLException, ClassNotFoundException{
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ResultSet rs;
        try {
            String query = "SELECT * FROM transactions WHERE transactionId='"+id+"'";
            rs = stmt.executeQuery(query);
            while(rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                Gson gson = new Gson();
                Transaction transaction = gson.fromJson(json, Transaction.class);
                return transaction;
            }
        } catch (Exception e) {
            System.err.println("databaseToTransaction: Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

    /**
     Converts a Transaction object to a JSON string.
     @param transaction the Transaction object to convert to JSON.
     @return a JSON string representing the Transaction object.
     */
    public String transactionToJSON(Transaction transaction){
        Gson gson = new Gson();
        String json = gson.toJson(transaction, Transaction.class);
        return json;
    }

    /**
     Adds a new transaction to the "transactions" table in the connected database using a JSON string.
     @param json the JSON string representing the Transaction object to be added to the table.
     @throws ClassNotFoundException if the JDBC driver class cannot be found.
     */
    public void addTransactionJSON(String json) throws ClassNotFoundException{
        Transaction transaction =jsonToTransaction(json);
        addTransaction(transaction);
    }

    /**
     Converts a JSON string to a Transaction object.
     @param json the JSON string to convert to a Transaction object.
     @return the Transaction object represented by the JSON string.
     */
    public Transaction jsonToTransaction(String json){
        Gson gson = new Gson();
        Transaction transaction = gson.fromJson(json, Transaction.class);
        return transaction;
    }

}