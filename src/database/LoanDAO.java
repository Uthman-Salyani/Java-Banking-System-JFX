package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoanDAO {
    
    // Create a new loan application
    public boolean createLoanApplication(int customerId, double loanAmount, double interestRate, int durationMonths) throws SQLException {
        String query = "INSERT INTO loans (customer_id, loan_amount, interest_rate, duration_months) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, customerId);
            stmt.setDouble(2, loanAmount);
            stmt.setDouble(3, interestRate);
            stmt.setInt(4, durationMonths);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
    
    // Get all loans for a customer
    public ResultSet getLoansByCustomerId(int customerId) throws SQLException {
        String query = "SELECT * FROM loans WHERE customer_id = ?";
        
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query);
        
        stmt.setInt(1, customerId);
        return stmt.executeQuery();
    }
    
    // Get all pending loan applications (for admin)
    public ResultSet getPendingLoans() throws SQLException {
        String query = "SELECT * FROM loans WHERE status = 'pending' ORDER BY application_date DESC";
        
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query);
        
        return stmt.executeQuery();
    }
    
    // Update loan status (approve/decline)
    public boolean updateLoanStatus(int loanId, String status) throws SQLException {
        String query = "UPDATE loans SET status = ?, processed_date = CURRENT_TIMESTAMP WHERE loan_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, status);
            stmt.setInt(2, loanId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
    
    // Get loan by ID
    public ResultSet getLoanById(int loanId) throws SQLException {
        String query = "SELECT * FROM loans WHERE loan_id = ?";
        
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query);
        
        stmt.setInt(1, loanId);
        return stmt.executeQuery();
    }
}