package servlets;

import database.tables.ReservationTable;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Nick Giovanopoulos
 */
@WebServlet(name = "Pending", value = "/Pending")
public class Pending extends HttpServlet {

    /**
     Handles GET requests to retrieve all pending reservations from the database
     and send them as a JSON string in the response body
     @param request HttpServletRequest object representing the request
     @param response HttpServletResponse object representing the response
     @throws ServletException if a servlet-specific error occurs
     @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        ReservationTable rt = new ReservationTable();
        try (PrintWriter out = response.getWriter()) {
            String jsonReservations = rt.pendingReservations();
            out.println(jsonReservations);
            System.out.println(jsonReservations);
            response.setStatus(200);
        } catch (SQLException ex) {
            response.setStatus(403);
            Logger.getLogger(ReservationTable.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            response.setStatus(403);
            Logger.getLogger(ReservationTable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     Handles POST requests to update a pending reservation to be completed
     @param request HttpServletRequest object representing the request
     @param response HttpServletResponse object representing the response
     @throws ServletException if a servlet-specific error occurs
     @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        int reservationId = Integer.parseInt( request.getParameter("reservationId") );
        ReservationTable rt = new ReservationTable();
        try {
            int res = rt.updateReservationToCompleted(reservationId);
            if (res==0) response.setStatus(200);
            else response.setStatus(400);
        } catch (SQLException e) {
            response.setStatus(403);
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            response.setStatus(403);
            throw new RuntimeException(e);
        }
    }
}