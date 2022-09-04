<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
	<link href="css/registration.css" type="text/css" rel="stylesheet">
	<title>shmacse</title>
</head>
<body>

<div class="registration-form">
	<img src="images/bags.png"  alt="." style="width:100px;height:auto;">
	<h1> Create account </h1>
	<form action="register" method="post">
		<div class = "form-group">
			<input type="text" class="input-box" placeholder="First name" name="firstname">
		</div>
		<input type="text" class="input-box" placeholder="Last name" name="lastname">
		<input type="text" class="input-box" placeholder="Username " name="username">
		<input type="text" class="input-box" placeholder="Phone No." name="contact">
		<input type="password" class="input-box" placeholder="Password" name="pass">
		<input type="password" class="input-box" placeholder="Repeat Password" name="re_pass"> <br><br>
		<%  if (request.getAttribute("status") != null) {%>
		<label form="registration-form"> Passwords do not match </label>

		<% }%>
		<br>
		<button type="submit" class="register"> Sign up</button>
		<br>
		<a style="font-size: smaller;color: rgb(40, 65, 24)" class="registered-link" href="http://localhost:8080/shmacse/login.jsp">Already registered? Log in</a>
	</form>
</div>



</body>
</html>