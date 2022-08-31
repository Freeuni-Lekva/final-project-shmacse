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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@WebServlet(name = "homepage", value = "/homepage")
public class HomepageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        List<Category> categories = new ArrayList<>(Arrays.asList(Category.values()));
        req.setAttribute("categoryList", categories);

        ItemDao itemDao = new ItemDao((Connection) req.getServletContext().getAttribute("DBConnection"));
        try {
            List<Item> itemsList = itemDao.getFilteredItems("", null);

            if(req.getAttribute("pageNumber") != null){ // access pageNumber
                int pageNumber = (int) req.getAttribute("pageNumber");
                if(req.getParameter("newPage") != null){ // handles Next/Previous Page clicks (from homepage.jsp)
                    String newPage = req.getParameter("newPage");
                    if(newPage.equals("next")){
                        pageNumber++;
                    }
                    if(newPage.equals("previous")){
                        pageNumber--;
                    }
                }
                req.setAttribute("pageNumber",(int) req.getAttribute("pageNumber"));
                if( (itemsList.size()/32) + 1 == pageNumber){
                    req.setAttribute("isLastPage", true);
                }
                itemsList = itemsList.subList((pageNumber-1)*32, pageNumber*32);
            }else{ // if pageNumber attr does not exist, it must be 1.
                req.setAttribute("pageNumber", 1);
                itemsList = itemsList.subList(0,32);
            }
            req.setAttribute("itemsList", itemsList);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        req.getRequestDispatcher("/homepage.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
