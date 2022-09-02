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
<body>
<h1> Homepage </h1>
<form id="search-form" action="search-servlet" method="post">
    <label for="categories" id="categories-label"> Categories: </label>
    <select name="categories" id="categories">
        <option value="ALL"> ALL </option>
        <%
            List<Category> categories = (List<Category>) request.getAttribute("categoryList");

            for (Category c: categories) {
                out.println("<option value=\"" + c + "\">" + c + "</option>");
            }
        %>
    </select>
    <input name="textfield" id=textfield" type="text" placeholder="Search..">
    <button type="submit" form="search-form" value="Submit" id="search-button" name="search-button">Search</button>

    <label for="button" id="price-order"> Sort by price:  </label>
    <span class="button r" id="button">
        <input type="checkbox" class="checkbox" id="invert" name="invert"/>
        <span class="knobs"></span>
    </span>

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
</form>

<div class="row">
    <%
        if (request.getAttribute("itemsList") != null){
            List<Item> items = (List<Item>) request.getAttribute("itemsList");

            for (Item item: items) {
    %>

        <div class="col-lg-3 col-md-4 col-sm-10 my-3">
            <div class="card" style="width: 18rem;">
                <div class="card-body">

                    <%
                        String nameToDisplay = item.getName();
                        if(nameToDisplay.length() >= 15){
                            nameToDisplay = nameToDisplay.substring(0,15) + "...";
                        }
                    %>
                    <h3 class="text-center"><%=nameToDisplay%></h3>
                    <h5 class="text-center"><%=item.getPrice()+" GEL"%></h5>

                    <div class="text-center my-3">
                        <form>
                            <input type="hidden" name="itemId" value="<%=item.getId()%>">
                            <button formaction="item-page" formmethod="get" class="btn btn-primary">Visit</button>
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

<form action="homepage" method="get">
    <input type="hidden" value="<%=(String) request.getAttribute("pageNumber")%>" id="pageNumber" name="pageNumber">

    <%
        if(!((boolean)request.getAttribute("isLastPage"))){

    %>
    <input type="submit" value="Next Page" id="next-page" onclick="changePage('next')">
    <%
        }
        if( !(((String)request.getAttribute("pageNumber")).equals("1"))){
    %>
        <input type="submit" value="Previous Page" id="previous-page" onclick="changePage('previous')">
    <%
        }
    %>
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
