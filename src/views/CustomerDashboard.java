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
    private UserDAO userDAO;
    
    public CustomerDashboard(Stage stage) {
        this.stage = stage;
        this.accountDAO = new AccountDAO();
        this.transactionDAO = new TransactionDAO();
        this.loanDAO = new LoanDAO();
        this.overdraftDAO = new OverdraftDAO();
        this.customerDAO = new CustomerDAO();
        this.userDAO = new UserDAO();
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
                double balance = rs.getDouble("balance");
                String display = accountNumber + " (" + accountType + ") - $" + String.format("%.2f", balance);
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
            dialog.setTitle("Withdraw Money");
            dialog.setHeaderText("Enter withdrawal details");
            
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
                        
                        // Check if sufficient balance
                        if (currentBalance < amount) {
                            showError("Insufficient balance!\nCurrent Balance: $" + String.format("%.2f", currentBalance));
                            return;
                        }

                        double newBalance = currentBalance - amount;

                        // Check minimum balance for savings accounts (get account type first)
                        ResultSet accountInfo = accountDAO.getAccountById(accountId);
                        if (accountInfo.next()) {
                            String accountType = accountInfo.getString("account_type");
                            
                            // Savings accounts must maintain minimum $100 balance
                            if (accountType.equals("savings") && newBalance < 100) {
                                showError("Savings accounts must maintain a minimum balance of $100!\n" +
                                        "After withdrawal, your balance would be: $" + String.format("%.2f", newBalance));
                                accountInfo.close();
                                return;
                            }
                        }
                        accountInfo.close();

                        // Update balance
                        boolean updated = accountDAO.updateBalance(accountId, newBalance);
                        
                        if (updated) {
                            // Record transaction
                            transactionDAO.createTransaction(
                                accountId, 
                                "withdrawal", 
                                amount, 
                                newBalance, 
                                "Withdrawal via customer portal"
                            );
                            
                            showSuccess("Withdrawal successful!\nNew Balance: $" + String.format("%.2f", newBalance));
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
    
    private void showLoanApplicationForm() {
    // Create dialog
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Apply for Loan");
        dialog.setHeaderText("Enter loan application details");
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        
        TextField loanAmountField = new TextField();
        loanAmountField.setPromptText("Loan Amount");

        Label interestRateLabel = new Label("5.5%");  // Show as label, not editable
        interestRateLabel.setStyle("-fx-font-weight: bold;");

        TextField durationField = new TextField();
        durationField.setPromptText("Duration (months)");

        grid.add(new Label("Loan Amount:"), 0, 0);
        grid.add(loanAmountField, 1, 0);
        grid.add(new Label("Interest Rate:"), 0, 1);
        grid.add(interestRateLabel, 1, 1);  // Label instead of TextField
        grid.add(new Label("Duration (months):"), 0, 2);
        grid.add(durationField, 1, 2);
        
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    double loanAmount = Double.parseDouble(loanAmountField.getText());
                    double interestRate = 5.5; // Fixed interest rate for simplicity
                    int duration = Integer.parseInt(durationField.getText());
                    
                    // Validation
                    if (loanAmount <= 0) {
                        showError("Loan amount must be greater than zero!");
                        return;
                    }
                    
                    if (interestRate <= 0) {
                        showError("Interest rate must be greater than zero!");
                        return;
                    }
                    
                    if (duration <= 0) {
                        showError("Duration must be greater than zero!");
                        return;
                    }
                    
                    // Get customer ID
                    int customerId = SessionManager.getInstance().getCustomerId();
                    
                    // Create loan application
                    boolean created = loanDAO.createLoanApplication(customerId, loanAmount, interestRate, duration);
                    
                    if (created) {
                        showSuccess("Loan application submitted successfully!\n" +
                                "Amount: $" + String.format("%.2f", loanAmount) + "\n" +
                                "Status: Pending approval\n\n" +
                                "An administrator will review your application soon.");
                    } else {
                        showError("Failed to submit loan application!");
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
    
    private void showOverdraftRequestForm() {
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
            dialog.setTitle("Request Overdraft");
            dialog.setHeaderText("Enter overdraft request details");
            
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20));
            
            TextField overdraftLimitField = new TextField();
            overdraftLimitField.setPromptText("Overdraft Limit");
            
            grid.add(new Label("Select Account:"), 0, 0);
            grid.add(accountBox, 1, 0);
            grid.add(new Label("Overdraft Limit ($):"), 0, 1);
            grid.add(overdraftLimitField, 1, 1);
            
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
                        
                        double overdraftLimit = Double.parseDouble(overdraftLimitField.getText());
                        
                        // Validation
                        if (overdraftLimit <= 0) {
                            showError("Overdraft limit must be greater than zero!");
                            return;
                        }
                        
                        if (overdraftLimit > 5000) {
                            showError("Maximum overdraft limit is $5,000!");
                            return;
                        }
                        
                        int accountId = accountMap.get(selectedAccount);
                        
                        // Create overdraft request
                        boolean created = overdraftDAO.createOverdraftRequest(accountId, overdraftLimit);
                        
                        if (created) {
                            showSuccess("Overdraft request submitted successfully!\n" +
                                    "Requested Limit: $" + String.format("%.2f", overdraftLimit) + "\n" +
                                    "Status: Pending approval\n\n" +
                                    "An administrator will review your request soon.");
                        } else {
                            showError("Failed to submit overdraft request!");
                        }
                        
                    } catch (NumberFormatException e) {
                        showError("Invalid number format!");
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
    
    private void showStatement() {
    // First, ask for password re-authentication (security feature)
    TextInputDialog passwordDialog = new TextInputDialog();
    passwordDialog.setTitle("Security Verification");
    passwordDialog.setHeaderText("Please re-enter your password to view statement");
    passwordDialog.setContentText("Password:");
    
    // Make it a password field
    TextField textField = passwordDialog.getEditor();
    textField.setPromptText("Enter password");
    
    passwordDialog.showAndWait().ifPresent(password -> {
        try {
            // Verify password
            String username = SessionManager.getInstance().getUsername();
            boolean isValid = userDAO.validateLogin(username, password);
            
            if (!isValid) {
                showError("Incorrect password!");
                return;
            }
            
            // Get customer's accounts
            int customerId = SessionManager.getInstance().getCustomerId();
            ResultSet accountsRs = accountDAO.getAccountsByCustomerId(customerId);
            
            // Create account selection
            ComboBox<String> accountBox = new ComboBox<>();
            java.util.HashMap<String, Integer> accountMap = new java.util.HashMap<>();
            
            while (accountsRs.next()) {
                int accountId = accountsRs.getInt("account_id");
                String accountNumber = accountsRs.getString("account_number");
                String accountType = accountsRs.getString("account_type");
                String display = accountNumber + " (" + accountType + ")";
                accountBox.getItems().add(display);
                accountMap.put(display, accountId);
            }
            accountsRs.close();
            
            if (accountBox.getItems().isEmpty()) {
                showError("No accounts found!");
                return;
            }
            
            // Create dialog to select account
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("View Statement");
            dialog.setHeaderText("Select account to view transactions");
            
            VBox vbox = new VBox(10);
            vbox.setPadding(new Insets(20));
            vbox.getChildren().addAll(new Label("Select Account:"), accountBox);
            
            dialog.getDialogPane().setContent(vbox);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
            
            dialog.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        String selectedAccount = accountBox.getValue();
                        if (selectedAccount == null) {
                            showError("Please select an account!");
                            return;
                        }
                        
                        int accountId = accountMap.get(selectedAccount);
                        
                        // Get transactions
                        ResultSet transRs = transactionDAO.getTransactionsByAccountId(accountId);
                        
                        StringBuilder statement = new StringBuilder();
                        statement.append("ACCOUNT STATEMENT\n");
                        statement.append("Account: ").append(selectedAccount).append("\n");
                        statement.append("-".repeat(50)).append("\n\n");
                        
                        boolean hasTransactions = false;
                        while (transRs.next()) {
                            hasTransactions = true;
                            String date = transRs.getString("transaction_date");
                            String type = transRs.getString("transaction_type");
                            double amount = transRs.getDouble("amount");
                            double balanceAfter = transRs.getDouble("balance_after");
                            String description = transRs.getString("description");
                            
                            statement.append("Date: ").append(date).append("\n");
                            statement.append("Type: ").append(type.toUpperCase()).append("\n");
                            statement.append("Amount: $").append(String.format("%.2f", amount)).append("\n");
                            statement.append("Balance After: $").append(String.format("%.2f", balanceAfter)).append("\n");
                            statement.append("Description: ").append(description).append("\n");
                            statement.append("-".repeat(50)).append("\n\n");
                        }
                        transRs.close();
                        
                        if (!hasTransactions) {
                            statement.append("No transactions found.");
                        }
                        
                        // Show statement in a scrollable dialog
                        Alert statementAlert = new Alert(Alert.AlertType.INFORMATION);
                        statementAlert.setTitle("Account Statement");
                        statementAlert.setHeaderText("Transaction History");
                        
                        TextArea textArea = new TextArea(statement.toString());
                        textArea.setEditable(false);
                        textArea.setWrapText(true);
                        textArea.setPrefSize(600, 400);
                        
                        statementAlert.getDialogPane().setContent(textArea);
                        statementAlert.showAndWait();
                        
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
    });
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