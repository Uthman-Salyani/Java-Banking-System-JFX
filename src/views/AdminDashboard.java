package views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import utils.SessionManager;
import database.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AdminDashboard {
    
    private Stage stage;
    private UserDAO userDAO;
    private CustomerDAO customerDAO;
    private AccountDAO accountDAO;
    private LoanDAO loanDAO;
    private OverdraftDAO overdraftDAO;
    
    public AdminDashboard(Stage stage) {
        this.stage = stage;
        this.userDAO = new UserDAO();
        this.customerDAO = new CustomerDAO();
        this.accountDAO = new AccountDAO();
        this.loanDAO = new LoanDAO();
        this.overdraftDAO = new OverdraftDAO();
    }
    
    public Scene createAdminScene() {
        // Top bar with welcome and logout
        Label welcomeLabel = new Label("Welcome, Admin: " + SessionManager.getInstance().getUsername());
        welcomeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        
        Button logoutButton = new Button("Logout");
        logoutButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
        logoutButton.setOnAction(e -> logout());
        
        HBox topBar = new HBox(20);
        topBar.setPadding(new Insets(10));
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.getChildren().addAll(welcomeLabel, logoutButton);
        HBox.setHgrow(welcomeLabel, Priority.ALWAYS);
        topBar.setStyle("-fx-background-color: #2196F3;");
        
        // Menu buttons
        Button createCustomerBtn = new Button("Create Customer");
        createCustomerBtn.setPrefSize(200, 50);
        createCustomerBtn.setOnAction(e -> showCreateCustomerForm());
        
        Button viewCustomersBtn = new Button("View All Customers");
        viewCustomersBtn.setPrefSize(200, 50);
        viewCustomersBtn.setOnAction(e -> showAllCustomers());
        
        Button approveLoansBtn = new Button("Approve Loans");
        approveLoansBtn.setPrefSize(200, 50);
        approveLoansBtn.setOnAction(e -> showPendingLoans());
        
        Button approveOverdraftsBtn = new Button("Approve Overdrafts");
        approveOverdraftsBtn.setPrefSize(200, 50);
        approveOverdraftsBtn.setOnAction(e -> showPendingOverdrafts());
        
        Button changePasswordBtn = new Button("Change Password");
        changePasswordBtn.setPrefSize(200, 50);
        changePasswordBtn.setOnAction(e -> showChangePasswordForm());
        
        // Layout for buttons
        VBox menu = new VBox(15);
        menu.setPadding(new Insets(30));
        menu.setAlignment(Pos.CENTER);
        menu.getChildren().addAll(
            createCustomerBtn,
            viewCustomersBtn,
            approveLoansBtn,
            approveOverdraftsBtn,
            changePasswordBtn
        );
        
        // Main layout
        BorderPane mainLayout = new BorderPane();
        mainLayout.setTop(topBar);
        mainLayout.setCenter(menu);
        
        return new Scene(mainLayout, 800, 600);
    }
    
    private void logout() {
        SessionManager.getInstance().endSession();
        LoginView loginView = new LoginView(stage);
        stage.setScene(loginView.createLoginScene());
    }
    
    private void showCreateCustomerForm() {
     // Create a dialog
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Create New Customer");
        dialog.setHeaderText("Enter customer details");
    
        // Create form fields
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
    
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
    
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
    
        TextField firstNameField = new TextField();
        firstNameField.setPromptText("First Name");
    
        TextField lastNameField = new TextField();
        lastNameField.setPromptText("Last Name");
    
        TextField emailField = new TextField();
        emailField.setPromptText("Email");
    
        TextField phoneField = new TextField();
        phoneField.setPromptText("Phone");
    
        TextField addressField = new TextField();
        addressField.setPromptText("Address");
    
        TextField dobField = new TextField();
        dobField.setPromptText("Date of Birth (YYYY-MM-DD)");
    
        ComboBox<String> accountTypeBox = new ComboBox<>();
        accountTypeBox.getItems().addAll("savings", "current");
        accountTypeBox.setPromptText("Account Type");
    
        TextField initialBalanceField = new TextField();
        initialBalanceField.setPromptText("Initial Balance");
    
        // Add to grid
        grid.add(new Label("Username:"), 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(passwordField, 1, 1);
        grid.add(new Label("First Name:"), 0, 2);
        grid.add(firstNameField, 1, 2);
        grid.add(new Label("Last Name:"), 0, 3);
        grid.add(lastNameField, 1, 3);
        grid.add(new Label("Email:"), 0, 4);
        grid.add(emailField, 1, 4);
        grid.add(new Label("Phone:"), 0, 5);
        grid.add(phoneField, 1, 5);
        grid.add(new Label("Address:"), 0, 6);
        grid.add(addressField, 1, 6);
        grid.add(new Label("Date of Birth:"), 0, 7);
        grid.add(dobField, 1, 7);
        grid.add(new Label("Account Type:"), 0, 8);
        grid.add(accountTypeBox, 1, 8);
        grid.add(new Label("Initial Balance:"), 0, 9);
        grid.add(initialBalanceField, 1, 9);
    
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
    
        // Handle the response
        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    // Get values
                    String username = usernameField.getText();
                    String password = passwordField.getText();
                    String firstName = firstNameField.getText();
                    String lastName = lastNameField.getText();
                    String email = emailField.getText();
                    String phone = phoneField.getText();
                    String address = addressField.getText();
                    String dob = dobField.getText();
                    String accountType = accountTypeBox.getValue();
                    double initialBalance = Double.parseDouble(initialBalanceField.getText());
                
                    // Validate
                    if (username.isEmpty() || password.isEmpty() || firstName.isEmpty() || 
                        lastName.isEmpty() || accountType == null) {
                        showError("Please fill all required fields!");
                        return;
                    }
                
                    // Check if username exists
                    if (userDAO.usernameExists(username)) {
                        showError("Username already exists!");
                        return;
                    }
                
                    // Create user
                    boolean userCreated = userDAO.createUser(username, password, "customer");
                    if (!userCreated) {
                        showError("Failed to create user!");
                        return;
                    }
                
                    // Get the user_id
                    int userId = userDAO.getUserId(username);
                
                    // Create customer
                    boolean customerCreated = customerDAO.createCustomer(
                        userId, firstName, lastName, email, phone, address, dob
                    );
                
                    if (!customerCreated) {
                        showError("Failed to create customer!");
                        return;
                    }
                
                    // Get customer_id
                    int customerId = customerDAO.getCustomerIdByUserId(userId);
                
                    // Generate account number
                    String accountNumber = "ACC" + System.currentTimeMillis();
                
                    // Create account
                    boolean accountCreated = accountDAO.createAccount(
                        customerId, accountNumber, accountType, initialBalance
                    );
                    
                    if (accountCreated) {
                        showSuccess("Customer created successfully!\nAccount Number: " + accountNumber);
                    } else {
                        showError("Failed to create account!");
                    }
                
                } catch (NumberFormatException e) {
                    showError("Invalid number format!");
                } catch (Exception e) {
                    showError("Error: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

private void showError(String message) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Error");
    alert.setContentText(message);
    alert.showAndWait();
}

private void showSuccess(String message) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Success");
    alert.setContentText(message);
    alert.showAndWait();
}
    
    private void showAllCustomers() {
        try {
            // Get all customers with their user info
            String query = "SELECT c.customer_id, c.first_name, c.last_name, c.email, c.phone, " +
                        "u.username, u.created_date " +
                        "FROM customers c " +
                        "JOIN users u ON c.user_id = u.user_id " +
                        "ORDER BY c.customer_id DESC";
            
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            
            StringBuilder customerList = new StringBuilder();
            customerList.append("ALL CUSTOMERS\n");
            customerList.append("=".repeat(80)).append("\n\n");
            
            boolean hasCustomers = false;
            while (rs.next()) {
                hasCustomers = true;
                int customerId = rs.getInt("customer_id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String email = rs.getString("email");
                String phone = rs.getString("phone");
                String username = rs.getString("username");
                String createdDate = rs.getString("created_date");
                
                customerList.append("Customer ID: ").append(customerId).append("\n");
                customerList.append("Name: ").append(firstName).append(" ").append(lastName).append("\n");
                customerList.append("Username: ").append(username).append("\n");
                customerList.append("Email: ").append(email).append("\n");
                customerList.append("Phone: ").append(phone != null ? phone : "N/A").append("\n");
                customerList.append("Created: ").append(createdDate).append("\n");
                customerList.append("-".repeat(80)).append("\n\n");
            }
            
            rs.close();
            stmt.close();
            conn.close();
            
            if (!hasCustomers) {
                customerList.append("No customers found.");
            }
            
            // Show in scrollable dialog
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("All Customers");
            alert.setHeaderText("Customer List");
            
            TextArea textArea = new TextArea(customerList.toString());
            textArea.setEditable(false);
            textArea.setWrapText(true);
            textArea.setPrefSize(700, 500);
            
            alert.getDialogPane().setContent(textArea);
            alert.showAndWait();
            
        } catch (Exception e) {
            showError("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void showPendingLoans() {
        try {
            ResultSet rs = loanDAO.getPendingLoans();
            
            java.util.List<LoanInfo> pendingLoans = new java.util.ArrayList<>();
            
            while (rs.next()) {
                LoanInfo loan = new LoanInfo();
                loan.loanId = rs.getInt("loan_id");
                loan.customerId = rs.getInt("customer_id");
                loan.loanAmount = rs.getDouble("loan_amount");
                loan.interestRate = rs.getDouble("interest_rate");
                loan.durationMonths = rs.getInt("duration_months");
                loan.applicationDate = rs.getString("application_date");
                pendingLoans.add(loan);
            }
            rs.close();
            
            if (pendingLoans.isEmpty()) {
                showSuccess("No pending loan applications.");
                return;
            }
            
            // Show each loan for approval
            for (LoanInfo loan : pendingLoans) {
                // Get customer name
                String customerName = getCustomerName(loan.customerId);
                
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Loan Approval");
                alert.setHeaderText("Review Loan Application");
                
                String loanDetails = "Customer: " + customerName + "\n" +
                                "Loan Amount: $" + String.format("%.2f", loan.loanAmount) + "\n" +
                                "Interest Rate: " + loan.interestRate + "%\n" +
                                "Duration: " + loan.durationMonths + " months\n" +
                                "Application Date: " + loan.applicationDate + "\n\n" +
                                "Do you want to APPROVE this loan?";
                
                alert.setContentText(loanDetails);
                
                ButtonType approveButton = new ButtonType("Approve");
                ButtonType declineButton = new ButtonType("Decline");
                ButtonType skipButton = new ButtonType("Skip", ButtonBar.ButtonData.CANCEL_CLOSE);
                
                alert.getButtonTypes().setAll(approveButton, declineButton, skipButton);
                
                alert.showAndWait().ifPresent(response -> {
                    try {
                        if (response == approveButton) {
                            loanDAO.updateLoanStatus(loan.loanId, "approved");
                            showSuccess("Loan approved!");
                        } else if (response == declineButton) {
                            loanDAO.updateLoanStatus(loan.loanId, "declined");
                            showSuccess("Loan declined.");
                        }
                    } catch (Exception e) {
                        showError("Error: " + e.getMessage());
                    }
                });
            }
            
            showSuccess("Finished reviewing all pending loans.");
            
        } catch (Exception e) {
            showError("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Helper class
    private class LoanInfo {
        int loanId;
        int customerId;
        double loanAmount;
        double interestRate;
        int durationMonths;
        String applicationDate;
    }

    // Helper method
    private String getCustomerName(int customerId) {
        try {
            ResultSet rs = customerDAO.getCustomerDetails(customerId);
            if (rs.next()) {
                String name = rs.getString("first_name") + " " + rs.getString("last_name");
                rs.close();
                return name;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Unknown";
    }
    
    private void showPendingOverdrafts() {
        try {
            ResultSet rs = overdraftDAO.getPendingOverdrafts();
            
            java.util.List<OverdraftInfo> pendingOverdrafts = new java.util.ArrayList<>();
            
            while (rs.next()) {
                OverdraftInfo overdraft = new OverdraftInfo();
                overdraft.overdraftId = rs.getInt("overdraft_id");
                overdraft.accountId = rs.getInt("account_id");
                overdraft.overdraftLimit = rs.getDouble("overdraft_limit");
                overdraft.requestDate = rs.getString("request_date");
                pendingOverdrafts.add(overdraft);
            }
            rs.close();
            
            if (pendingOverdrafts.isEmpty()) {
                showSuccess("No pending overdraft requests.");
                return;
            }
            
            // Show each overdraft for approval
            for (OverdraftInfo overdraft : pendingOverdrafts) {
                // Get account and customer info
                String accountInfo = getAccountInfo(overdraft.accountId);
                
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Overdraft Approval");
                alert.setHeaderText("Review Overdraft Request");
                
                String overdraftDetails = accountInfo + "\n" +
                                        "Requested Overdraft Limit: $" + String.format("%.2f", overdraft.overdraftLimit) + "\n" +
                                        "Request Date: " + overdraft.requestDate + "\n\n" +
                                        "Do you want to APPROVE this overdraft?";
                
                alert.setContentText(overdraftDetails);
                
                ButtonType approveButton = new ButtonType("Approve");
                ButtonType declineButton = new ButtonType("Decline");
                ButtonType skipButton = new ButtonType("Skip", ButtonBar.ButtonData.CANCEL_CLOSE);
                
                alert.getButtonTypes().setAll(approveButton, declineButton, skipButton);
                
                alert.showAndWait().ifPresent(response -> {
                    try {
                        if (response == approveButton) {
                            overdraftDAO.updateOverdraftStatus(overdraft.overdraftId, "approved");
                            showSuccess("Overdraft approved!");
                        } else if (response == declineButton) {
                            overdraftDAO.updateOverdraftStatus(overdraft.overdraftId, "declined");
                            showSuccess("Overdraft declined.");
                        }
                    } catch (Exception e) {
                        showError("Error: " + e.getMessage());
                    }
                });
            }
            
            showSuccess("Finished reviewing all pending overdrafts.");
            
        } catch (Exception e) {
            showError("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Helper class
    private class OverdraftInfo {
        int overdraftId;
        int accountId;
        double overdraftLimit;
        String requestDate;
    }

    // Helper method
    private String getAccountInfo(int accountId) {
        try {
            ResultSet accountRs = accountDAO.getAccountById(accountId);
            if (accountRs.next()) {
                String accountNumber = accountRs.getString("account_number");
                String accountType = accountRs.getString("account_type");
                int customerId = accountRs.getInt("customer_id");
                accountRs.close();
                
                String customerName = getCustomerName(customerId);
                
                return "Customer: " + customerName + "\n" +
                    "Account: " + accountNumber + " (" + accountType + ")";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Unknown Account";
    }
    
    private void showChangePasswordForm() {
        // Create dialog
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Change Password");
        dialog.setHeaderText("Enter your current and new password");
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        
        PasswordField currentPasswordField = new PasswordField();
        currentPasswordField.setPromptText("Current Password");
        
        PasswordField newPasswordField = new PasswordField();
        newPasswordField.setPromptText("New Password");
        
        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm New Password");
        
        grid.add(new Label("Current Password:"), 0, 0);
        grid.add(currentPasswordField, 1, 0);
        grid.add(new Label("New Password:"), 0, 1);
        grid.add(newPasswordField, 1, 1);
        grid.add(new Label("Confirm Password:"), 0, 2);
        grid.add(confirmPasswordField, 1, 2);
        
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    String currentPassword = currentPasswordField.getText();
                    String newPassword = newPasswordField.getText();
                    String confirmPassword = confirmPasswordField.getText();
                    
                    // Validation
                    if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                        showError("All fields are required!");
                        return;
                    }
                    
                    // Check if new passwords match
                    if (!newPassword.equals(confirmPassword)) {
                        showError("New passwords do not match!");
                        return;
                    }
                    
                    // Check password strength
                    if (newPassword.length() < 8) {
                        showError("New password must be at least 8 characters long!");
                        return;
                    }
                    
                    // Verify current password
                    String username = SessionManager.getInstance().getUsername();
                    boolean isValid = userDAO.validateLogin(username, currentPassword);
                    
                    if (!isValid) {
                        showError("Current password is incorrect!");
                        return;
                    }
                    
                    // Update password
                    boolean updated = userDAO.updatePassword(username, newPassword);
                    
                    if (updated) {
                        showSuccess("Password changed successfully!\n\nPlease login again with your new password.");
                        
                        // Logout user
                        SessionManager.getInstance().endSession();
                        LoginView loginView = new LoginView(stage);
                        stage.setScene(loginView.createLoginScene());
                    } else {
                        showError("Failed to update password!");
                    }
                    
                } catch (Exception e) {
                    showError("Error: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }
}