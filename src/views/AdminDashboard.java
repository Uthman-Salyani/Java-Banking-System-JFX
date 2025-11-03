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
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("View Customers");
        alert.setHeaderText("Feature Coming Soon");
        alert.setContentText("Customer list will be displayed here.");
        alert.showAndWait();
    }
    
    private void showPendingLoans() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Pending Loans");
        alert.setHeaderText("Feature Coming Soon");
        alert.setContentText("Pending loan applications will be shown here.");
        alert.showAndWait();
    }
    
    private void showPendingOverdrafts() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Pending Overdrafts");
        alert.setHeaderText("Feature Coming Soon");
        alert.setContentText("Pending overdraft requests will be shown here.");
        alert.showAndWait();
    }
    
    private void showChangePasswordForm() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Change Password");
        alert.setHeaderText("Feature Coming Soon");
        alert.setContentText("Password change form will be implemented here.");
        alert.showAndWait();
    }
}