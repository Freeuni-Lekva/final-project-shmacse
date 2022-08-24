<%@ page import="schmacse.model.Category" %>
<%@ page import="java.util.List" %>
<%@ page import="schmacse.model.Item" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Shmacse store</title>
</head>
<body>
<h1> COCO </h1>
<form id="search-form" action="search-servlet" method="post">
    <select name="categories" id="categories">
        <option value="ALL"> ALL </option>
        <%
            List<Category> categories = (List<Category>) request.getAttribute("categoryList");

            for (Category c: categories) {
                out.println("<option value=\"" + c + "\">" + c + "</option>");
            }
        %>
    </select>
    <input name="textfield" id=textfield" type="text" placeholder="Search..">
    <span class="button r" id="button">
        <label for="Invert">Price: </label><input type="checkbox" class="checkbox" id="Invert"/>
        <span class="knobs"></span>
    </span>
</form>
<button type="submit" form="search-form" value="Submit" id="search-button" name="search-button">Search</button><br>



<%
    if (request.getAttribute("itemsList") != null){
        List<Item> items = (List<Item>) request.getAttribute("itemsList");
        System.out.println("itemsList size: " + items.size());

        for (Item i: items) {
            System.out.println("printed " + i);
            out.println("<label>" + i + "</label><br>");
        }
    }

%>

</body>
</html>
