package schmacse.servlets.forHomepage;

import schmacse.daos.ItemDao;
import schmacse.model.Category;
import schmacse.model.Item;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "search-servlet", value = "/search-servlet")
public class SearchServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Connection dbconnection = (Connection) getServletContext().getAttribute("DBConnection");

        String textfield = req.getParameter("textfield");
        String category = req.getParameter("categories");

        ItemDao itemDao = new ItemDao(dbconnection);
        try {
            List<Item> allItems = itemDao.getFilteredItems(textfield, Category.valueOf(category));
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
