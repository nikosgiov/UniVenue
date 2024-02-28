package servlets;

import database.tables.ReviewTable;
import database.tables.RoomTable;

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
@WebServlet(name = "Reviews", value = "/Reviews")
public class Reviews extends HttpServlet {

    /**
     Handles HTTP GET requests to get the reviews of a specific room
     @param request HTTP request
     @param response HTTP response
     @throws ServletException if a servlet-specific error occurs
     @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        int roomId = Integer.parseInt( request.getParameter("roomId") );
        ReviewTable rt = new ReviewTable();
        try (PrintWriter out = response.getWriter()) {
            String reviews = rt.databaseToReviews(roomId);
            out.println(reviews);
            System.out.println(reviews);
        } catch (SQLException ex) {
            Logger.getLogger(RoomTable.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(RoomTable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     Handles HTTP POST requests to add a new review for a specific room
     @param request HTTP request
     @param response HTTP response
     @throws ServletException if a servlet-specific error occurs
     @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        int costumerId = Integer.parseInt( request.getParameter("costumerId") );
        int roomId = Integer.parseInt( request.getParameter("roomId") );
        String comment = request.getParameter("comment");
        int rating = Integer.parseInt( request.getParameter("rating") );
        ReviewTable rt = new ReviewTable();
        try {
            int res = rt.addReview(costumerId, roomId,comment,rating);
            System.err.println(res);
            if (res==0) response.setStatus(200);
            else response.setStatus(400);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(RoomTable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}