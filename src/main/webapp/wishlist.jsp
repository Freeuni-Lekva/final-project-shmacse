<%@ page import="schmacse.model.Item" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Wishlist</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0/dist/css/bootstrap.min.css" rel="stylesheet" >

    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js" ></script>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/js/bootstrap.min.js"
            integrity="sha384-Atwg2Pkwv9vp0ygtn1JAojH0nYbwNJLPhwyoVbhoPwBhjQPR5VtM2+xf0Uwh9KtT"
            crossorigin="anonymous"></script>
</head>
<body>

<section id="my-wishlist" class="container bg-light py-4">

    <h1 class="text-center">My Wishlist</h1>

    <div class="row">
        <%
            List<Item> itemsInWishlist = (List<Item>) request.getAttribute("itemsInWishlist");

            for (Item item: itemsInWishlist) {
        %>

        <div class="col-lg-4 col-md-6 col-sm-10 my-3">
            <div class="card" style="width: 18rem;">
                <div class="card-body">

                    <h3><%=item.getName()%></h3>
                    <p class="card-text"><%=item.getDescription()%></p><br/>
                    <p class="card-text">Price: <%=item.getPrice()%></p>

                    <div class="text-center my-3">
                        <form action="wishlist" method="post">
                            <input type="hidden" name="itemId" value="<%=item.getId()%>">
                            <button class="btn btn-danger">Delete</button>
                        </form>
                    </div>

                </div>
            </div>
        </div>

        <%}%>
    </div>
</section>

</body>
</html>
