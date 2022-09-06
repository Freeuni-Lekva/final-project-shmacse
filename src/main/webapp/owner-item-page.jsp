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
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-+0n0xVW2eSR5OomGNYDnhzAbDsOXxcvSN1TPprVMTNDbiYZCxYbOOl7+AMvyTG2x" crossorigin="anonymous">
</head>
<body>
<%
    Item item = (Item) request.getAttribute("item");
    User owner = (User) request.getAttribute("user");

%>
<h1 class="display-3 text-center text-bold"><%=item.getName()%></h1>
<h2 class="fs-4 text-center text-muted"><%=item.getCategory().toString()%></h2>


<br>
<br>
<div class="container">
    <div class="row gx-5 gy-5">
        <div class="col-5 text-center py-1">
            <div class="fs-3"> Description </div>
        </div>
        <div class="col-7 text-center py-2">
            <form action="owner-item-page" method="post" style="display: inline">
                <label for="newPrice"> Price : </label>
                <input type="text" id="newPrice" name="newPrice" value="<%=Integer.toString(item.getPrice())%>">
                <label class="pe-5" for="itemId"> GEL </label>
                <input type="hidden" id="itemId" name="itemId" value="<%=item.getId()%>">
                <button class="btn btn-primary">Update Price</button>
            </form>
        </div>

    </div>
    <div class="row gx-5 gy-1">
        <div class="col-5 text-center">
            <div class="fs-5 text-muted"> <%=item.getDescription()%> </div>
        </div>
        <div class="col-7 text-center d-none d-md-block">
            <!-- <img src="/assets/surati.jpg" class="img-fluid" alt="ebook"> -->
            <img src="images/bags.png">
        </div>
        <div class="col-md-5 text-center d-none d-md-block">
        </div>
    </div>
    <div class="row">
        <div class="col text-end"></div>
    </div>
    <div class="row">
        <div class="col text-end">
            <div class="fs-5 text-muted"> Owner: <%= owner.getFirstName()%> </div>
        </div>
    </div>
    <div class="row">
        <div class="col text-end">
            <div class="fs-5 text-muted"> Phone Number: <%= owner.getPhoneNumber() %> </div>
        </div>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/js/bootstrap.bundle.min.js" integrity="sha384-gtEjrD/SeCtmISkJkNUaaKMoLD0//ElJ19smozuHV6z3Iehds+3Ulb9Bn9Plx0x4" crossorigin="anonymous"></script>
</body>
</html>