package schmacse.authorization;

import com.mysql.cj.x.protobuf.MysqlxPrepare;
import schmacse.databaseconnection.DBConnection;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String uname = req.getParameter("username");
        String pass = req.getParameter("password");

        Connection con = null;
        HttpSession session = req.getSession();
        RequestDispatcher dispatcher = null;

        try {
            con = DBConnection.getConnection();
            PreparedStatement stm = con.prepareStatement(
                    "select * from users where username = ? and password = ?"
            );
            stm.setString(1,uname);
            stm.setString(2,pass);

            ResultSet rs = stm.executeQuery();

            if(rs.next()){
                session.setAttribute("name", rs.getString("username"));
                dispatcher = req.getRequestDispatcher("index.jsp");
            }else{
                req.setAttribute("status", "failed");
                dispatcher = req.getRequestDispatcher("login.jsp");
            }
            dispatcher.forward(req,resp);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
