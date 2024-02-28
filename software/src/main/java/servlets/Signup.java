package servlets;

import database.tables.AdminTable;
import database.tables.CostumerTable;
import models.Admin;
import models.Costumer;
import org.json.JSONObject;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.stream.Collectors;


@WebServlet(name = "Signup", value = "/Signup")
public class Signup extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        CostumerTable ct = new CostumerTable();
        String jsonData = request.getReader().lines().collect(Collectors.joining());

        try (PrintWriter out = response.getWriter()) {
            if(ct.checkError(ct.jsonToCostumer(jsonData).getUsername(),1)!=null){
                response.getWriter().write('1');
                response.setStatus(403);
            } else if ((ct.checkError(ct.jsonToCostumer(jsonData).getEmail(),2)!=null)) {
                response.getWriter().write('2');
                response.setStatus(403);
            } else if (ct.checkError(ct.jsonToCostumer(jsonData).getTelephone(),3)!=null){
                response.getWriter().write('3');
                response.setStatus(403);
            }else{
                ct.addCostumerJSON(jsonData);
                response.setStatus(200);
            }

        } catch (ClassNotFoundException | SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

}