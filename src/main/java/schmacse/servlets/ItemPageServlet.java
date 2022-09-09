package schmacse.servlets;

import schmacse.daos.ItemDao;
import schmacse.daos.UserDao;
import schmacse.daos.UserReviewDao;
import schmacse.model.Item;
import schmacse.model.Review;
import schmacse.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "item-page", value = "/item-page")
public class ItemPageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{

        Connection connection = (Connection) getServletContext().getAttribute("DBConnection");
        ItemDao itemDao = new ItemDao(connection);
        UserDao userDao = new UserDao(connection);
        UserReviewDao reviewDao = new UserReviewDao(connection);

        int itemId = Integer.parseInt(req.getParameter("itemId"));

        try {

            Item item = itemDao.getItemByItemID(itemId);
            int userID = itemDao.getUserIDByItemID(itemId);
            User user = userDao.getUserById(userID);
            List<Review> itemSellerReviews = reviewDao.getReviewsByUserId(userID);
            //TODO: get review from input, call reviewDao.add(userID, input)

            req.setAttribute("item", item);
            req.setAttribute("user", user);
            req.setAttribute("reviews", itemSellerReviews);

            req.getRequestDispatcher("/item-page.jsp").forward(req, resp);

        } catch (SQLException e) {
            req.setAttribute("error-message", "Error while retrieving item");
            req.setAttribute("back-to", "homepage"); // where to go from error page

            List<Item> itemsList = null;
            try {
                itemsList = itemDao.getFilteredItems("", null);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            itemsList.sort(Item::comparePrice);
            req.getSession().setAttribute("itemsList", itemsList);
            req.getRequestDispatcher("/error-page.jsp").forward(req,resp);
        }
    }

}
