package servlets;

import database.tables.AdminTable;
import database.tables.CostumerTable;
import models.Admin;
import models.Costumer;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
/**
 *
 * @author Nick Giovanopoulos
 */
@WebServlet(name = "Login", value = "/Login")
public class Login extends HttpServlet {

    /**
     Handles GET requests to check if a user is logged in and returns the user's data if logged in.
     @param request the HTTP request
     @param response the HTTP response
     @throws ServletException if an error occurs while handling the request
     @throws IOException if an error occurs while handling the request
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session=request.getSession();
        Costumer costumer = null;
        Admin admin = null;
        if(session.getAttribute("loggedIn")!=null){
            String id = session.getAttribute("loggedIn").toString();
            String[] splitString = id.split("___");
            CostumerTable ct = new CostumerTable();
            AdminTable at = new AdminTable();
            try {
                costumer = ct.databaseToCostumer(splitString[0], splitString[1]);
                admin = at.databaseToAdmin(splitString[0], splitString[1]);

            }catch (Exception e){
                System.err.println(e.getMessage());
            }
            String json;
            if (costumer!=null) json = ct.CostumerToJSON(costumer);
            else if (admin!=null) json = at.adminToJSON(admin);
            else{
                response.setStatus(403);
                return;
            }
            response.setStatus(200);
            response.getWriter().write(json);
        }
        else response.setStatus(403);
    }

    /**
     Handles POST requests to authenticate user login and creates a session for the user if login is successful.
     @param request the HTTP request
     @param response the HTTP response
     @throws ServletException if an error occurs while handling the request
     @throws IOException if an error occurs while handling the request
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String username=request.getParameter("username");
        String password=request.getParameter("password");
        HttpSession session = request.getSession(true);
        try (PrintWriter out = response.getWriter()) {
            CostumerTable ct = new CostumerTable();
            AdminTable at = new AdminTable();
            Costumer costumer = ct.databaseToCostumer(username, password);
            Admin admin = at.databaseToAdmin(username, password);
            if(costumer==null && admin==null) response.setStatus(403);
            else{
                String id = username +"___"+password;
                session.setAttribute("loggedIn",id);
                String json;
                if (costumer!=null) json = ct.CostumerToJSON(costumer);
                else json = at.adminToJSON(admin);
                System.out.println(json);
                out.println(json);
                response.setStatus(200);
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        } catch (ClassNotFoundException ex) {
            System.err.println(ex.getMessage());
        }
    }
}