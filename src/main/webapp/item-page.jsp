<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="schmacse.model.Item" %>
<%@ page import="schmacse.model.User" %>
<%@ page import="schmacse.model.Category" %>
<%@ page import="java.io.OutputStream" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.util.List" %>
<%@ page import="schmacse.model.Review" %>
<%@ page import="java.util.Arrays" %>
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
        <div class="Reviews">
            <div class="row">
                <%
                    if (request.getAttribute("reviews") != null){
                        List<Review> reviews = (List<Review>) request.getAttribute("reviews");
//
//                        List<Review> reviews = Arrays.asList(new Review(11, 21, "komentari1"), new Review(12, 22, "komentari2"), new Review(13, 23, "komentari3"));
                        int coords = 120;

                        for (Review review: reviews) {
                %>
                <div class="col-xl-3 col-lg-4 col-md-6 col-sm-12 my-3" style="text-align: center; margin: 0 auto;">
                    <div class="card" style="width: 19rem; text-align: center; margin: 0 auto; border-radius: 15px; border-color: HoneyDew; border-width: medium; position: absolute; top: <%=coords%>px; right: -50px">
                        <div class="card-body" style="background: rgb(167, 190, 130); border-radius: 15px">

                            <%
                                String nameToDisplay = item.getName();
                                if(nameToDisplay.length() >= 15){
                                    nameToDisplay = nameToDisplay.substring(0,15) + "...";
                                }
                            %>
                            <h3 class="text-center fw-bold"><%out.println(review.getUser_id());%></h3>
                            <h5 class="text-center" style="opacity: 0.5;"><%out.println(review.getComment());%></h5>
                        </div>
                    </div>
                    <br>
                    <br>
                </div>

                <%          coords += 120;
                        }
                    }
                %>

            </div>
        </div>
        <div class="ReviewInput">
            <form>
                <label id="review"> Review : </label>
                <input type="hidden" name="itemId" value="<%=item.getId()%>">
                <input type="text" name="review-input" placeholder="Review.." style="border-radius: 8px; border-color: darkblue; border-width: 2px; background-color: #dde2eb; padding: 0px 5px;">
                <button formaction="item-page" formmethod="get" type="submit" id="submit-review">Submit Review</button>
            </form>
        </div>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/js/bootstrap.bundle.min.js" integrity="sha384-gtEjrD/SeCtmISkJkNUaaKMoLD0//ElJ19smozuHV6z3Iehds+3Ulb9Bn9Plx0x4" crossorigin="anonymous"></script>
</body>
</html>