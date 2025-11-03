package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import models.User;

public class UserDAO {
    //if the username already exists or not 
    public boolean usernameExists(String username) throws SQLException {
    String query = "SELECT COUNT(*) FROM users WHERE username = ?";
    
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {
        
        stmt.setString(1, username);
        ResultSet rs = stmt.executeQuery();
        
        if (rs.next()) {
            return rs.getInt(1) > 0;
        }
        return false;
    }
}

//checks if the username and password match in the database
    public boolean validateLogin(String username, String password) throws SQLException {
        String query = "SELECT password FROM users WHERE username = ?";
    
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
        
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
        
            if (rs.next()) {
                String storedPassword = rs.getString("password");
                return storedPassword.equals(password);
            }
            return false;
        }
    }   

    //checks the database if the user is a customer or an admin 
    public String getUserRole(String username) throws SQLException {
        String query = "SELECT role FROM users WHERE username = ?";
    
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
        
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
        
            if (rs.next()) {
                return rs.getString("role");
            }
            return null;
        }
    }

    //converts the username to user_id
    public int getUserId(String username) throws SQLException {
        String query = "SELECT user_id FROM users WHERE username = ?";
    
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
        
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
        
            if (rs.next()) {
                return rs.getInt("user_id");
            }
            return -1;  // Return -1 if user not found
        }
    }

    //function to enable admin to create new user
    public boolean createUser(String username, String password, String role) throws SQLException {
        String query = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
    
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
        
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, role);
        
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    //for users to change pssword
    public boolean updatePassword(String username, String newPassword) throws SQLException {
        String query = "UPDATE users SET password = ? WHERE username = ?";
    
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
        
            stmt.setString(1, newPassword);
            stmt.setString(2, username);
        
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
}
