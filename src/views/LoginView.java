package views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import database.UserDAO;
import database.CustomerDAO;
import utils.SessionManager;

public class LoginView {
    
    private Stage stage;
    private UserDAO userDAO;
    private CustomerDAO customerDAO;
    
    public LoginView(Stage stage) {
        this.stage = stage;
        this.userDAO = new UserDAO();
        this.customerDAO = new CustomerDAO();
    }
    
    public Scene createLoginScene() {
        // Title
        Label titleLabel = new Label("Nix - My Banking System");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        
        Label subtitleLabel = new Label("Login to your account");
        subtitleLabel.setFont(Font.font("Arial", 14));
        
        // Username field
        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter username");
        usernameField.setMaxWidth(300);
        
        // Password field
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter password");
        passwordField.setMaxWidth(300);
        
        // Error label (hidden by default)
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");
        errorLabel.setVisible(false);
        
        // Login button
        Button loginButton = new Button("Login");
        loginButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10 30;");
        loginButton.setMaxWidth(300);
        
        // Login button action
        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            
            if (username.isEmpty() || password.isEmpty()) {
                errorLabel.setText("Please enter both username and password");
                errorLabel.setVisible(true);
                return;
            }
            
            try {
                // Validate login
                boolean isValid = userDAO.validateLogin(username, password);
                
                if (isValid) {
                    // Get user details
                    int userId = userDAO.getUserId(username);
                    String role = userDAO.getUserRole(username);
                    
                    // Start session
                    SessionManager.getInstance().startSession(userId, username, role);
                    
                    // Navigate based on role
                    if (role.equals("admin")) {
                        openAdminDashboard();
                    } else if (role.equals("customer")) {
                        // Get customer_id
                        int customerId = customerDAO.getCustomerIdByUserId(userId);
                        SessionManager.getInstance().setCustomerId(customerId);
                        openCustomerDashboard();
                    }
                } else {
                    errorLabel.setText("Invalid username or password");
                    errorLabel.setVisible(true);
                }
            } catch (Exception ex) {
                errorLabel.setText("Error: " + ex.getMessage());
                errorLabel.setVisible(true);
                ex.printStackTrace();
            }
        });
        
        // Layout
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(40));
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(
            titleLabel,
            subtitleLabel,
            usernameLabel,
            usernameField,
            passwordLabel,
            passwordField,
            errorLabel,
            loginButton
        );
        
        return new Scene(layout, 500, 450);
    }
    
    private void openAdminDashboard() {
        AdminDashboard adminDashboard = new AdminDashboard(stage);
        stage.setScene(adminDashboard.createAdminScene());
    }
    
    private void openCustomerDashboard() {
        CustomerDashboard customerDashboard = new CustomerDashboard(stage);
        stage.setScene(customerDashboard.createCustomerScene());
    }
}