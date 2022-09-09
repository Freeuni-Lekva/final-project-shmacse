<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Admin Page</title>
    <link rel="stylesheet" type="text/css" href="css/admin-page.css">
</head>
<body>
<div class="container">
    <div class="To-Homepage">
        <a href="${pageContext.request.contextPath}/homepage" class="btn" role="button" id="homepage-button" name="homepage-button">
            <span>Go to Homepage</span>
        </a>
    </div>
    <div class="Shut-Down-Website">
        <form action="" method="post">
            <input type="submit" id="shut-down-button" name="shut-down-button" value="Shut Down Website">
        </form>
    </div>
    <div class="User-Search">
        <div class="User-Id-Title"></div>
        <div class="Full-Name-Title"></div>
        <div class="Phone-Number-Title"></div>
        <div class="isAdmin-Title"></div>
        <div class="User-Search-Area">
            <input type="text" placeholder="Username..">
        </div>
        <div class="User-Id">
            <label> User-Id </label>
        </div>
        <div class="Full-Name">
            <label> User-Id </label>
        </div>
        <div class="Phone-Number">
            <label> User-Id </label>
        </div>
        <div class="isAdmin">
            <label> User-Id </label>
        </div>
        <div class="Ban-Phone-Number">
            <label> User-Id </label>
        </div>
        <div class="Ban-Username">
            <label> User-Id </label>
        </div>
        <div class="Delete-User">
            <label> User-Id </label>
        </div>
        <div class="Visit-Items">
            <label> User-Id </label>
        </div>
        <div class="Make-Admin">
            <label> User-Id </label>
        </div>
    </div>
    <div class="Item-Search">
        <div class="Item-Id-Title"></div>
        <div class="Item-Name-Title"></div>
        <div class="Item-Price-Title"></div>
        <div class="Item-Category-Title"></div>
        <div class="Item-Search-Area"></div>
        <div class="Item-Id"></div>
        <div class="Item-Name"></div>
        <div class="Item-Price"></div>
        <div class="Visit-Item"></div>
        <div class="Delete-Item"></div>
        <div class="Owner-Username"></div>
        <div class="Item-Category"></div>
    </div>
    <div class="Remove-Image"></div>
    <div class="Item-Image"></div>
    <div class="Gap"></div>
</div>
</body>
</html>
