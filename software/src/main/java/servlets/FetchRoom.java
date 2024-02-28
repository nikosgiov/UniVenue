package servlets;

import com.google.gson.Gson;
import database.tables.RoomTable;
import models.Room;

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
@WebServlet(name = "FetchRoom", value = "/FetchRoom")
public class FetchRoom extends HttpServlet {

    /**
     Handles GET requests to fetch a room from the database by ID.
     @param request the HTTP request object containing the room ID parameter
     @param response the HTTP response object that will contain the fetched room in JSON format
     @throws ServletException if an error occurs while handling the servlet request
     @throws IOException if an error occurs while reading or writing data to/from the servlet request/response
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        int roomId = Integer.parseInt( request.getParameter("roomId") );
        RoomTable rt = new RoomTable();
        try (PrintWriter out = response.getWriter()) {
            Room room = rt.databaseToRoom(roomId);
            if(room==null){
                response.setStatus(403);
            }
            else{
                String json = new Gson().toJson(room);
                out.println(json);
                response.setStatus(200);
            }
        } catch (SQLException ex) {
            Logger.getLogger(RoomTable.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(RoomTable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}