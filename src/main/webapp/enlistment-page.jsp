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

<form action="enlistment-page" method="post" id="enlistment-form" enctype="multipart/form-data">
    <label for="item-name"
        <%
            String item_name_prompt = (String) request.getAttribute("item-name-prompt");
            if (item_name_prompt != null){
                out.print("class=\"prompt\" ");
            }
        %>
        >Name of the item:
        <%
            if (item_name_prompt != null){
                out.print(item_name_prompt);
            }
        %>
    <br></label>

    <input type="text" name="item-name" id="item-name"
        <% if (request.getAttribute("item-name") != null) {
            String name = (String) request.getAttribute("item-name");
            out.println("value=\"" + name + "\"");
        } %>
    >
    <br>

    <label for="item-price"
        <%
            String item_price_prompt = (String) request.getAttribute("item-price-prompt");
            if (item_price_prompt != null){
                out.print("class=\"prompt\" ");
            }
        %>
        >price:
        <%
            if (item_price_prompt != null){
                out.print(item_price_prompt);
            }
        %>
        <br></label>

    <input type="number" name="item-price" id="item-price" min="0"
        <% if (request.getAttribute("item-price") != null) {
            String price = (String) request.getAttribute("item-price");
            out.println("value=\"" + price + "\"");
        }else{
            out.println("value=\"0\"");
        } %>
    >
    <br>

    <label for="categories">select category:<br></label>
    <select name="categories" id="categories">
        <%
            String selected = (String) request.getAttribute("categories");

            List<Category> categories = (List<Category>) request.getAttribute("categoryList");

            for (Category c: categories) {

                out.println("<option value=\"" + c + "\" ");

                if (selected != null && selected.equals(c.toString())) {
                    out.println("selected=\"selected\" ");
                }
                out.println(">" + c + "</option>");
            }
        %>
    </select><br>

    <label for="description"
        <%
            String description_prompt = (String) request.getAttribute("description-prompt");
            if (description_prompt != null){
                out.print("class=\"prompt\" ");
            }
        %>
        >Description:
        <%
            if (description_prompt != null){
                out.print(description_prompt);
            }
        %>
        <br></label>

        <textarea name="description" id="description"><% if (request.getAttribute("description") != null) {
                out.print((String) request.getAttribute("description"));
            } %></textarea>

    <br><br>

    <label for="image">Upload Image:<br></label>
    <input id="image" name="image" type="file" accept="image/*">

    <br>
</form>

<button type="submit" form="enlistment-form" value="Submit" id="enlist-button" name="enlist-button">Enlist</button>

</body>
</html>
