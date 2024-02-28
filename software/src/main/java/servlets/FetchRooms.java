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
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Nick Giovanopoulos
 */
@WebServlet(name = "FetchRooms", value = "/FetchRooms")
public class FetchRooms extends HttpServlet {

    /**
     Handles the HTTP GET request for fetching all the rooms from the database.
     @param request the HttpServletRequest object containing the request parameters.
     @param response the HttpServletResponse object used to send the response to the client.
     @throws ServletException if the servlet encounters an exception while processing the request.
     @throws IOException if an I/O error occurs while processing the request or sending the response.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        RoomTable rt = new RoomTable();
        try (PrintWriter out = response.getWriter()) {
            ArrayList<Room> rooms = rt.databaseToRooms();
            if(rooms==null || rooms.size()==0){
                response.setStatus(403);
            }
            else{
                String json = new Gson().toJson(rooms);
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