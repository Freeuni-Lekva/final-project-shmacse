package schmacse.authorization;

import com.mysql.cj.x.protobuf.MysqlxPrepare;
import schmacse.daos.UserDao;
import schmacse.databaseconnection.DBConnection;
import schmacse.model.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String userName = req.getParameter("username");
        String pass = req.getParameter("pass");

        HttpSession session = req.getSession();
        Connection connection = (Connection) getServletContext().getAttribute("DBConnection");

        try {
            UserDao userDao = new UserDao(connection);
            if (userDao.getUserByUsername(userName) == null) {
                req.setAttribute("status", "failed, username not found");
                req.getRequestDispatcher("login.jsp").forward(req, resp);
            } else if (userDao.getUserByUsernameAndHashedPassword(userName, pass) == null) {
                req.setAttribute("status", "failed, password incorrect");
                req.getRequestDispatcher("login.jsp").forward(req, resp);
            } else {
                session.setAttribute("username", userName);
                session.setAttribute("isAdmin", userDao.getIsAdminByUsername(userName));
                req.getRequestDispatcher("homepage").forward(req, resp);
            }
        }catch (SQLException ignored){}
        catch (NoSuchAlgorithmException e) {e.printStackTrace();}

    }
}
