package models;

public class SavingsAccount extends Account {
    //constructor
    public SavingsAccount(int accountId, int customerId, String accountNumber, double balance, String status, String createdDate, String accountType){
        super(accountId, customerId, accountNumber, balance, status, createdDate, accountType );
    }

    //implementing abstract methods -> what savings account should do
    @Override
    public void deposit(double amount){
        setBalance(getBalance() + amount);
        System.out.println("Deposited: $"+amount);
        System.out.println("New balance: $"+getBalance());
    }
    @Override
    public void withdraw(double amount){
        if (getBalance()>=amount){
            setBalance(getBalance() - amount);
            System.out.println("Withdrawal of $"+amount+" successful");
        } else{
            System.out.println("Withdrawal of $"+amount+" unsuccessful; Insufficient balance");
        }
    }
}

