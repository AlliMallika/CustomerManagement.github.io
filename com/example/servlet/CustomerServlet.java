package com.example.servlet;

import com.example.dao.CustomerDAO;
import com.example.model.Customer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

@WebServlet("/customers")
public class CustomerServlet extends HttpServlet {
    private CustomerDAO customerDAO;

    public void init() {
        customerDAO = new CustomerDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String token = (String) session.getAttribute("authToken");

        if (token != null) {
            // Fetch customers from the external service
            List<Customer> customers = fetchCustomersFromExternalService(token);

            // Store the customer data in the database
            for (Customer customer : customers) {
                customerDAO.addCustomer(customer);
            }

            // Pagination parameters
            int page = 1;
            int pageSize = 10; // Default page size
            if (request.getParameter("page") != null) {
                page = Integer.parseInt(request.getParameter("page"));
            }
            if (request.getParameter("pageSize") != null) {
                pageSize = Integer.parseInt(request.getParameter("pageSize"));
            }

            // Search and sort parameters
            String searchField = request.getParameter("searchField");
            String searchQuery = request.getParameter("searchQuery");
            String sortField = request.getParameter("sortField");

            if (searchField == null || searchField.isEmpty()) {
                searchField = "first_name"; // Default search field
            }
            if (sortField == null || sortField.isEmpty()) {
                sortField = "first_name"; // Default sort field
            }
            if (searchQuery == null) {
                searchQuery = ""; // Default empty search query
            }

            // Validate searchField and sortField
            List<String> validFields = List.of("first_name", "last_name", "street", "address", "city", "state", "email", "phone");
            if (!validFields.contains(searchField)) {
                searchField = "first_name";
            }
            if (!validFields.contains(sortField)) {
                sortField = "first_name";
            }

            // Fetch paginated, searched, and sorted customers from the database
            List<Customer> paginatedCustomers;
            if (searchQuery.isEmpty()) {
                // Only sorting
                paginatedCustomers = customerDAO.getPaginatedAndSortedCustomers(page, pageSize, sortField);
            } else {
                // Search and sort
                paginatedCustomers = customerDAO.getPaginatedAndSortedCustomers(page, pageSize, searchField, searchQuery, sortField);
            }

            // Set attributes and forward to JSP
            request.setAttribute("customers", paginatedCustomers);

            // Calculate total pages and set attributes for pagination
            int totalCustomers = customerDAO.getTotalCustomers(searchField, searchQuery);
            int totalPages = (int) Math.ceil((double) totalCustomers / pageSize);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("currentPage", page);

            RequestDispatcher dispatcher = request.getRequestDispatcher("customer-list.jsp");
            dispatcher.forward(request, response);
        } else {
            // Handle the case where the token is not present
            response.sendRedirect("login.jsp?error=Session expired. Please log in again.");
        }
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	String action = request.getParameter("action");

        if ("sync".equals(action)) {
            HttpSession session = request.getSession();
            String token = (String) session.getAttribute("authToken");

            if (token != null) {
                syncCustomers(token);
                response.sendRedirect("customers");
            } else {
                response.sendRedirect("login.jsp?error=Session expired. Please log in again.");
            }
        } else if ("delete".equals(action)) {
            String uuid = request.getParameter("uuid");
            customerDAO.deleteCustomer(uuid);
            response.sendRedirect("customers");
        } else {
            // Handle other actions if needed
            String uuid = UUID.randomUUID().toString();
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String street = request.getParameter("street");
            String address = request.getParameter("address");
            String city = request.getParameter("city");
            String state = request.getParameter("state");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");

            Customer customer = new Customer(uuid, firstName, lastName, street, address, city, state, email, phone);
            if ("update".equals(action)) {
                customer.setUuid(request.getParameter("uuid"));
                customerDAO.updateCustomer(customer);
            } else {
                customerDAO.addCustomer(customer);
            }
            response.sendRedirect("customers");
        }
    }
    
 
    private List<Customer> fetchCustomersFromExternalService(String token) throws IOException {
        List<Customer> customers = new ArrayList<>();
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet getRequest = new HttpGet("https://qa.sunbasedata.com/sunbase/portal/api/assignment.jsp?cmd=get_customer_list");
            getRequest.setHeader("Authorization", "Bearer " + token);

            try (CloseableHttpResponse response = httpClient.execute(getRequest)) {
                int statusCode = response.getStatusLine().getStatusCode();
                System.out.println("Response Code: " + statusCode);

                if (statusCode == 200) {
                    String jsonResponse = EntityUtils.toString(response.getEntity());
                    System.out.println("Full JSON Response: " + jsonResponse); // Log the JSON response

                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode rootNode = mapper.readTree(jsonResponse);

                    for (JsonNode customerNode : rootNode) {
                        // Extract customer fields and add to list
                        String uuid = customerNode.get("uuid").asText();
                        String firstName = customerNode.get("first_name").asText();
                        String lastName = customerNode.get("last_name").asText();
                        String street = customerNode.get("street").asText();
                        String address = customerNode.get("address").asText();
                        String city = customerNode.get("city").asText();
                        String state = customerNode.get("state").asText();
                        String email = customerNode.get("email").asText();
                        String phone = customerNode.get("phone").asText();

                        Customer customer = new Customer(uuid, firstName, lastName, street, address, city, state, email, phone);
                        customers.add(customer);
                    }
                } else {
                    System.out.println("Error fetching customers: " + response.getStatusLine().getReasonPhrase());
                }
            }
        }
        return customers;
    }


    private void syncCustomers(String token) throws IOException {
        // Fetch customers from the external service
        List<Customer> customers = fetchCustomersFromExternalService(token);

        // Store the customer data in the database
        for (Customer customer : customers) {
            customerDAO.addCustomer(customer);
        }
    }
}

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

 
 