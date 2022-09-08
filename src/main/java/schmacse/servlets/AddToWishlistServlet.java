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
@WebServlet(name = "add-to-wishlist", value = "/add-to-wishlist")
public class AddToWishlistServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Connection connection = (Connection) req.getServletContext().getAttribute("DBConnection");

        String username = (String) req.getSession().getAttribute("username");
        int itemId = Integer.parseInt(req.getParameter("itemId"));
        int ownerId = Integer.parseInt(req.getParameter("ownerId"));

        UserDao userDao = new UserDao(connection);

        User user = null; // user who adds to his wishlist
        User owner = null; // user who is the owner of item
        try {
            owner = userDao.getUserById(ownerId);

            user = userDao.getUserByUsername(username);
            int userID = user.getId();

            WishListDao wishListDao = new WishListDao(connection);

            boolean added = false;
            boolean alreadyInWishlist = wishListDao.hasItemInWishlist(userID, itemId);
            if(alreadyInWishlist) {
                req.setAttribute("error-message", "item already in wishlist");
                req.setAttribute("back-to", "item-page"); // where to go from error page
            }else{
                added = wishListDao.add(userID, itemId);
                if (!added) {
                    // prepare for error page
                    req.setAttribute("error-message", "unable to add to wishlist");
                    req.setAttribute("back-to", "item-page"); // where to go from error page
                }
            }


            ItemDao itemDao = new ItemDao(connection);
            Item item = null;
            try {
                item = itemDao.getItemByItemID(itemId);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            req.setAttribute("itemId", itemId);
            req.setAttribute("item", item);
            req.setAttribute("user", owner);

            if(added){
                req.getRequestDispatcher("item-page.jsp").forward(req,resp);
            }else{
                req.getRequestDispatcher("/error-page.jsp").forward(req,resp);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
