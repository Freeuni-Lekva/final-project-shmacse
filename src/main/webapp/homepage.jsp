<%@ page import="schmacse.model.Category" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h1> COCO </h1>
<form id="search-form">
    <select name="as" id="categories">
        <option value="ALL"> ALL </option>
        <%
            List<Category> categories = (List<Category>) request.getAttribute("categoryList");

            for (Category c: categories) {
                out.println("<option value=\"" + c + "\">" + c + "</option>");
            }
        %>
    </select>
    <input type="text" placeholder="Search..">
</form>
<button type="submit" form="search-form" value="Submit" id="search-button" name="search-button">Search</button>
</body>
</html>
