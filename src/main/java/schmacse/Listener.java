package schmacse;

import schmacse.daos.ItemDao;
import schmacse.databaseconnection.DBConnection;
import schmacse.model.Item;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebListener
public class Listener implements ServletContextListener, HttpSessionListener, HttpSessionAttributeListener {

    public Listener() {
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        /* This method is called when the servlet context is initialized(when the Web application is deployed). */
        sce.getServletContext().setAttribute("DBConnection", DBConnection.getConnection());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        /* This method is called when the servlet Context is undeployed or Application Server shuts down. */
        DBConnection.closeConnection();
    }

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        /* Session is created. */
        Connection connection = DBConnection.getConnection();

        ItemDao itemDao = new ItemDao(connection);
        try {
            List<Item> itemsList = itemDao.getFilteredItems("", null);
            se.getSession().setAttribute("itemsList", itemsList);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //TODO initiate user session (privileges, shopping cart etc.).
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        /* Session is destroyed. */
    }

    @Override
    public void attributeAdded(HttpSessionBindingEvent sbe) {
        /* This method is called when an attribute is added to a session. */
    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent sbe) {
        /* This method is called when an attribute is removed from a session. */
    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent sbe) {
        /* This method is called when an attribute is replaced in a session. */
    }
}
