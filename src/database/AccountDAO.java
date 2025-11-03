package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountDAO {
    
    // Create a new account
    public boolean createAccount(int customerId, String accountNumber, String accountType, double initialBalance) throws SQLException {
        String query = "INSERT INTO accounts (customer_id, account_number, account_type, balance) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, customerId);
            stmt.setString(2, accountNumber);
            stmt.setString(3, accountType);
            stmt.setDouble(4, initialBalance);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
    
    // Get account details by account_id
    public ResultSet getAccountById(int accountId) throws SQLException {
        String query = "SELECT * FROM accounts WHERE account_id = ?";
        
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query);
        
        stmt.setInt(1, accountId);
        return stmt.executeQuery();
    }
    
    // Get all accounts for a customer
    public ResultSet getAccountsByCustomerId(int customerId) throws SQLException {
        String query = "SELECT * FROM accounts WHERE customer_id = ?";
        
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query);
        
        stmt.setInt(1, customerId);
        return stmt.executeQuery();
    }
    
    // Update account balance
    public boolean updateBalance(int accountId, double newBalance) throws SQLException {
        String query = "UPDATE accounts SET balance = ? WHERE account_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setDouble(1, newBalance);
            stmt.setInt(2, accountId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
    
    // Get current balance
    public double getBalance(int accountId) throws SQLException {
        String query = "SELECT balance FROM accounts WHERE account_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, accountId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getDouble("balance");
            }
            return -1;
        }
    }
    
    // Get account_id by account number
    public int getAccountIdByAccountNumber(String accountNumber) throws SQLException {
        String query = "SELECT account_id FROM accounts WHERE account_number = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, accountNumber);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("account_id");
            }
            return -1;
        }
    }
    
    // Update account status
    public boolean updateAccountStatus(int accountId, String status) throws SQLException {
        String query = "UPDATE accounts SET status = ? WHERE account_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, status);
            stmt.setInt(2, accountId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
}