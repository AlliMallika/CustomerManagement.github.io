<!DOCTYPE html>
<html>
<head>
    <title>Add Customer</title>
    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
</head>
<body>
<h1>Add Customer</h1>
<form method="post" action="customers">
    <label for="firstName">First Name:</label><br>
    <input type="text" id="firstName" name="firstName"><br>
    <label for="lastName">Last Name:</label><br>
    <input type="text" id="lastName" name="lastName"><br>
    <label for="street">Street:</label><br>
    <input type="text" id="street" name="street"><br>
    <label for="address">Address:</label><br>
    <input type="text" id="address" name="address"><br>
    <label for="city">City:</label><br>
    <input type="text" id="city" name="city"><br>
    <label for="state">State:</label><br>
    <input type="text" id="state" name="state"><br>
    <label for="email">Email:</label><br>
    <input type="text" id="email" name="email"><br>
    <label for="phone">Phone:</label><br>
    <input type="text" id="phone" name="phone"><br>
    <button type="submit" name="action" class="btn btn-success" value="add">Add Customer</button>
</form>
<a href="customers" class="btn btn-secondary">Back to Customer List</a>
 <!-- Bootstrap JS -->
    <script src="${pageContext.request.contextPath}/js/bootstrap.bundle.min.js"></script>
</body>
</html>
