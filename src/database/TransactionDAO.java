package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TransactionDAO {
    
    // Record a new transaction
    public boolean createTransaction(int accountId, String transactionType, double amount, double balanceAfter, String description) throws SQLException {
        String query = "INSERT INTO transactions (account_id, transaction_type, amount, balance_after, description) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, accountId);
            stmt.setString(2, transactionType);
            stmt.setDouble(3, amount);
            stmt.setDouble(4, balanceAfter);
            stmt.setString(5, description);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
    
    // Get all transactions for an account
    public ResultSet getTransactionsByAccountId(int accountId) throws SQLException {
        String query = "SELECT * FROM transactions WHERE account_id = ? ORDER BY transaction_date DESC";
        
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query);
        
        stmt.setInt(1, accountId);
        return stmt.executeQuery();
    }
    
    // Get recent transactions (limit to last N transactions)
    public ResultSet getRecentTransactions(int accountId, int limit) throws SQLException {
        String query = "SELECT * FROM transactions WHERE account_id = ? ORDER BY transaction_date DESC LIMIT ?";
        
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query);
        
        stmt.setInt(1, accountId);
        stmt.setInt(2, limit);
        return stmt.executeQuery();
    }
    
    // Get transaction by ID
    public ResultSet getTransactionById(int transactionId) throws SQLException {
        String query = "SELECT * FROM transactions WHERE transaction_id = ?";
        
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query);
        
        stmt.setInt(1, transactionId);
        return stmt.executeQuery();
    }

    public void deleteTransactionsByAccountId(int accountId) throws SQLException {
        String query = "DELETE FROM transactions WHERE account_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, accountId);
            stmt.executeUpdate();
        }
    }
}