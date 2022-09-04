package schmacse.servlets;

import schmacse.daos.ItemDao;
import schmacse.daos.UserDao;
import schmacse.daos.WishListDao;
import schmacse.model.Item;
import schmacse.model.User;

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

@WebServlet("/wishlist")
public class WishlistServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();

        Connection connection = (Connection) getServletContext().getAttribute("DBConnection");
        UserDao userDao = new UserDao(connection);
        ItemDao itemDao = new ItemDao(connection);

        String username = (String) session.getAttribute("username");

        List<Item> itemsInWishlist;

        try {
            User user = userDao.getUserByUsername(username);

            itemsInWishlist = itemDao.getItemsForUserInWishlist(user.getId());
        } catch (SQLException e) {
            itemsInWishlist = Collections.emptyList();
        }

        req.setAttribute("itemsInWishlist", itemsInWishlist);

        req.getRequestDispatcher("/wishlist.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();

        Connection connection = (Connection) getServletContext().getAttribute("DBConnection");
        UserDao userDao = new UserDao(connection);
        WishListDao wishListDao = new WishListDao(connection);
        ItemDao itemDao = new ItemDao(connection);

        int itemId = Integer.parseInt(req.getParameter("itemId"));

        String username = (String) session.getAttribute("username");

        List<Item> itemsInWishlist;

        try {
            User user = userDao.getUserByUsername(username);

            wishListDao.remove(user.getId(), itemId);

            itemsInWishlist = itemDao.getItemsForUserInWishlist(user.getId());
        } catch (SQLException e) {
            itemsInWishlist = Collections.emptyList();
        }

        req.setAttribute("itemsInWishlist", itemsInWishlist);

        req.getRequestDispatcher("/wishlist.jsp").forward(req, resp);
    }
}
