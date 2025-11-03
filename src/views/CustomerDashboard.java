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

import java.sql.ResultSet;

import database.*;

public class CustomerDashboard {
    
    private Stage stage;
    private AccountDAO accountDAO;
    private TransactionDAO transactionDAO;
    private LoanDAO loanDAO;
    private OverdraftDAO overdraftDAO;
    private CustomerDAO customerDAO;
    
    public CustomerDashboard(Stage stage) {
        this.stage = stage;
        this.accountDAO = new AccountDAO();
        this.transactionDAO = new TransactionDAO();
        this.loanDAO = new LoanDAO();
        this.overdraftDAO = new OverdraftDAO();
        this.customerDAO = new CustomerDAO();
    }
    
    public Scene createCustomerScene() {
        // Top bar with welcome and logout
        Label welcomeLabel = new Label("Welcome, " + SessionManager.getInstance().getUsername());
        welcomeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        
        Button logoutButton = new Button("Logout");
        logoutButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
        logoutButton.setOnAction(e -> logout());
        
        HBox topBar = new HBox(20);
        topBar.setPadding(new Insets(10));
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.getChildren().addAll(welcomeLabel, logoutButton);
        HBox.setHgrow(welcomeLabel, Priority.ALWAYS);
        topBar.setStyle("-fx-background-color: #4CAF50;");
        
        // Menu buttons
        Button viewBalanceBtn = new Button("View Balance");
        viewBalanceBtn.setPrefSize(200, 50);
        viewBalanceBtn.setOnAction(e -> showBalance());
        
        Button depositBtn = new Button("Deposit Money");
        depositBtn.setPrefSize(200, 50);
        depositBtn.setOnAction(e -> showDepositForm());
        
        Button withdrawBtn = new Button("Withdraw Money");
        withdrawBtn.setPrefSize(200, 50);
        withdrawBtn.setOnAction(e -> showWithdrawForm());
        
        Button applyLoanBtn = new Button("Apply for Loan");
        applyLoanBtn.setPrefSize(200, 50);
        applyLoanBtn.setOnAction(e -> showLoanApplicationForm());
        
        Button requestOverdraftBtn = new Button("Request Overdraft");
        requestOverdraftBtn.setPrefSize(200, 50);
        requestOverdraftBtn.setOnAction(e -> showOverdraftRequestForm());
        
        Button viewStatementBtn = new Button("View Statement");
        viewStatementBtn.setPrefSize(200, 50);
        viewStatementBtn.setOnAction(e -> showStatement());
        
        Button viewTermsBtn = new Button("Terms & Conditions");
        viewTermsBtn.setPrefSize(200, 50);
        viewTermsBtn.setOnAction(e -> showTermsAndConditions());
        
        Button changePasswordBtn = new Button("Change Password");
        changePasswordBtn.setPrefSize(200, 50);
        changePasswordBtn.setOnAction(e -> showChangePasswordForm());
        
        // Layout for buttons
        VBox menu = new VBox(12);
        menu.setPadding(new Insets(30));
        menu.setAlignment(Pos.CENTER);
        menu.getChildren().addAll(
            viewBalanceBtn,
            depositBtn,
            withdrawBtn,
            applyLoanBtn,
            requestOverdraftBtn,
            viewStatementBtn,
            viewTermsBtn,
            changePasswordBtn
        );
        
        // Main layout
        BorderPane mainLayout = new BorderPane();
        mainLayout.setTop(topBar);
        mainLayout.setCenter(menu);
        
        return new Scene(mainLayout, 800, 650);
    }
    
    private void logout() {
        SessionManager.getInstance().endSession();
        LoginView loginView = new LoginView(stage);
        stage.setScene(loginView.createLoginScene());
    }
    
    private void showBalance() {
        try {
            // Get customer ID from session
            int customerId = SessionManager.getInstance().getCustomerId();
        
            // Get all accounts for this customer
            ResultSet rs = accountDAO.getAccountsByCustomerId(customerId);
        
            StringBuilder balanceInfo = new StringBuilder();
            balanceInfo.append("Your Account(s):\n\n");
        
            boolean hasAccounts = false;
            while (rs.next()) {
                hasAccounts = true;
                String accountNumber = rs.getString("account_number");
                String accountType = rs.getString("account_type");
                double balance = rs.getDouble("balance");
                String status = rs.getString("status");
            
                balanceInfo.append("Account Number: ").append(accountNumber).append("\n");
                balanceInfo.append("Type: ").append(accountType).append("\n");
                balanceInfo.append("Balance: $").append(String.format("%.2f", balance)).append("\n");
                balanceInfo.append("Status: ").append(status).append("\n\n");
            }
        
            rs.close();
        
            if (!hasAccounts) {
                balanceInfo.append("No accounts found.");
            }
        
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Account Balance");
            alert.setHeaderText("Your Account Information");
            alert.setContentText(balanceInfo.toString());
            alert.showAndWait();
            
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Failed to retrieve balance: " + e.getMessage());
            alert.showAndWait();
            e.printStackTrace();
        }
    }
    
