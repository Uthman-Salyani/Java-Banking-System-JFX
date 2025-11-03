package models;

public class Overdraft {
    private final int overdraftId;
    private final int accountId;
    private final double overdraftLimit;
    private String status;
    private final String requestedDate;
    private String processedDate;

    //constructor
    public Overdraft(int overdraftId, int accountId, double overdraftLimit, String status, String requestedDate, String processedDate){
        this.overdraftId = overdraftId;
        this.accountId = accountId;
        this.overdraftLimit = overdraftLimit;
        this.status = status;
        this.requestedDate = requestedDate;
        this.processedDate = processedDate;
    }

    //getters
    public int getOverdraftId(){
        return overdraftId;
    }
    public int getAccountId(){
        return accountId;
    }
    public double getOverdraftLimit(){
        return overdraftLimit;
    }
    public String getStatus(){
        return status;
    }
    public String getRequestedDate(){
        return requestedDate;
    }
    public String getProcessedDate(){
        return processedDate; 
    }

    //setters
    public void setStatus(String status){
        this.status = status;
    }
    public void setProcessedDate(String processedDate){
        this.processedDate = processedDate;
    }
}
