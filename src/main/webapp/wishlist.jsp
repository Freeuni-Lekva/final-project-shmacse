<%@ page import="schmacse.model.Item" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
     <span name="span-my-items" id="span-my-items">
        <a href="${pageContext.request.contextPath}/my-items" class="btn" role="button" id="my-items-button" name="my-items-button">
            <span>My Items</span>
        </a>
    </span>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Wishlist</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0/dist/css/bootstrap.min.css" rel="stylesheet" >

    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js" ></script>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/js/bootstrap.min.js"
            integrity="sha384-Atwg2Pkwv9vp0ygtn1JAojH0nYbwNJLPhwyoVbhoPwBhjQPR5VtM2+xf0Uwh9KtT"
            crossorigin="anonymous"></script>
    <link rel="stylesheet" type="text/css" href="css/wishlist.css">
</head>
<body>

<section id="my-wishlist" class="container py-4">

    <h1 class="text-center">My Wishlist</h1>
    <br>



    <div class="row">
        <%
            List<Item> itemsInWishlist = (List<Item>) request.getAttribute("itemsInWishlist");

            for (Item item: itemsInWishlist) {
        %>

        <div class="col-lg-4 col-md-6 col-sm-10 my-3">
            <div class="card" style="width: 18rem; text-align: center; margin: 0 auto; border-radius: 15px; border-width: medium; border-color: HoneyDew">
                <div class="card-body" style="background: rgb(167, 190, 130); border-radius: 15px">

                    <h3 id="item-name"><%=item.getName()%></h3>
                    <p class="card-text" id="item-category"><%=item.getCategory().toString()%></p>
                    <p class="card-text" id="item-price">Price: <%=item.getPrice()%> ₾</p>

                    <img src="getImage.jsp?item_id=<%=item.getId()%>" style="width: 242px; height: 133px; white-space: nowrap; object-fit: contain;">
                    <div class="text-center my-3">
                        <form>
                            <input type="hidden" name="itemId" value="<%=item.getId()%>">
                            <button formaction="item-page" formmethod="get" class="bton vist" style="background-color: #a7c7e7; border-radius: 10px">Visit</button>
                            <button formaction="wishlist" formmethod="post" class="bton delete" style="background-color: #ff6961; border-radius: 10px">Delete</button>
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
