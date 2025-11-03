package utils;

public class SessionManager {
    
    private static SessionManager instance;
    
    // Current logged-in user details
    private int userId;
    private String username;
    private String role;
    private int customerId;  // Only for customers
    
    // Private constructor (Singleton pattern)
    private SessionManager() {
        this.userId = -1;
        this.username = null;
        this.role = null;
        this.customerId = -1;
    }
    
    // Get the single instance
    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }
    
    // Start a session (login)
    public void startSession(int userId, String username, String role) {
        this.userId = userId;
        this.username = username;
        this.role = role;
    }
    
    // Set customer ID (for customer users)
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
    
    // End session (logout)
    public void endSession() {
        this.userId = -1;
        this.username = null;
        this.role = null;
        this.customerId = -1;
    }
    
    // Check if user is logged in
    public boolean isLoggedIn() {
        return userId != -1 && username != null;
    }
    
    // Getters
    public int getUserId() {
        return userId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public String getRole() {
        return role;
    }
    
    public int getCustomerId() {
        return customerId;
    }
    
    // Check if current user is admin
    public boolean isAdmin() {
        return "admin".equals(role);
    }
    
    // Check if current user is customer
    public boolean isCustomer() {
        return "customer".equals(role);
    }
}