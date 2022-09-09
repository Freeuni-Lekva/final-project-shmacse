<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.io.OutputStream" %>
<%@ page import="schmacse.model.Item" %>
<%@ page import="schmacse.daos.ItemDao" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="java.io.InputStream" %>
<%@ page import="java.io.ByteArrayOutputStream" %>
<%@ page import="java.io.File" %>
<%@ page import="java.nio.file.Files" %>
<%@ page import="java.awt.image.BufferedImage" %>
<%@ page import="javax.imageio.ImageIO" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
Connection connection = (Connection) request.getServletContext().getAttribute("DBConnection");
ItemDao itemDao = new ItemDao(connection);
Item item = null;
try {
    item = itemDao.getItemByItemID(Integer.parseInt(request.getParameter("item_id")));
} catch (SQLException e) {
    throw new RuntimeException(e);
}
response.setContentType("image/gif");

String path = request.getSession().getServletContext().getRealPath("/")+"images/bags.png";
File file = new File(path);
byte[] defaultImageBytes = Files.readAllBytes(file.toPath());

OutputStream img = response.getOutputStream();
if (item.getImageId() != 0){
    img.write(item.getImage((Connection) request.getServletContext().getAttribute("DBConnection")));
}else {
    img.write(defaultImageBytes);
}
img.write(defaultImageBytes);
img.flush();
img.close();
%>
