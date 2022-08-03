<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Error Page</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-+0n0xVW2eSR5OomGNYDnhzAbDsOXxcvSN1TPprVMTNDbiYZCxYbOOl7+AMvyTG2x" crossorigin="anonymous">
</head>
    <%
        String errorMessage = (String) request.getAttribute("error-message");
        String backTo = (String) request.getAttribute("back-to");
    %>

    <h1 class="display-2"> Error Page </h1>
    <h2 class="display-6"> <%= errorMessage %> </h2>

    <br>
    <form action= <%= backTo %> method="get">
        <%
            String attribute = "";
            int attrInt = 0;
            if(backTo.equals("item-page")){
                attrInt = (int) request.getAttribute("itemID");
                request.setAttribute("itemID", attribute);
            }
        %>
        <button class="btn btn-info">Go Back</button>
    </form>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/js/bootstrap.bundle.min.js" integrity="sha384-gtEjrD/SeCtmISkJkNUaaKMoLD0//ElJ19smozuHV6z3Iehds+3Ulb9Bn9Plx0x4" crossorigin="anonymous"></script>
    </body>
</html>
