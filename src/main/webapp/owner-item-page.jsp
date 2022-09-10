<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="schmacse.model.Item" %>
<%@ page import="schmacse.model.User" %>
<%@ page import="schmacse.model.Category" %>
<%@ page import="java.util.List" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Item Page</title>
    <link rel="stylesheet" type="text/css" href="css/owner-item-page.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-+0n0xVW2eSR5OomGNYDnhzAbDsOXxcvSN1TPprVMTNDbiYZCxYbOOl7+AMvyTG2x" crossorigin="anonymous">
</head>
<body>
<%
    Item item = (Item) request.getAttribute("item");
    User owner = (User) request.getAttribute("user");
%>
<div class="container">
    <div class="MyItemsButton">
        <a href="${pageContext.request.contextPath}/my-items" class="btn" role="button" id="my-items-button">
            <span>Go to My Items</span>
        </a>
    </div>
    <div class="Description-header">
        <div class="fs-3" id="description-header"> Description </div>
    </div>
    <div class="Description">
        <textarea name="description" id="description" value="<%=item.getDescription()%>"><%=item.getDescription()%> </textarea>
    </div>
    <div class="Price">
        <label for="updated-price" id="label-first"> Price : </label>
        <input type="text" id="updated-price" value="<%=Integer.toString(item.getPrice())%>">
        <label class="pe-5"id="label-second"> ₾ </label>
    </div>
    <div class="Image">
        <img src="getImage.jsp?item_id=<%=item.getId()%>" id="image" style="width: 800px; height: 520px; white-space: nowrap;">
        <span id="for-new-image" name="for-new-image">
            <label for="new-image">Select New Image:</label>
            <input id="new-image" name="new-image" type="file" accept="image/*">
        </span>
    </div>

    <div class="Owner">
            <span id="owner-info">
                <label id="owner-name">Owner: <%= owner.getFirstName() %></label>
                <br>
                <label for="owner-phone"> Phone Number:</label>
                <input type="text" id="owner-phone" value="<%= owner.getPhoneNumber() %>">
            </span>
    </div>
    <div class="Title">
        <input type="text" id="item-name" value="<%=item.getName()%>">
    </div>
    <div class="Category">
        <select name="categories" id="categories">
            <option value="<%=item.getCategory().toString()%>"> <%=item.getCategory().toString()%> </option>
            <%
                List<Category> categories = (List<Category>) request.getAttribute("categoryList");

                for (Category c: categories) {
                    out.println("<option value=\"" + c + "\">" + c + "</option>");
                }
            %>
        </select>
    </div>
    <div class="Update-Page">
        <form action="${pageContext.request.contextPath}/owner-item-page" method="post">
            <input type="submit" id="update-button" value="Update Information" onclick="prepeareHiddenParameters()">

            <input type="hidden" id="hidden-updated-owner-phone" name="updated-owner-phone" value="">
            <input type="hidden" id="hidden-updated-price" name="updated-price" value="">
            <input type="hidden" id="hidden-updated-description" name="updated-description" value="">
            <input type="hidden" id="hidden-updated-item-name" name="updated-item-name" value="">
            <input type="hidden" id="hidden-updated-category" name="updated-category" value="">
            <input type="hidden" id="hidden-updated-image" name="updated-image" value="">

            <input type="hidden" value="<%=item.getId()%>" name="itemId">
        </form>
    </div>
</div>
<script>
    function prepeareHiddenParameters() {
        document.getElementById('hidden-updated-owner-phone').setAttribute('value', document.getElementById('owner-phone').value);
        document.getElementById('hidden-updated-price').setAttribute('value', document.getElementById('updated-price').value);
        document.getElementById('hidden-updated-description').setAttribute('value', document.getElementById('description').value);
        document.getElementById('hidden-updated-item-name').setAttribute('value', document.getElementById('item-name').value);
        document.getElementById('hidden-updated-category').setAttribute('value', document.getElementById('categories').value);
        document.getElementById('hidden-updated-image').setAttribute('value', document.getElementById('new-image').value);
    }
</script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/js/bootstrap.bundle.min.js" integrity="sha384-gtEjrD/SeCtmISkJkNUaaKMoLD0//ElJ19smozuHV6z3Iehds+3Ulb9Bn9Plx0x4" crossorigin="anonymous"></script>
</body>
</html>