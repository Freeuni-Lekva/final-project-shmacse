<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="schmacse.model.Item" %>
<%@ page import="schmacse.model.User" %>
<%@ page import="schmacse.model.Category" %>
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
        <textarea name="description" id="description"></textarea>
    </div>
    <div class="Price">
        <label for="newPrice" id="label-first"> Price : </label>
        <input type="text" id="newPrice" name="newPrice" value="<%=Integer.toString(item.getPrice())%>">
        <label class="pe-5"id="label-second"> ₾ </label>
    </div>
    <div class="Image">
        <img src="images/bags.png" id="image">
    </div>

    <div class="Owner">
            <span id="owner-info">
                <label id="owner-name">Owner: <%= owner.getFirstName() %></label>
                <label id="owner-phone">Phone Number: <%= owner.getPhoneNumber() %></label>
            </span>
    </div>
    <div class="Title">
        <h1 class="display-3 text-center text-bold"><%=item.getName()%></h1>
    </div>
    <div class="Category">
        <h2 class="fs-4 text-center text-muted"><%=item.getCategory().toString()%></h2>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/js/bootstrap.bundle.min.js" integrity="sha384-gtEjrD/SeCtmISkJkNUaaKMoLD0//ElJ19smozuHV6z3Iehds+3Ulb9Bn9Plx0x4" crossorigin="anonymous"></script>
</body>
</html>