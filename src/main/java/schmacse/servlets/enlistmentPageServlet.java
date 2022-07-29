package schmacse.servlets;

import schmacse.daos.ItemDao;
import schmacse.daos.UserDao;
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
import java.util.List;
import java.util.Objects;


@WebServlet(name = "enlistment-page", value = "/enlistment-page")
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
        if(validInput(req)){
            System.out.println(req.getParameter("category"));
            try {
                itemDao.add(new Item(0, userId, req.getParameter("item-name"), req.getParameter("description"), Category.valueOf(req.getParameter("categories"))));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            resp.sendRedirect("my-items");
            return;
        }

        List<Category> categories = new ArrayList<>(Arrays.asList(Category.values()));
        req.setAttribute("categoryList", categories);

        req.getRequestDispatcher("/enlistment-page.jsp").forward(req, resp);
    }

    private boolean validInput(HttpServletRequest req) {
        boolean ret = true;

        String itemName = req.getParameter("item-name");
        if(Objects.equals(itemName, "")){
            req.setAttribute("item-name-prompt", "*Name can't be empty");
            ret = false;
        }else if(itemName.length() > 64){
            req.setAttribute("item-name-prompt", "*Name can't be longer than 64 characters");
            ret = false;
        }else{
            req.removeAttribute("item-name-prompt");
        }

        String description = req.getParameter("description");
        if(Objects.equals(description, "")){
            req.setAttribute("description-prompt", "*Description can't be empty");
            ret = false;
        }else{
            req.removeAttribute("description-prompt");
        }

        return ret;
    }
}
