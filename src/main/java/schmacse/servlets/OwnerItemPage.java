package schmacse.servlets;

import schmacse.daos.ImageDao;
import schmacse.daos.ItemDao;
import schmacse.daos.UserDao;
import schmacse.model.Category;
import schmacse.model.Item;
import schmacse.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@WebServlet(name = "owner-item-page", value = "/owner-item-page")
@MultipartConfig
public class OwnerItemPage extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Connection connection = (Connection) getServletContext().getAttribute("DBConnection");

        String username = (String) req.getSession().getAttribute("username");
        int itemId = Integer.parseInt(req.getParameter("itemId"));

        UserDao userDao = new UserDao(connection);
        ItemDao itemDao = new ItemDao(connection);
        try {
            if(itemDao.getUserIDByItemID(itemId) != userDao.getUserByUsername(username).getId()){
                // not the owner is trying to access the webpage
                req.setAttribute("error-message", "Current user is not the owner of this item.");
                req.setAttribute("back-to", "homepage");
                req.getRequestDispatcher("/error-page.jsp").forward(req,resp);
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

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

    private  boolean onlyDigits(String str, int n) {
        // Traverse the string from
        // start to end
        for (int i = 0; i < n; i++) {

            // Check if character is
            // not a digit between 0-9
            // then return false
            if (str.charAt(i) < '0'
                    || str.charAt(i) > '9') {
                return false;
            }
        }
        if(n == 0) return false;
        // If we reach here, that means
        // all characters were digits.
        return true;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Connection connection = (Connection) req.getServletContext().getAttribute("DBConnection");

        String username = (String) req.getSession().getAttribute("username");

        int itemId = Integer.parseInt(req.getParameter("itemId"));

        String newPriceString = req.getParameter("updated-price");
        int newPrice = 0;
        if(onlyDigits(newPriceString, newPriceString.length())){
            newPrice = Integer.parseInt(req.getParameter("updated-price"));
        }
        String newItemName = req.getParameter("updated-item-name");
        String newPhoneNumber = req.getParameter("updated-owner-phone");
        String newDescription = req.getParameter("updated-description");
        Category newCategory = Category.valueOf(req.getParameter("updated-category"));
        boolean hasChanged = !("".equals(req.getParameter("updated-image-check")));

        byte[] imageByteArray = null;
        try {
            InputStream stream = req.getPart("new-image").getInputStream();
            byte[] buffer = new byte[8192];
            int bytesRead;
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            while ((bytesRead = stream.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
            imageByteArray = output.toByteArray();
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }

        ItemDao itemDao = new ItemDao(connection);
        UserDao userDao = new UserDao(connection);
        ImageDao imageDao = new ImageDao(connection);

        try {

            User user = userDao.getUserByUsername(username);
            int userId = user.getId();

            if(newPrice > 0){
                itemDao.updatePrice(itemId, newPrice);
            }
            if(newItemName.length() > 0){
                itemDao.updateName(itemId, newItemName);
            }
            itemDao.updateDescription(itemId, newDescription);
            userDao.updatePhoneNumber(userId, newPhoneNumber);
            itemDao.updateCategory(itemId, newCategory);

            if(hasChanged) {
                imageDao.removeWithItemId(itemId);
                int imageId = imageDao.addImage(itemId, imageByteArray);
                itemDao.changeImageID(itemId, imageId);
            }

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