    private void showDepositForm() {
        try {
            // Get customer's accounts
            int customerId = SessionManager.getInstance().getCustomerId();
            ResultSet rs = accountDAO.getAccountsByCustomerId(customerId);
            
            // Create account selection
            ComboBox<String> accountBox = new ComboBox<>();
            java.util.HashMap<String, Integer> accountMap = new java.util.HashMap<>();
            
            while (rs.next()) {
                int accountId = rs.getInt("account_id");
                String accountNumber = rs.getString("account_number");
                String accountType = rs.getString("account_type");
                String display = accountNumber + " (" + accountType + ")";
                accountBox.getItems().add(display);
                accountMap.put(display, accountId);
            }
            rs.close();
            
            if (accountBox.getItems().isEmpty()) {
                showError("No accounts found!");
                return;
            }
            
            // Create dialog
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Deposit Money");
            dialog.setHeaderText("Enter deposit details");
            
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20));
            
            TextField amountField = new TextField();
            amountField.setPromptText("Amount");
            
            grid.add(new Label("Select Account:"), 0, 0);
            grid.add(accountBox, 1, 0);
            grid.add(new Label("Amount:"), 0, 1);
            grid.add(amountField, 1, 1);
            
            dialog.getDialogPane().setContent(grid);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
            
            dialog.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        String selectedAccount = accountBox.getValue();
                        if (selectedAccount == null) {
                            showError("Please select an account!");
                            return;
                        }
                        
                        double amount = Double.parseDouble(amountField.getText());
                        
                        if (amount <= 0) {
                            showError("Amount must be greater than zero!");
                            return;
                        }
                        
                        int accountId = accountMap.get(selectedAccount);
                        
                        // Get current balance
                        double currentBalance = accountDAO.getBalance(accountId);
                        double newBalance = currentBalance + amount;
                        
                        // Update balance
                        boolean updated = accountDAO.updateBalance(accountId, newBalance);
                        
                        if (updated) {
                            // Record transaction
                            transactionDAO.createTransaction(
                                accountId, 
                                "deposit", 
                                amount, 
                                newBalance, 
                                "Deposit via customer portal"
                            );
                            
                            showSuccess("Deposit successful!\nNew Balance: $" + String.format("%.2f", newBalance));
                        } else {
                            showError("Failed to update balance!");
                        }
                        
                    } catch (NumberFormatException e) {
                        showError("Invalid amount format!");
                    } catch (Exception e) {
                        showError("Error: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            });
            
        } catch (Exception e) {
            showError("Error: " + e.getMessage());
            e.printStackTrace();
        }
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
    
    private void showWithdrawForm() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Withdraw Money");
        alert.setHeaderText("Feature Coming Soon");
        alert.setContentText("Withdrawal form will be implemented here.");
        alert.showAndWait();
    }
    
    private void showLoanApplicationForm() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Apply for Loan");
        alert.setHeaderText("Feature Coming Soon");
        alert.setContentText("Loan application form will be implemented here.");
        alert.showAndWait();
    }
    
    private void showOverdraftRequestForm() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Request Overdraft");
        alert.setHeaderText("Feature Coming Soon");
        alert.setContentText("Overdraft request form will be implemented here.");
        alert.showAndWait();
    }
    
    private void showStatement() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Account Statement");
        alert.setHeaderText("Feature Coming Soon");
        alert.setContentText("Your transaction statement will be shown here.");
        alert.showAndWait();
    }
    
    private void showTermsAndConditions() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Terms & Conditions");
        alert.setHeaderText("Banking Terms & Conditions");
        alert.setContentText("1. All transactions are final.\n2. Maintain minimum balance of $100.\n3. Withdrawals limited to account balance.\n4. Bank reserves the right to freeze suspicious accounts.\n5. Customer information is kept confidential.");
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