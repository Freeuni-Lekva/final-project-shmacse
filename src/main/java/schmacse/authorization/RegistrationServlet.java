package schmacse.authorization;

import schmacse.daos.UserDao;
import schmacse.databaseconnection.DBConnection;
import schmacse.model.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet("/register")
public class RegistrationServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String firstName = req.getParameter("firstname");
        String lastName = req.getParameter("lastname");
        String username = req.getParameter("username");
        String password = req.getParameter("pass");
        String repeatedPassword = req.getParameter("re_pass");
        String contact = req.getParameter("contact");

        if (firstName.isEmpty() || lastName.isEmpty() || username.isEmpty() || password.isEmpty()
                || repeatedPassword.isEmpty() || contact.isEmpty()){
            req.setAttribute("status", "failed, empty field(s)");
            req.getRequestDispatcher("registration.jsp").forward(req, resp);
            return;
        }

        if(!repeatedPassword.equals(password)){
            req.setAttribute("status", "failed, passwords do not match");
            req.getRequestDispatcher("registration.jsp").forward(req, resp);
            return;
        }

        try {
            Double.parseDouble(contact);
        } catch (NumberFormatException e){
            req.setAttribute("status", "failed, invalid number");
            req.getRequestDispatcher("registration.jsp").forward(req, resp);
            return;
        }
        try{
            Connection con = (Connection) req.getServletContext().getAttribute("DBConnection");
            UserDao userDao = new UserDao(con);
            if (userDao.getUserByUsername(username) != null){
                req.setAttribute("status", "failed, username is already taken");
                req.getRequestDispatcher("registration.jsp").forward(req, resp);
                return;
            }
        } catch (SQLException ignored) {
        }


        try {
            Connection con = (Connection) req.getServletContext().getAttribute("DBConnection");
            UserDao userDao = new UserDao(con);
//            User user = new User(firstName, lastName, username, contact, password);

            PreparedStatement stm = con.prepareStatement(
                    "insert into users(first_name,last_name,username,phone_number,password) " +
                            "values (?,?,?,?,?)"
            );
            stm.setString(1, firstName);
            stm.setString(2, lastName);
            stm.setString(3, username);
            stm.setString(4, contact);
            stm.setString(5, password);

            int rowCount = stm.executeUpdate();
            RequestDispatcher dispatcher = req.getRequestDispatcher("registration.jsp");

            if (rowCount > 0) {
                req.setAttribute("status", "success");
            }else {
                req.setAttribute("status", "failed");
            }

            dispatcher.forward(req,resp);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBConnection.closeConnection();
        }


    }
}
