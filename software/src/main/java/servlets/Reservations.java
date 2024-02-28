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
@WebServlet(name = "Reservations", value = "/Reservations")
public class Reservations extends HttpServlet {

    /**
     Handles HTTP GET requests for a list of reservations for a specific costumer.
     @param request the HTTP request object
     @param response the HTTP response object
     @throws ServletException if the servlet encounters a servlet-specific problem
     @throws IOException if the servlet encounters an I/O problem
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        int costumerId = Integer.parseInt( request.getParameter("costumerId") );
        ReservationTable rt = new ReservationTable();
        try (PrintWriter out = response.getWriter()) {
            String json = rt.databaseToReservationsJSON(costumerId);
            if(json == null || json.isEmpty()){
                response.setStatus(403);
            }
            else{
                out.println(json);
                response.setStatus(200);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReservationTable.class.getName()).log(Level.SEVERE, null, ex);
            response.setStatus(403);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ReservationTable.class.getName()).log(Level.SEVERE, null, ex);
            response.setStatus(403);
        }
    }

    /**
     Handles HTTP POST requests to delete a reservation.
     @param request the HTTP request object
     @param response the HTTP response object
     @throws ServletException if the servlet encounters a servlet-specific problem
     @throws IOException if the servlet encounters an I/O problem
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        int reservationId = Integer.parseInt( request.getParameter("reservationId") );
        ReservationTable rt = new ReservationTable();
        try {
            rt.deleteReservation(reservationId);
            response.setStatus(200);
        } catch (ClassNotFoundException ex) {
            response.setStatus(403);
            Logger.getLogger(ReservationTable.class.getName()).log(Level.SEVERE, null, ex);
        }
        response.setStatus(200);

    }
}