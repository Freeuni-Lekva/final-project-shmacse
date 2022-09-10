<%@ page import="schmacse.model.Item" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <span name="span-homepage" id="span-homepage">
        <a href="${pageContext.request.contextPath}/homepage" class="btn" role="button" id="homepage-button" name="homepage-button">
            <span>Shmacse Store</span>
        </a>
    </span>
    <span name="span-enlist" id="span-enlist">
        <a href="${pageContext.request.contextPath}/enlistment-page" class="btn" role="button" id="enlist-button" name="enlist-button">
            <span>Add Item</span>
        </a>
    </span>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Items</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0/dist/css/bootstrap.min.css" rel="stylesheet" >
    <link rel="stylesheet" type="text/css" href="css/my-items.css">

    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js" ></script>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/js/bootstrap.min.js"
            integrity="sha384-Atwg2Pkwv9vp0ygtn1JAojH0nYbwNJLPhwyoVbhoPwBhjQPR5VtM2+xf0Uwh9KtT"
            crossorigin="anonymous"></script>
</head>
<body>

<section id="my-notes" class="container py-4">

    <h1 class="text-center">My Items to Sell</h1>

    <div class="row">
        <%
            List<Item> myItemList = (List<Item>) request.getAttribute("myItemsList");

            for (Item item: myItemList) {
        %>

            <div class="col-lg-4 col-md-6 col-sm-10 my-3">
                <div class="card" style="width: 18rem; text-align: center; margin: 0 auto; border-radius: 15px; border-width: medium; border-color: HoneyDew">
                    <div class="card-body" style="background: rgb(167, 190, 130); border-radius: 15px">

                        <h3><%=item.getName()%></h3>
                        <p class="card-text"><%=item.getDescription()%></p>

<%--                        <img src="images/bags.png" style="width: 242px; height: 133px; white-space: nowrap;">--%>
                        <img src="getImage.jsp?item_id=<%=item.getId()%>" style="width: 242px; height: 133px; white-space: nowrap; object-fit: contain;">
                        <div class="text-center my-3">
                            <form>
                                <input type="hidden" name="itemId" value="<%=item.getId()%>">
                                <button formaction="owner-item-page" formmethod="get" class="bton vist" style="background-color: #a7c7e7; border-radius: 10px">Visit</button>
                                <button formaction="delete-item" formmethod="post" class="bton delete" style="background-color: #ff6961; border-radius: 10px">Delete</button>
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
