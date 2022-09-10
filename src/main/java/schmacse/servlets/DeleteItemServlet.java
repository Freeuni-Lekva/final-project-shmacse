package schmacse.servlets;

import schmacse.daos.ItemDao;
import schmacse.daos.WishListDao;
import schmacse.model.Category;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@WebServlet("/delete-item")
public class DeleteItemServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();

        Connection connection = (Connection) getServletContext().getAttribute("DBConnection");
        ItemDao itemDao = new ItemDao(connection);
        WishListDao wishListDao = new WishListDao(connection);

        int itemId = Integer.parseInt(req.getParameter("itemId"));

        String username = (String) session.getAttribute("username");

        List<Item> myItemsList = Collections.emptyList();

        try {
            wishListDao.removeRowsOfItem(itemId);
            itemDao.remove(itemId);
            myItemsList = itemDao.getItemsByUsername(username);
        } catch (SQLException e) {
            req.setAttribute("myItemsList", myItemsList);
            req.getRequestDispatcher("/my-items.jsp").forward(req, resp);
            return;
        }

        req.setAttribute("myItemsList", myItemsList);
        List<Category> categories = new ArrayList<>(Arrays.asList(Category.values()));
        req.setAttribute("categoryList", categories);

        if(req.getSession().getAttribute("byAdmin")!= null && (Boolean) req.getSession().getAttribute("byAdmin")) {
            req.getRequestDispatcher("/homepage").forward(req, resp);
        }
        else
            req.getRequestDispatcher("/my-items.jsp").forward(req, resp);

    }
}
