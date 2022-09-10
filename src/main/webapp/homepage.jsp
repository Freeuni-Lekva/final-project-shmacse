<%@ page import="schmacse.model.Category" %>
<%@ page import="java.util.List" %>
<%@ page import="schmacse.model.Item" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Shmacse store</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0/dist/css/bootstrap.min.css" rel="stylesheet" >
    <link rel="stylesheet" type="text/css" href="css/homepage.css">
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js" ></script>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/js/bootstrap.min.js"
            integrity="sha384-Atwg2Pkwv9vp0ygtn1JAojH0nYbwNJLPhwyoVbhoPwBhjQPR5VtM2+xf0Uwh9KtT"
            crossorigin="anonymous"></script>
</head>
<body style="background: rgb(250,248,223);
background: radial-gradient(circle, rgba(250,248,223,1) 50%, rgba(222,218,173,1) 100%);">
<h1> Shmacse </h1>
<form id="search-form" action="search-servlet" method="post">
    <label for="categories" id="categories-label"> Categories: </label>
    <select name="categories" id="categories" style="background-color: #dde2eb; border-radius: 10px; padding: 0px 5px; font-size: 15px;">
        <option value="ALL"> ALL </option>
        <%
            List<Category> categories = (List<Category>) request.getAttribute("categoryList");

            for (Category c: categories) {
                out.println("<option value=\"" + c + "\">" + c + "</option>");
            }
        %>
    </select>
    <input name="textfield" id=textfield" type="text" placeholder="Search.." style="border-radius: 8px; border-color: darkblue; border-width: 2px; background-color: #dde2eb; padding: 0px 5px;">
    <button type="submit" form="search-form" value="Submit" id="search-button" name="search-button">Search</button>

    <label for="button" id="price-order"> Sort by price:  </label>
    <span id="button">
        <input type="checkbox" id="invert" name="invert"
            <%if (request.getParameterValues("invert") != null){%> checked <% } %>
        />
        <span class="knobs"></span>
    </span>

    <span id="top-right-corner" name="top-right-corer">
    <%
        if(request.getSession().getAttribute("username") != null){
    %>

        <button type="submit" formaction="my-items" formmethod="get" id="to-my-items" name="to-my-items"> My Items </button>
        <button type="submit" formaction="log-out" formmethod="post" id="log-out" name="log-out"> Log Out </button>

    <%
        }else{
    %>
    <input type="submit" formaction="login.jsp" value="Log In" id="log-in" name="log-in">
    <input type="submit" formaction="registration.jsp" value="Register" id="register" name="register">
    <%
        }
    %>
    </span>
</form>

<div class="row">
    <%
        if (request.getAttribute("itemsList") != null){
            List<Item> items = (List<Item>) request.getAttribute("itemsList");

            for (Item item: items) {
    %>
        <div class="col-xl-3 col-lg-4 col-md-6 col-sm-12 my-3" style="text-align: center; margin: 0 auto;">
            <div class="card" style="width: 19rem; text-align: center; margin: 0 auto; border-radius: 15px; border-color: HoneyDew; border-width: medium">
                <div class="card-body" style="background: rgb(167, 190, 130); border-radius: 15px">

                    <%
                        String nameToDisplay = item.getName();
                        if(nameToDisplay.length() >= 15){
                            nameToDisplay = nameToDisplay.substring(0,15) + "...";
                        }
                    %>
                    <h3 class="text-center fw-bold"><%=nameToDisplay%></h3>
                    <h5 class="text-center" style="opacity: 0.5; font-size: 18px;"><%=item.getPrice()+" ₾"%></h5>

                    <img src="getImage.jsp?item_id=<%=item.getId()%>" style="width: 242px; height: 133px; white-space: nowrap; object-fit: contain;">
                    <div class="text-center my-3">
                        <form>
                            <input type="hidden" name="itemId" value="<%=item.getId()%>">
                            <button formaction="item-page" formmethod="get" id="visit-button" name="visit-button"
                            >Visit</button>
                        </form>
                    </div>

                </div>
            </div>
        </div>

    <%
            }
        }
    %>

</div>

<form action="homepage" method="get" style="text-align: center" id="south-form">
    <input type="hidden" value="<%=(String) request.getAttribute("pageNumber")%>" id="pageNumber" name="pageNumber">

    <% if(request.getParameterValues("invert") != null){ %>
        <input type="hidden" value="<%=request.getParameterValues("invert")[0]%>"  name="invert">
    <% } %>

    <input
            type="submit" value="Previous Page" id="previous-page" onclick="changePage('previous')"
            <% if( (((String)request.getAttribute("pageNumber")).equals("1"))){ %>
                style="opacity: 0.4; cursor: not-allowed; transition-duration: 1000s;" disabled
            <% } %>
        >

        <label id="pageNumber-label" style="font-size: 17px;"> <%=(String) request.getAttribute("pageNumber")%> </label>

        <input
                type="submit" value="Next Page" id="next-page" onclick="changePage('next')"
            <% if(((Boolean) request.getAttribute("isLastPage"))){ %>
                style="opacity: 0.4; cursor: not-allowed; transition-duration: 1000s;" disabled
            <% } %>
        >

</form>

<script>
    function changePage(direction){
        let hiddenParameter = document.getElementById('pageNumber');
        let oldValue = parseInt(hiddenParameter.getAttribute('value'));
        if(direction === 'next'){
            oldValue = oldValue + 1;
        }
        if(direction === 'previous'){
            oldValue = oldValue - 1;
        }
        hiddenParameter.setAttribute('value', oldValue.toString())
    }
</script>
</body>
</html>
