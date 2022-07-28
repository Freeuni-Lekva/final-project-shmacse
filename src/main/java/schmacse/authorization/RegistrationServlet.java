package schmacse.authorization;

import schmacse.databaseconnection.DBConnection;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet("/register")
public class RegistrationServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fname = req.getParameter("firstname");
        String lname = req.getParameter("lastname");
        String uname = req.getParameter("username");
        String pass = req.getParameter("pass");
        String email = req.getParameter("email");
        String contact = req.getParameter("contact");

        RequestDispatcher dispatcher = null;
        Connection con = null;

        try {
            con = DBConnection.getConnection();
            PreparedStatement stm = con.prepareStatement(
                    "insert into users(first_name,last_name,username,phone_number,password) " +
                            "values (?,?,?,?,?)"
            );
            stm.setString(1,fname);
            stm.setString(2,lname);
            stm.setString(3,uname);
            stm.setString(4,contact);
            stm.setString(5,pass);

            int rowCount = stm.executeUpdate();
            dispatcher = req.getRequestDispatcher("registration.jsp");

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
