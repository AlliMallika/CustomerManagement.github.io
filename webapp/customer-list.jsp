<%@ page import="java.util.List" %>
<%@ page import="com.example.model.Customer" %>
<%
    List<Customer> customers = (List<Customer>) request.getAttribute("customers");
    Integer currentPage = (Integer) request.getAttribute("currentPage");
    Integer totalPages = (Integer) request.getAttribute("totalPages");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Customer List</title>
    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap-icons.css">
</head>
<body>
<h1 class="text-center">Customer List</h1>
<div class="container">
    <div class="d-flex justify-content-between mb-3 align-items-center">
        <div class="d-flex">
            <a href="addCustomer.jsp" class="btn btn-primary mr-2">Add Customer</a>
            <form method="post" action="customers" class="d-inline-block">
                <button type="submit" name="action" value="sync" class="btn btn-secondary">Sync</button>
            </form>
        </div>
        <form method="get" action="customers" class="form-inline d-flex align-items-center">
    <div class="form-group d-flex align-items-center mr-2">
        <label for="searchField" class="mr-2">Search By:</label>
        <select name="searchField" id="searchField" class="form-control mr-2">
            <option value="first_name">First Name</option>
            <option value="last_name">Last Name</option>
            <option value="street">Street</option>
            <option value="city">City</option>
            <option value="address">Address</option>
            <option value="state">State</option>
            <option value="email">Email</option>
            <option value="phone">Phone</option>
        </select>
    </div>
    <div class="form-group d-flex align-items-center mr-2">
        <input type="text" name="searchQuery" class="form-control mr-2" placeholder="Search...">
    </div>
    <button type="submit" class="btn btn-primary mr-2">Search</button>
    <div class="form-group d-flex align-items-center mr-2">
        <label for="sortField" class="mr-2">Sort By:</label>
        <select name="sortField" id="sortField" class="form-control mr-2" onchange="this.form.submit()">
            <option value="first_name">First Name</option>
            <option value="last_name">Last Name</option>
            <option value="street">Street</option>
            <option value="city">City</option>
            <option value="address">Address</option>
            <option value="state">State</option>
            <option value="email">Email</option>
            <option value="phone">Phone</option>
        </select>
    </div>
    <!-- Hidden inputs to preserve search parameters during sorting -->
    <input type="hidden" name="searchField" value="${param.searchField}">
    <input type="hidden" name="searchQuery" value="${param.searchQuery}">
</form>

    </div>
    <table class="table table-bordered">
        <thead>
            <tr>
               
                <th>First Name</th>
                <th>Last Name</th>
                <th>Street</th>
                <th>Address</th>
                <th>City</th>
                <th>State</th>
                <th>Email</th>
                <th>Phone</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
            <%
                if (customers != null) {
                    for (Customer customer : customers) {
            %>
            <tr>
                
                <td><%= customer.getFirstName() %></td>
                <td><%= customer.getLastName() %></td>
                <td><%= customer.getStreet() %></td>
                <td><%= customer.getAddress() %></td>
                <td><%= customer.getCity() %></td>
                <td><%= customer.getState() %></td>
                <td><%= customer.getEmail() %></td>
                <td><%= customer.getPhone() %></td>
                <td class="d-flex justify-content-around">
                    <form method="post" action="customers" class="d-inline-block" id="deleteForm<%= customer.getUuid() %>">
                        <input type="hidden" name="uuid" value="<%= customer.getUuid() %>">
                        <input type="hidden" name="action" value="delete">
                        <i class="bi bi-dash-circle-fill text-danger" title="Delete" onclick="document.getElementById('deleteForm<%= customer.getUuid() %>').submit();" style="cursor: pointer;"></i>
                    </form>
                    <form method="get" action="edit-customer.jsp" class="d-inline-block ml-1" id="editForm<%= customer.getUuid() %>">
                        <input type="hidden" name="uuid" value="<%= customer.getUuid() %>">
                        <input type="hidden" name="action" value="edit">
                        <i class="bi bi-pencil-fill" title="Edit" onclick="document.getElementById('editForm<%= customer.getUuid() %>').submit();" style="cursor: pointer;"></i>
                    </form>
                </td>
            </tr>
            <%
                    }
                } else {
            %>
            <tr>
                <td colspan="9" class="text-center">No customers found.</td>
            </tr>
            <%
                }
            %>
        </tbody>
    </table>

    <!-- Pagination controls -->
    <%
        if (totalPages != null && totalPages > 1) {
    %>
    <nav>
        <ul class="pagination justify-content-center">
            <%
                if (currentPage > 1) {
            %>
            <li class="page-item">
                <a class="page-link" href="customers?page=<%= currentPage - 1 %>&pageSize=10">Previous</a>
            </li>
            <%
                }
                for (int i = 1; i <= totalPages; i++) {
            %>
            <li class="page-item <%= (i == currentPage) ? "active" : "" %>">
                <a class="page-link" href="customers?page=<%= i %>&pageSize=10"><%= i %></a>
            </li>
            <%
                }
                if (currentPage < totalPages) {
            %>
            <li class="page-item">
                <a class="page-link" href="customers?page=<%= currentPage + 1 %>&pageSize=10">Next</a>
            </li>
            <%
                }
            %>
        </ul>
    </nav>
    <%
        }
    %>
</div>

<!-- Bootstrap JS -->
<script src="${pageContext.request.contextPath}/js/bootstrap.bundle.min.js"></script>
</body>
</html>
