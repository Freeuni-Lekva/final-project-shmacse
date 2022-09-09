<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="schmacse.model.Item" %>
<%@ page import="schmacse.model.User" %>
<%@ page import="schmacse.model.Category" %>
<%@ page import="java.io.OutputStream" %>
<%@ page import="java.sql.Connection" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Item Page</title>
    <link rel="stylesheet" type="text/css" href="css/item-page.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-+0n0xVW2eSR5OomGNYDnhzAbDsOXxcvSN1TPprVMTNDbiYZCxYbOOl7+AMvyTG2x" crossorigin="anonymous">
</head>
<body>
    <%
        Item item = (Item) request.getAttribute("item");
        User owner = (User) request.getAttribute("user");
    %>
    <div class="container">
        <div class="HomepageButton">
            <a href="${pageContext.request.contextPath}/homepage" class="btn" role="button" id="homepage-button" name="homepage-button">
                    <span>Go to Homepage</span>
            </a>
        </div>
        <div class="Description-header">
            <div class="fs-3" id="description-header"> Description </div>
        </div>
        <div class="Description">
            <div class="fs-5 text-muted" id="description"> <%=item.getDescription()%> </div>
        </div>
        <div class="Price">
            <div class="text-center bg-light border" id="price">
                Price: <%= Integer.toString(item.getPrice()) %> ₾
            </div>
        </div>
        <div class="Add-To-Wishlist">
            <%
                if(request.getSession().getAttribute("username") != null){
                    if(!(((String)request.getSession().getAttribute("username")).equals(owner.getUsername()))){
            %>
            <div class="text-start py-2" id="wishlist-div">
                <form action="add-to-wishlist" method="post" id="wishlist-form">
                    <input type="hidden" name="itemId" value="<%=item.getId()%>">
                    <input type="hidden" name="ownerId" value="<%=item.getUserId()%>">
                    <button class="btn btn-success" id="wishlist-button">Add to WishList</button>
                </form>
            </div>
            <%
                    }
                }
            %>
        </div>
        <div class="Image">
           <img src="getImage.jsp?item_id=<%=item.getId()%>" id="image" style="width: 800px; height: 520px; white-space: nowrap;">
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