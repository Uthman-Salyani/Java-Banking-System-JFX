package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import models.Customer;

//inserts a new customer in the customer table 
public class CustomerDAO {
    public boolean createCustomer(int userId, String firstName, String lastName, String email, String phone, String address, String dateOfBirth) throws SQLException {
        String query = "INSERT INTO customers (user_id, first_name, last_name, email, phone, address, date_of_birth) VALUES (?, ?, ?, ?, ?, ?, ?)";
    
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
        
            stmt.setInt(1, userId);
            stmt.setString(2, firstName);
            stmt.setString(3, lastName);
            stmt.setString(4, email);
            stmt.setString(5, phone);
            stmt.setString(6, address);
            stmt.setString(7, dateOfBirth);
        
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    //converts user_id to customer_id
    public int getCustomerIdByUserId(int userId) throws SQLException {
    String query = "SELECT customer_id FROM customers WHERE user_id = ?";
    
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {
        
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
        
            if (rs.next()) {
                return rs.getInt("customer_id");
            }
            return -1;  // Return -1 if not found
        }
    }

    //gets all the information on a customer using their customer_id 
    public ResultSet getCustomerDetails(int customerId) throws SQLException {
        String query = "SELECT * FROM customers WHERE customer_id = ?";
    
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query);
    
        stmt.setInt(1, customerId);
        return stmt.executeQuery();
    }

    //function for updating customer email address
    public boolean updateCustomerEmail(int customerId, String newEmail) throws SQLException {
        String query = "UPDATE customers SET email = ? WHERE customer_id = ?";
    
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
        
            stmt.setString(1, newEmail);
            stmt.setInt(2, customerId);
        
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    //function for updating customer phone number
    public boolean updateCustomerPhone(int customerId, String newPhone) throws SQLException{
        String query = "UPDATE customers SET phone = ? WHERE customer_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, newPhone);
                stmt.setInt(2, customerId);

                int rowsAffected = stmt.executeUpdate();
                return rowsAffected > 0;
            }
    }

    //function to update customer address
    public boolean updateCustomerAddress(int customerId, String newAddress) throws SQLException{
        String query = "UPDATE customers SET address = ? WHERE customer_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, newAddress);
                stmt.setInt(2, customerId);

                int rowsAffected = stmt.executeUpdate();
                return rowsAffected > 0;
            }
    }


}
