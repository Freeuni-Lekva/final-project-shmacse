<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
	<link href="css/login.css" type="text/css" rel="stylesheet">
	<title>shmacse</title>
</head>
<body>

<div class="login-form">
	<img src="images/cart.png"  alt="." style="width:130px;height:auto;">
	<h1>Log in</h1>
	<form action="login" method="post">
		<input type="text" class="input-box" placeholder="Username " name="username">
		<input type="password" class="input-box" placeholder="Password" name="pass">
		<% if (request.getAttribute("status") != null){ %>
		<%  if (request.getAttribute("status").equals("failed, username not found")) {%>
		<label form="login-form"> Invalid username </label>
		<% }else if(request.getAttribute("status").equals("failed, password incorrect")) { %>
		<label form="login-form"> Password incorrect </label>
		<% }}%>
		<br>
		<button type="submit" class="login-button">Sign in</button>
		<br>
		<br>
		<a style="font-size: smaller;color: rgb(40, 65, 24)" class="newtohere-link" href="registration.jsp">New to here? Sign up!</a>
	</form>
</div>



</body>
</html>