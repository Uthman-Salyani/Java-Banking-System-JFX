package models;

public class Transaction {
    private final int transactionId;
    private final int accountId;
    private final String transactionType;
    private final double amount;
    private final double balanceAfter;
    private final String description;
    private final String transactionDate;

    public Transaction(int transactionId, int accountId, String transactionType, double amount, double balanceAfter, String description,String transactionDate){
        this.transactionId = transactionId;
        this.accountId = accountId;
        this.transactionType = transactionType;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.description = description;
        this.transactionDate = transactionDate;
    }

    //getters
    public int getTransactionId(){
        return transactionId;
    }
    public int getAccountId(){
        return accountId;
    }
    public String getTransactionType(){
        return transactionType;
    }
    public double getAmount(){
        return amount;
    }
    public double getBalanceAfter(){
        return balanceAfter;
    }
    public String getDescription(){
        return description;
    }
    public String getTransactionDate(){
        return transactionDate;
    }
}
