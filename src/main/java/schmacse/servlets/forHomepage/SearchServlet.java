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
import java.util.*;

@WebServlet(name = "search-servlet", value = "/search-servlet")
public class SearchServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Connection dbconnection = (Connection) getServletContext().getAttribute("DBConnection");

        String textfield = req.getParameter("textfield");

        String category = req.getParameter("categories");

        List<Category> categories = new ArrayList<>(Arrays.asList(Category.values()));
        req.setAttribute("categoryList", categories);

        ItemDao itemDao = new ItemDao(dbconnection);
        try {
            List<Item> items;
            if(category.equals("ALL")){
                items = itemDao.getFilteredItems(textfield, null);
            }else{
                items = itemDao.getFilteredItems(textfield, Category.valueOf(category));
            }


            items.sort(Item::comparePrice);
//            System.out.println(req.getParameterValues("invert")[0]);
            if (req.getParameterValues("invert") != null){
                Collections.reverse(items);
            }

            req.setAttribute("itemsList", items);
            req.getSession().setAttribute("itemsList", items);
            req.getRequestDispatcher("/homepage").forward(req, resp);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}


