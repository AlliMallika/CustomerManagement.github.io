package com.example.dao;

import com.example.model.Customer;
import com.example.model.UUIDUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {
    private String jdbcURL = "jdbc:mysql://localhost:3306/customer_management";
    private String jdbcUsername = "root";
    private String jdbcPassword = "root";

    private static final String INSERT_CUSTOMER_SQL = "INSERT INTO customers (uuid, first_name, last_name, street, address, city, state, email, phone) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SELECT_ALL_CUSTOMERS = "SELECT * FROM customers";
    private static final String SELECT_CUSTOMER_BY_UUID = "SELECT * FROM customers WHERE uuid = ?";
    private static final String UPDATE_CUSTOMER_SQL = "UPDATE customers SET first_name = ?, last_name = ?, street = ?, address = ?, city = ?, state = ?, email = ?, phone = ? WHERE uuid = ?";
    private static final String DELETE_CUSTOMER_SQL = "DELETE FROM customers WHERE uuid = ?";

    protected Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public void addCustomer(Customer customer) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_CUSTOMER_SQL)) {
        	 customer.setUuid(UUIDUtil.generateTestUUID());
            preparedStatement.setString(1, customer.getUuid());
            preparedStatement.setString(2, customer.getFirstName());
            preparedStatement.setString(3, customer.getLastName());
            preparedStatement.setString(4, customer.getStreet());
            preparedStatement.setString(5, customer.getAddress());
            preparedStatement.setString(6, customer.getCity());
            preparedStatement.setString(7, customer.getState());
            preparedStatement.setString(8, customer.getEmail());
            preparedStatement.setString(9, customer.getPhone());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // Method to fetch paginated and sorted customers
    public List<Customer> getPaginatedAndSortedCustomers(int page, int pageSize, String sortField) {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customers ORDER BY " + sortField + " LIMIT ? OFFSET ?";
        try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, pageSize);
            statement.setInt(2, (page - 1) * pageSize);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                customers.add(mapRowToCustomer(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }

    // Overloaded method to fetch paginated, searched, and sorted customers
    public List<Customer> getPaginatedAndSortedCustomers(int page, int pageSize, String searchField, String searchQuery, String sortField) {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customers WHERE " + searchField + " LIKE ? ORDER BY " + sortField + " LIMIT ? OFFSET ?";
        try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, "%" + searchQuery + "%");
            statement.setInt(2, pageSize);
            statement.setInt(3, (page - 1) * pageSize);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                customers.add(mapRowToCustomer(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }

    public int getTotalCustomers(String searchField, String searchQuery) {
        int total = 0;
        String sql = "SELECT COUNT(*) FROM customers WHERE " + searchField + " LIKE ?";
        try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, "%" + searchQuery + "%");
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                total = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }
    
    
    

    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_CUSTOMERS)) {
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                String uuid = rs.getString("uuid");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String street = rs.getString("street");
                String address = rs.getString("address");
                String city = rs.getString("city");
                String state = rs.getString("state");
                String email = rs.getString("email");
                String phone = rs.getString("phone");
                customers.add(new Customer(uuid, firstName, lastName, street, address, city, state, email, phone));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }

    public Customer getCustomerById(String uuid) {
        Customer customer = null;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_CUSTOMER_BY_UUID)) {
            preparedStatement.setString(1, uuid);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String street = rs.getString("street");
                String address = rs.getString("address");
                String city = rs.getString("city");
                String state = rs.getString("state");
                String email = rs.getString("email");
                String phone = rs.getString("phone");
                customer = new Customer(uuid, firstName, lastName, street, address, city, state, email, phone);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customer;
    }

    public boolean customerExists(String uuid) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_CUSTOMER_BY_UUID)) {
            preparedStatement.setString(1, uuid);
            ResultSet rs = preparedStatement.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void updateCustomer(Customer customer) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_CUSTOMER_SQL)) {
            preparedStatement.setString(1, customer.getFirstName());
            preparedStatement.setString(2, customer.getLastName());
            preparedStatement.setString(3, customer.getStreet());
            preparedStatement.setString(4, customer.getAddress());
            preparedStatement.setString(5, customer.getCity());
            preparedStatement.setString(6, customer.getState());
            preparedStatement.setString(7, customer.getEmail());
            preparedStatement.setString(8, customer.getPhone());
            preparedStatement.setString(9, customer.getUuid());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteCustomer(String uuid) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_CUSTOMER_SQL)) {
            preparedStatement.setString(1, uuid);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private Customer mapRowToCustomer(ResultSet resultSet) throws SQLException {
        return new Customer(
            resultSet.getString("uuid"),
            resultSet.getString("first_name"),
            resultSet.getString("last_name"),
            resultSet.getString("street"),
            resultSet.getString("address"),
            resultSet.getString("city"),
            resultSet.getString("state"),
            resultSet.getString("email"),
            resultSet.getString("phone")
        );
    }
    
    
    
    public List<Customer> getPaginatedCustomers(int page, int pageSize, String searchField, String searchQuery, String sortField) {
        List<Customer> customers = new ArrayList<>();
        int start = (page - 1) * pageSize;
        String query = "SELECT * FROM customers WHERE " + searchField + " LIKE ? ORDER BY " + sortField + " LIMIT ?, ?";
        
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, "%" + searchQuery + "%");
            preparedStatement.setInt(2, start);
            preparedStatement.setInt(3, pageSize);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String uuid = resultSet.getString("uuid");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String street = resultSet.getString("street");
                String address = resultSet.getString("address");
                String city = resultSet.getString("city");
                String state = resultSet.getString("state");
                String email = resultSet.getString("email");
                String phone = resultSet.getString("phone");

                customers.add(new Customer(uuid, firstName, lastName, street, address, city, state, email, phone));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }

    public int getCustomerCount(String searchField, String searchQuery) {
        int count = 0;
        String query = "SELECT COUNT(*) FROM customers WHERE " + searchField + " LIKE ?";
        
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, "%" + searchQuery + "%");
            ResultSet resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }
}