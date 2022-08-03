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
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

// adds item to wishlist of current user
@WebServlet(name = "add_to_wishlist", value = "/add_to_wishlist")
public class AddToWishlistServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Connection connection = (Connection) req.getServletContext().getAttribute("DBConnection");

        String username = (String) req.getSession().getAttribute("username");

        UserDao userDao = new UserDao(connection);
        int itemID = (int) req.getAttribute("itemID");

        User user = null;
        try {
            user = userDao.getUserByUsername(username);
            int userID = user.getId();

            WishListDao wishListDao = new WishListDao(connection);
            boolean added = wishListDao.add(userID, itemID);
            if(!added){
                // prepare for error page
                req.setAttribute("error-message", "unable to add to wishlist");
                req.setAttribute("back-to", "item-page"); // where to go from error page
            }

            ItemDao itemDao = new ItemDao(connection);
            Item item = null;
            try {
                item = itemDao.getItemByItemID(itemID);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            req.setAttribute("item", item);
            req.setAttribute("user", user);

            if(added){
                req.getRequestDispatcher("/itemPage.jsp").forward(req,resp);
            }else{
                req.getRequestDispatcher("/error-page.jsp").forward(req,resp);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
