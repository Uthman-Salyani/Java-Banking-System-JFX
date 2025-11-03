package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OverdraftDAO {
    
    // Create a new overdraft request
    public boolean createOverdraftRequest(int accountId, double overdraftLimit) throws SQLException {
        String query = "INSERT INTO overdrafts (account_id, overdraft_limit) VALUES (?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, accountId);
            stmt.setDouble(2, overdraftLimit);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
    
    // Get all overdraft requests for an account
    public ResultSet getOverdraftsByAccountId(int accountId) throws SQLException {
        String query = "SELECT * FROM overdrafts WHERE account_id = ?";
        
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query);
        
        stmt.setInt(1, accountId);
        return stmt.executeQuery();
    }
    
    // Get all pending overdraft requests (for admin)
    public ResultSet getPendingOverdrafts() throws SQLException {
        String query = "SELECT * FROM overdrafts WHERE status = 'pending' ORDER BY request_date DESC";
        
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query);
        
        return stmt.executeQuery();
    }
    
    // Update overdraft status (approve/decline)
    public boolean updateOverdraftStatus(int overdraftId, String status) throws SQLException {
        String query = "UPDATE overdrafts SET status = ?, processed_date = CURRENT_TIMESTAMP WHERE overdraft_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, status);
            stmt.setInt(2, overdraftId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
    
    // Get overdraft by ID
    public ResultSet getOverdraftById(int overdraftId) throws SQLException {
        String query = "SELECT * FROM overdrafts WHERE overdraft_id = ?";
        
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query);
        
        stmt.setInt(1, overdraftId);
        return stmt.executeQuery();
    }
}