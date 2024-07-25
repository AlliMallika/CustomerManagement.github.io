<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Login Page</title>
    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
</head>
<body>
    <h2>Login Page</h2>
    <form action="/CustomerManagement/auth" method="post">
        <label for="username">Username:</label>
        <input type="text" id="username" placeholder="login id" name="username"><br><br>
        <label for="password">Password:</label>
        <input type="password" id="password" placeholder="Password" name="password"><br><br>
        <input type="submit" class="btn btn-primary" value="Login">
    </form>
    <!-- Bootstrap JS -->
    <script src="${pageContext.request.contextPath}/js/bootstrap.bundle.min.js"></script>
</body>
</html>

