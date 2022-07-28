<%
    if(session.getAttribute("name") == null){
        response.sendRedirect("login.jsp");
    }
%>

<html>
<body>
<h2>Hello World!</h2>
</body>
</html>