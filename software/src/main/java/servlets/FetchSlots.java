package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import database.tables.ReservationTable;
import org.json.JSONArray;
/**
 *
 * @author Nick Giovanopoulos
 */
@WebServlet(name = "FetchSlots", value = "/FetchSlots")
public class FetchSlots extends HttpServlet {

    /**
     Retrieves the available time slots for a given room and date.
     @param request the HTTP request object containing the room ID and date parameters
     @param response the HTTP response object containing the available time slots in JSON format
     @throws ServletException if an error occurs while processing the request
     @throws IOException if an error occurs while writing the response
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve the roomId and date parameters from the request
        int roomId = Integer.parseInt(request.getParameter("roomId"));
        String date = request.getParameter("date");
        if (date.length()<1){
            response.setStatus(403);
            return;
        }
        ReservationTable reservationTable = new ReservationTable();
        ArrayList<Integer> slots = reservationTable.getSlots(roomId, date);
        // Create a JSON array to hold the slots
        JSONArray slotsArray = new JSONArray();
        for (int slot : slots) {
            slotsArray.put(slot);
        }
        // Set the response content type to JSON
        response.setContentType("application/json");
        // Write the slots JSON array to the response
        PrintWriter out = response.getWriter();
        out.print(slotsArray);
        response.setStatus(200);
    }

}