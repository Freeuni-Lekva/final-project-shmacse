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

    private static final int MAX_ITEMS_PER_PAGE = 32;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        List<Category> categories = new ArrayList<>(Arrays.asList(Category.values()));
        req.setAttribute("categoryList", categories);

        List<Item> allItems;
        if(req.getAttribute("itemsList") != null){
            System.out.println("SSS");
            allItems = (List<Item>) req.getAttribute("itemsList");
        }
        else {
            allItems = (List<Item>) req.getSession().getAttribute("itemsList");
        }

        int currentPageNumber = 0;
        boolean isLastPage = false;
        if(req.getParameter("pageNumber") == null){
            currentPageNumber = 1;
        }else{
            currentPageNumber = Integer.parseInt(req.getParameter("pageNumber"));
        }

        int endIndex = currentPageNumber * MAX_ITEMS_PER_PAGE;
        if(allItems.size() - (currentPageNumber * MAX_ITEMS_PER_PAGE) <= 0){
            endIndex = allItems.size();
            isLastPage = true;
        }

        List<Item> itemsOnCurrentPage = allItems.subList(
                                            (currentPageNumber - 1) * MAX_ITEMS_PER_PAGE, endIndex);

        req.setAttribute("pageNumber", Integer.toString(currentPageNumber));
        req.setAttribute("isLastPage", isLastPage);
        req.setAttribute("itemsList", itemsOnCurrentPage);

        req.getRequestDispatcher("/homepage.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
