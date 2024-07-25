# Customer Management Application

## Overview
This is a Java-based web application for managing customer data. It uses Servlets and JSP for the web interface and MySQL for the database. The application allows users to add, edit, delete, and view customer details, with functionalities for authentication, pagination, sorting, and searching.

## Project Structure
### `src/com/example/model`
- **Customer.java**: Represents the Customer model.
- **UUIDUtil.java**: Utility class for generating UUIDs.

### `src/com/example/dao`
- **CustomerDAO.java**: Data Access Object (DAO) for performing CRUD operations on the customer data in the database.

### `src/com/example/servlet`
- **AuthServlet.java**: Handles authentication requests.
- **CustomerServlet.java**: Handles customer-related requests such as fetching, adding, updating, and deleting customers.

### `webapp/WEB-INF`
- **web.xml**: Deployment descriptor for the application.

### `webapp`
- **addCustomer.jsp**, **editCustomer.jsp**, **customer-list.jsp**, **login.jsp**: JSP files for various functionalities of the application.

## Database Setup
1. Create a MySQL database named `customer_management`.
2. Create a table named `customers`.
3. Update the database connection details (`jdbcURL`, `jdbcUsername`, `jdbcPassword`) in `CustomerDAO.java`.

## Configuration
- Ensure you have Apache Tomcat and MySQL installed and configured.
- Place the MySQL JDBC driver in the lib folder of your Tomcat installation.

## Running the Application
1. Compile the application and package it into a WAR file.
2. Deploy the WAR file to your Tomcat server.
3. Access the application at `http://localhost:8080/your-app-name/`.

## Usage
### Login
Navigate to `login.jsp` and enter your credentials to authenticate.

### Customer Management
After logging in, you can view the customer list, add new customers, edit existing customers, and delete customers. The customer list supports pagination, sorting, and searching.

## Code Explanation
### Model
- **Customer.java**: Represents a customer with fields like uuid, firstName, lastName, street, address, city, state, email, and phone.
- **UUIDUtil.java**: Generates a UUID for customers.

### DAO
- **CustomerDAO.java**: Handles database operations for customers, including adding, updating, deleting, and fetching customer records. It also provides methods for pagination and sorting.

### Servlet
- **AuthServlet.java**: Authenticates users using an external API.
- **CustomerServlet.java**: Handles HTTP requests related to customer management. It supports operations like fetching customers from an external service, syncing customers to the local database, and performing CRUD operations.

### JSP
- **addCustomer.jsp**: Form for adding a new customer.
- **editCustomer.jsp**: Form for editing an existing customer.
- **customer-list.jsp**: Displays the list of customers with pagination, sorting, and searching functionalities.
- **login.jsp**: Login form for authentication.

## External Service Integration
The application integrates with an external service for authentication and fetching customer data.
- **Authentication**: Sends a POST request to the external service and retrieves an authentication token.
- **Fetching Customers**: Uses the token to fetch customer data from the external service and stores it in the local database.

## Dependencies
- MySQL JDBC Driver
- Jackson Databind (for JSON parsing)
- Apache HttpClient (for making HTTP requests)
