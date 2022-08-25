<%@ page import="schmacse.model.Category" %>
<%@ page import="java.util.List" %>
<%@ page import="schmacse.model.Item" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Shmacse store</title>
    <link rel="stylesheet" type="text/css" href="css/homepage.css">
</head>
<body>
<h1> COCO </h1>
<form id="search-form" action="search-servlet" method="post">
    <label for="categories" id="categories-label"> Categories: </label>
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
    <button type="submit" form="search-form" value="Submit" id="search-button" name="search-button">Search</button>

    <label for="button" id="price-order"> Sort by price:  </label>
    <span class="button r" id="button">
        <input type="checkbox" class="checkbox" id="invert" name="invert"/>
        <span class="knobs"></span>
    </span>
    <button type="submit" formaction="my-items" formmethod="get" id="to-my-items" name="to-my-items"> My Items </button>
    <button type="submit" formaction="log-out" formmethod="post" id="log-out" name="log-out"> Log Out </button>
</form>



<%
    if (request.getAttribute("itemsList") != null){
        List<Item> items = (List<Item>) request.getAttribute("itemsList");
//        System.out.println("itemsList size: " + items.size());

        for (Item i: items) {
//            System.out.println("printed " + i);
            out.println("<label>" + i + "</label><br>");
        }
    }

%>

</body>
</html>
