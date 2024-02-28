package servlets;

import com.google.gson.Gson;
import database.DB_Connection;
import database.tables.CostumerTable;
import database.tables.ReservationTable;
import database.tables.TransactionTable;
import models.Reservation;
import models.Transaction;
import org.json.JSONArray;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Nick Giovanopoulos
 */
@WebServlet(name = "Book", value = "/Book")
public class Book extends HttpServlet {

    /**
     Handles POST requests to book a room.
     @param request The HttpServletRequest object containing the request parameters.
     @param response The HttpServletResponse object used to send the response to the client.
     @throws ServletException If there is a servlet error.
     @throws IOException If there is an I/O error.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        int costumerId = Integer.parseInt( request.getParameter("costumerId") );
        String date = request.getParameter("date");
        int slot = Integer.parseInt( request.getParameter("slot") );
        int roomId = Integer.parseInt( request.getParameter("roomId") );
        int amount = Integer.parseInt( request.getParameter("amount") );
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = currentDate.format(formatter);

        ReservationTable rt = new ReservationTable();
        TransactionTable t = new TransactionTable();
        CostumerTable ct = new CostumerTable();
        int canBeBooked = ct.decreaseMoney(costumerId,amount);
        if (canBeBooked==-1){
            response.setStatus(400);
            return;
        }
        try{
            Transaction transaction = new Transaction(formattedDate,amount,"wallet");
            int transactionId = t.addTransaction(transaction);
            Reservation reservation = new Reservation(roomId,costumerId,transactionId,date,slot,"PENDING");
            rt.addReservation(reservation);
            response.setStatus(200);
        } catch (ClassNotFoundException ex) {
            response.setStatus(403);
        }
    }

    /**
     Gets the upcoming reservation notifications for a customer.
     @param costumerId The ID of the customer.
     @return A list of strings containing the upcoming reservation notifications for the customer.
     @throws SQLException If there is a SQL error.
     @throws ClassNotFoundException If the JDBC driver class is not found.
     @throws ParseException If there is a parsing error.
     */
    public List<String> getUpcomingReservationNotifications(int costumerId) throws SQLException, ClassNotFoundException, ParseException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        List<Reservation> reservations = new ArrayList<>();
        ResultSet rs;
        try {
            String query = "SELECT * FROM reservations WHERE costumerId='" + costumerId + "'";
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                Gson gson = new Gson();
                Reservation reservation = gson.fromJson(json, Reservation.class);
                reservations.add(reservation);
            }
        } catch (Exception e) {
            System.err.println("getUpcomingReservationNotifications: Got an exception! ");
            System.err.println(e.getMessage());
        }

        List<String> notifications = new ArrayList<>();
        LocalDate currentDate = LocalDate.now();
        for (Reservation reservation : reservations) {
            String stringDate = reservation.getDate();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            LocalDate reservationDate = LocalDate.parse(stringDate, formatter);
            if (reservationDate.equals(currentDate.plusDays(1))) {
                String roomName = getRoomName(reservation.getRoomId());
                String notification = "Hey there! Your booking in " + roomName + " is 1 day ahead. Get prepared for an amazing education experience!";
                notifications.add(notification);
            }
        }
        return notifications;
    }

    /**
     Gets the name of a room.
     @param roomId The ID of the room.
     @return The name of the room.
     @throws SQLException If there is a SQL error.
     @throws ClassNotFoundException If the JDBC driver class is not found.
     */
    private String getRoomName(int roomId) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ResultSet rs;
        String roomName = "";
        try {
            String query = "SELECT name FROM rooms WHERE roomId='" + roomId + "'";
            rs = stmt.executeQuery(query);
            if (rs.next()) {
                roomName = rs.getString("name");
            }
        } catch (Exception e) {
            System.err.println("getRoomName: Got an exception! ");
            System.err.println(e.getMessage());
        }
        return roomName;
    }

    /**
     Handles GET requests to get upcoming reservation notifications for a customer.
     @param request The HttpServletRequest object containing the request parameters.
     @param response The HttpServletResponse object used to send the response to the client.
     @throws ServletException If there is a servlet error.
     @throws IOException If there is an I/O error.
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        int costomerId = Integer.parseInt(request.getParameter("costumerId"));
        try {
            List<String> notifications = getUpcomingReservationNotifications(costomerId);
            JSONArray json = new JSONArray(notifications);
            PrintWriter out = response.getWriter();
            out.print(json);
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println(e);
        } catch (ParseException e) {
            System.err.println(e);
        }
    }
}