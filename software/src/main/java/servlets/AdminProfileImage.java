package servlets;

import database.tables.AdminTable;

import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.SQLException;

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
@WebServlet(name = "AdminProfileImage", value = "/AdminProfileImage")
public class AdminProfileImage extends HttpServlet {

    /**
     Handles HTTP GET requests for retrieving the profile image of an admin user.
     Retrieves the admin ID from the request parameters and uses it to retrieve the profile image from the database.
     Writes the image to the response output stream with content type "image/png".
     Sends an error response if the image cannot be retrieved or the server encounters an error.
     @param request the HTTP servlet request containing the admin ID parameter
     @param response the HTTP servlet response containing the profile image data
     @throws ServletException if the servlet encounters an error that prevents it from handling the request
     @throws IOException if an input or output error occurs while the servlet is handling the request
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int costumerId = Integer.parseInt(request.getParameter("admin_id"));
        try {
            AdminTable adminTable = new AdminTable();
            BufferedImage image = adminTable.getAdminProfileImg(costumerId);
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

}
