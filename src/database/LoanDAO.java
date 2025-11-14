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
        String updateLoanQuery = "UPDATE loans SET status = ?, processed_date = CURRENT_TIMESTAMP WHERE loan_id = ?";
        Connection conn = null;
        PreparedStatement updateLoanStmt = null;
        PreparedStatement selectLoanStmt = null;
        ResultSet loanRs = null;

        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Start transaction

            // Update loan status
            updateLoanStmt = conn.prepareStatement(updateLoanQuery);
            updateLoanStmt.setString(1, status);
            updateLoanStmt.setInt(2, loanId);
            int rowsAffected = updateLoanStmt.executeUpdate();

            if (rowsAffected > 0 && "approved".equalsIgnoreCase(status)) {
                // Get loan details using the same connection
                selectLoanStmt = conn.prepareStatement("SELECT * FROM loans WHERE loan_id = ?");
                selectLoanStmt.setInt(1, loanId);
                loanRs = selectLoanStmt.executeQuery();

                if (loanRs.next()) {
                    int customerId = loanRs.getInt("customer_id");
                    double loanAmount = loanRs.getDouble("loan_amount");

                    // Get customer's account
                    AccountDAO accountDAO = new AccountDAO();
                    ResultSet accountRs = accountDAO.getAccountsByCustomerId(customerId);

                    if (accountRs.next()) {
                        int accountId = accountRs.getInt("account_id");
                        double currentBalance = accountDAO.getBalance(accountId);
                        double newBalance = currentBalance + loanAmount;

                        // Update account balance
                        accountDAO.updateBalance(accountId, newBalance);
                    }
                }
            }

            conn.commit(); // Commit transaction
            return rowsAffected > 0;

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Rollback transaction on error
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw e;

        } finally {
            if (loanRs != null) {
                loanRs.close();
            }
            if (selectLoanStmt != null) {
                selectLoanStmt.close();
            }
            if (updateLoanStmt != null) {
                updateLoanStmt.close();
            }
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                conn.close();
            }
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

    public void deleteLoansByCustomerId(int customerId) throws SQLException {
        String query = "DELETE FROM loans WHERE customer_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, customerId);
            stmt.executeUpdate();
        }
    }
}