package schmacse.servlets;

import schmacse.daos.ItemDao;
import schmacse.daos.UserDao;
import schmacse.model.Category;
import schmacse.model.Item;
import schmacse.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@WebServlet(name = "owner-item-page", value = "/owner-item-page")
public class OwnerItemPage extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Connection connection = (Connection) getServletContext().getAttribute("DBConnection");

        String username = (String) req.getSession().getAttribute("username");
        int itemId = Integer.parseInt(req.getParameter("itemId"));

        UserDao userDao = new UserDao(connection);
        ItemDao itemDao = new ItemDao(connection);
        try {

            User user = userDao.getUserByUsername(username);
            Item item = itemDao.getItemByItemID(itemId);
            List<Category> categories = new ArrayList<>(Arrays.asList(Category.values()));

            req.setAttribute("categoryList", categories);
            req.setAttribute("item", item);
            req.setAttribute("user", user);

            req.getRequestDispatcher("/owner-item-page.jsp").forward(req, resp);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // updates price
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Connection connection = (Connection) req.getServletContext().getAttribute("DBConnection");

        String username = (String) req.getSession().getAttribute("username");

        int itemId = Integer.parseInt(req.getParameter("itemId"));

        int newPrice = Integer.parseInt(req.getParameter("updated-price"));
        String newItemName = req.getParameter("updated-item-name");
        String newPhoneNumber = req.getParameter("updated-owner-phone");
        String newDescription = req.getParameter("updated-description");
        Category newCategory = Category.valueOf(req.getParameter("updated-category"));

        ItemDao itemDao = new ItemDao(connection);
        UserDao userDao = new UserDao(connection);

        try {

            User user = userDao.getUserByUsername(username);
            int userId = user.getId();

            itemDao.updatePrice(itemId, newPrice);
            itemDao.updateName(itemId, newItemName);
            itemDao.updateDescription(itemId, newDescription);
            userDao.updatePhoneNumber(userId, newPhoneNumber);
            itemDao.updateCategory(itemId, newCategory);


            Item item = itemDao.getItemByItemID(itemId);
            List<Category> categories = new ArrayList<>(Arrays.asList(Category.values()));

            req.setAttribute("categoryList", categories);
            req.setAttribute("item", item);
            req.setAttribute("user", user);

            req.getRequestDispatcher("/owner-item-page.jsp").forward(req, resp);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
