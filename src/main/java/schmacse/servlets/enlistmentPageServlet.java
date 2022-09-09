package schmacse.servlets;

import schmacse.daos.ItemDao;
import schmacse.daos.UserDao;
import schmacse.model.Category;
import schmacse.model.Item;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


@WebServlet(name = "enlistment-page", value = "/enlistment-page")
@MultipartConfig
public class enlistmentPageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String userName = (String) session.getAttribute("username");
        if(userName == null){
            resp.sendRedirect("login.jsp");
            return;
        }

        List<Category> categories = new ArrayList<>(Arrays.asList(Category.values()));
        req.setAttribute("categoryList", categories);

        req.getRequestDispatcher("/enlistment-page.jsp").forward(req, resp);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Connection dbconnection = (Connection) getServletContext().getAttribute("DBConnection");
        HttpSession session = req.getSession();
        ItemDao itemDao = new ItemDao(dbconnection);
        UserDao userDao = new UserDao(dbconnection);
        String userName = (String) session.getAttribute("username");
        if(userName == null){
            resp.sendRedirect("login.jsp");
            return;
        }

        int userId;
        try {
            userId = userDao.getUserByUsername(userName).getId();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        byte[] imageByteArray = null;
        if(validInput(req)){

            InputStream stream;
            try {
                stream = req.getPart("image").getInputStream();

                byte[] buffer = new byte[8192];
                int bytesRead;
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                while ((bytesRead = stream.read(buffer)) != -1)
                {
                    output.write(buffer, 0, bytesRead);
                }
                imageByteArray = output.toByteArray();

            } catch (IOException | ServletException e) {
                throw new RuntimeException(e);
            }

            try {
                itemDao.add(new Item(0, userId, req.getParameter("item-name"), Integer.parseInt(req.getParameter("item-price")),
                        req.getParameter("description"), Category.valueOf(req.getParameter("categories"))), imageByteArray);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            resp.sendRedirect("my-items");
            return;
        }

        List<Category> categories = new ArrayList<>(Arrays.asList(Category.values()));
        req.setAttribute("categoryList", categories);
        req.setAttribute("item-name", req.getParameter("item-name"));
        req.setAttribute("item-price", req.getParameter("item-price"));
        req.setAttribute("categories", req.getParameter("categories"));
        req.setAttribute("description", req.getParameter("description"));

        req.getRequestDispatcher("/enlistment-page.jsp").forward(req, resp);
    }

    private boolean validInput(HttpServletRequest req) {
        boolean ret = true;

        String itemName = req.getParameter("item-name");
        if(Objects.equals(itemName, "")){
            req.setAttribute("item-name-prompt", " *can't be empty");
            ret = false;
        }else if(itemName.length() > 64){
            req.setAttribute("item-name-prompt", " *Name can't be longer than 64 characters");
            ret = false;
        }else{
            req.removeAttribute("item-name-prompt");
        }

        int itemPrice = Integer.parseInt(req.getParameter("item-price"));
        if(itemPrice == 0){
            req.setAttribute("item-price-prompt", " *can't be 0.");
            ret = false;
        }else if(itemPrice < 0){
            req.setAttribute("item-price-prompt", " *Item price can't be negative.");
            ret = false;
        }else{
            req.removeAttribute("item-price-prompt");
        }

        String description = req.getParameter("description");
        if(Objects.equals(description, "")){
            req.setAttribute("description-prompt", " *can't be empty");
            ret = false;
        }else{
            req.removeAttribute("description-prompt");
        }


        return ret;
    }
}
