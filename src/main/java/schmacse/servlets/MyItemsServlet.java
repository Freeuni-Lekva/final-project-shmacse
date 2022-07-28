package schmacse.servlets;

import schmacse.daos.ItemDao;
import schmacse.model.Item;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

@WebServlet("/my-items")
public class MyItemsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();

        Connection connection = (Connection) getServletContext().getAttribute("DBConnection");
        ItemDao itemDao = new ItemDao(connection);

        String username = (String) session.getAttribute("username");

        List<Item> myItemsList;

        try {
            myItemsList = itemDao.getItemsByUsername(username);
        } catch (SQLException e) {
            myItemsList = Collections.emptyList();
        }

        req.setAttribute("myItemsList", myItemsList);

        req.getRequestDispatcher("/my-items.jsp").forward(req, resp);
    }

}
