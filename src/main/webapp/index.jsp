<%
    if(session.getAttribute("username") == null){
        response.sendRedirect("homepage");
    }
%>

<html>
<body>
<h2>Hello World!</h2>
</body>
</html>