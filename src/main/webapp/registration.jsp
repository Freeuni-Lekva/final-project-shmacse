<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
	<link href="css/registration.css" type="text/css" rel="stylesheet">
	<title>shmacse</title>
</head>
<body>

<div class="registration-form">
	<img src="images/bags.png"  alt="." style="width:100px;height:auto;">
	<form action="register" method="post">
		<div class = "form-group">
			<input type="text" class="input-box" placeholder="First name" name="firstname">
		</div>
		<input type="text" class="input-box" placeholder="Last name" name="lastname">
		<input type="text" class="input-box" placeholder="Username " name="username">
		<input type="text" class="input-box" placeholder="Phone No." name="contact">
		<input type="password" class="input-box" placeholder="Password" name="pass">
		<input type="password" class="input-box" placeholder="Repeat Password" name="re_pass"> <br>
		<% if (request.getAttribute("status") != null){ %>
		<%  if (request.getAttribute("status").equals("failed, passwords do not match")) {%>
		<label form="registration-form"> Passwords do not match </label>
		<% }else if(request.getAttribute("status").equals("failed, username is already taken")) { %>
		<label form="registration-form"> Username is already taken </label>
		<% }else if (request.getAttribute("status").equals("failed, invalid number")) {%>
		<label form="registration-form"> Please enter valid contact number </label>
		<% }else if (request.getAttribute("status").equals("failed, empty field(s)")) {%>
		<label form="registration-form"> All fields must be filled </label>
		<% }else if (request.getAttribute("status").equals("failed, phone number is taken")) {%>
		<label form="registration-form"> Contact number is already taken </label>
		<% }}%>
		<br><br>
		<button type="submit" class="register">Sign up</button>
		<br><br>
		<a style="font-size: smaller;color: rgb(40, 65, 24)" class="registered-link" href="login.jsp">Already registered? Log in</a>
	</form>
	<h1> Create account </h1>
</div>



</body>
</html>