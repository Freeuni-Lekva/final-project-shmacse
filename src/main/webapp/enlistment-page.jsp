<%@ page import="schmacse.model.Category" %>
<%@ page import="java.util.List" %>
<%@ page import="java.io.IOException" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Enlistment Page</title>
    <link rel="stylesheet" type="text/css" href="css/enlistment-page.css">
</head>
<body id="body">
<h1>Enlist a new item</h1>

<form action="enlistment-page" method="post" id="enlistment-form">
    <label for="item-name">Name of the item:<br></label>
        <input type="text" name="item-name" id="item-name">
    <%
        String item_name_prompt = (String) request.getAttribute("item-name-prompt");
        if (item_name_prompt != null){
            out.println("<label class=\"prompt\">" + item_name_prompt + "</label>");
        }
    %>
    <br>

    <label for="categories">select category:<br></label>
    <select name="categories" id="categories">
        <%
            List<Category> categories = (List<Category>) request.getAttribute("categoryList");

            for (Category c: categories) {
                out.println("<option value=\"" + c + "\">" + c + "</option>");
            }
        %>
    </select><br>

    <label for="description">Description:<br></label>

        <textarea name="description" id="description"></textarea>
    <%
        String description_prompt = (String) request.getAttribute("description-prompt");
        if (description_prompt != null){
            out.println("<label class=\"prompt\">" + description_prompt + "</label>");
        }
    %>
    <br>

<%--    <input type="image"><br>--%>
</form>

<button type="submit" form="enlistment-form" value="Submit" id="enlist-button" name="enlist-button">Enlist</button>

</body>
</html>
