package models;

public abstract class Account {
    //attributes
    private final int accountId;
    private  final int customerId;
    private final String accountNumber;
    private double balance;
    private String status;
    private final String createdDate;
    private final String accountType;

    //custructor
    public Account(int accountId, int customerId, String accountNumber, double balance, String status, String createdDate, String accountType){
        this.accountId = accountId;
        this.customerId = customerId;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.status = status;
        this.createdDate = createdDate;
        this.accountType = accountType;
    }

    //getters
    public int getAccountId(){
        return accountId;
    }
    public int getCustomerId(){
        return customerId;
    }
    public String getAccountNumber(){
        return accountNumber;
    }
    public double getBalance(){
        return balance;
    }
    public String getStatus(){
        return status;
    }
    public String getCreatedDate(){
        return createdDate;
    }
    public String getAccountType(){
        return accountType;
    }

    //setters
    public void setBalance(double balance){
        this.balance = balance;
    }
    public void setStatus(String status){
        this.status = status;
    }

    //abstract methods
    public abstract void deposit(double amount);
    public abstract void withdraw(double amount);
    public String getAccountDetails(){ 
        return "Account #" + accountNumber + ", Balance: " + balance;
    }

}
