<%@ page import="com.example.dao.CustomerDAO" %>
<%@ page import="com.example.model.Customer" %>
<%
    String uuid = request.getParameter("uuid");
    CustomerDAO customerDAO = new CustomerDAO();
    Customer customer = customerDAO.getCustomerById(uuid);
%>
<!DOCTYPE html>
<html>
<head>
    <title>Edit Customer</title>
    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
</head>
<body>
<h1>Edit Customer</h1>
<form method="post" action="customers">
    <input type="hidden" name="uuid" value="<%= customer.getUuid() %>">
    <label for="firstName">First Name:</label><br>
    <input type="text" id="firstName" name="firstName" value="<%= customer.getFirstName() %>"><br>
    <label for="lastName">Last Name:</label><br>
    <input type="text" id="lastName" name="lastName" value="<%= customer.getLastName() %>"><br>
    <label for="street">Street:</label><br>
    <input type="text" id="street" name="street" value="<%= customer.getStreet() %>"><br>
    <label for="address">Address:</label><br>
    <input type="text" id="address" name="address" value="<%= customer.getAddress() %>"><br>
    <label for="city">City:</label><br>
    <input type="text" id="city" name="city" value="<%= customer.getCity() %>"><br>
    <label for="state">State:</label><br>
    <input type="text" id="state" name="state" value="<%= customer.getState() %>"><br>
    <label for="email">Email:</label><br>
    <input type="text" id="email" name="email" value="<%= customer.getEmail() %>"><br>
    <label for="phone">Phone:</label><br>
    <input type="text" id="phone" name="phone" value="<%= customer.getPhone() %>"><br>
    <button type="submit" name="action" class="btn btn-success"  value="update">Update Customer</button>
</form>
<a href="customers" class="btn btn-secondary">Back to Customer List</a>
<!-- Bootstrap JS -->
<script src="${pageContext.request.contextPath}/js/bootstrap.bundle.min.js"></script>
</body>
</html>
