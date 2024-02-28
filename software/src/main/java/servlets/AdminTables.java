package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import database.tables.CostumerTable;
import database.tables.ReservationTable;
import database.tables.RoomTable;
import database.tables.TransactionTable;
import org.json.JSONArray;
/**
 *
 * @author Nick Giovanopoulos
 */
@WebServlet(name = "AdminTables", value = "/AdminTables")
public class AdminTables extends HttpServlet {

    /**
     Handles GET requests to retrieve all tables from the database related to admin operations.
     @param request the HttpServletRequest object that contains the request the client has made of the servlet
     @param response the HttpServletResponse object that contains the response the servlet sends to the client
     @throws ServletException if the request for the GET could not be handled
     @throws IOException if an input or output error is detected when the servlet handles the GET request
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CostumerTable costumerTable = new CostumerTable();
        TransactionTable transactionTable = new TransactionTable();
        ReservationTable reservationTable = new ReservationTable();
        RoomTable roomTable = new RoomTable();
        Gson gson = new Gson();
        JSONArray tables = new JSONArray();
        try {
            String costumers = costumerTable.databaseToCostumers();
            String transactions = transactionTable.databaseToTransactions();
            String reservations = reservationTable.databaseToReservations();
            String rooms = gson.toJson( roomTable.databaseToRooms() );
            tables.put(costumers);
            tables.put(transactions);
            tables.put(reservations);
            tables.put(rooms);
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print(tables);
            response.setStatus(200);
        } catch (SQLException e) {
            response.setStatus(403);
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            response.setStatus(403);
            throw new RuntimeException(e);
        }
    }

}