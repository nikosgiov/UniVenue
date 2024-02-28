package servlets;

import database.tables.CostumerTable;
import database.tables.RoomTable;

import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 *
 * @author Nick Giovanopoulos
 */
@WebServlet(name = "ProfileImage", value = "/ProfileImage")
public class ProfileImage extends HttpServlet {

    /**
     Handles GET requests for retrieving the user's profile image based on their costumer ID.
     @param request the HttpServletRequest object containing the request parameters
     @param response the HttpServletResponse object for sending the response
     @throws ServletException if the servlet encounters an error during request processing
     @throws IOException if an I/O error occurs while handling the GET request
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int costumerId = Integer.parseInt(request.getParameter("costumerId"));
        try {
            CostumerTable costumerTable = new CostumerTable();
            BufferedImage image = costumerTable.getProfileImg(costumerId);
            System.out.println(image);
            // Set the content type of the response to "image/png"
            response.setContentType("image/png");
            // Write the image to the response output stream
            ImageIO.write(image, "png", response.getOutputStream());
            response.setStatus(200);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setStatus(403);
        }
    }

    /**
     Handles POST requests for adding/updating the user's profile image based on their costumer ID.
     @param request the HttpServletRequest object containing the request parameters
     @param response the HttpServletResponse object for sending the response
     @throws ServletException if the servlet encounters an error during request processing
     @throws IOException if an I/O error occurs while handling the POST request
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get the user's first name, last name, and costumer ID from the request parameters
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        int costumerId = Integer.parseInt(request.getParameter("costumerId"));
        CostumerTable costumerTable = new CostumerTable();
        try {
            costumerTable.addProfileImg(costumerId,firstName,lastName);
        } catch (SQLException e) {
            Logger.getLogger(RoomTable.class.getName()).log(Level.SEVERE, null, e);
        } catch (ClassNotFoundException e) {
            Logger.getLogger(RoomTable.class.getName()).log(Level.SEVERE, null, e);
        }
    }


}
