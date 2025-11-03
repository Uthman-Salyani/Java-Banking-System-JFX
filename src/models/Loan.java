package models;

public class Loan {
    private final int loanId;
    private final int customerId;
    private final double loanAmount;
    private final double interestRate;
    private final int durationMonths;
    private String status;
    private final String applicationDate;
    private String processedDate;

    public Loan(int loanId, int customerId, double loanAmount, double interestRate, int durationMonths, String status, String applicationDate, String processedDate){
        this.loanId = loanId;
        this.customerId = customerId;
        this.loanAmount = loanAmount;
        this.interestRate = interestRate;
        this.durationMonths = durationMonths;
        this.status = status;
        this.applicationDate = applicationDate;
        this.processedDate = processedDate;
    }

    //getters
    public int getLoanId(){
        return loanId;
    }
    public int getCustomerId(){
        return customerId;
    }
    public double getLoanAmount(){
        return loanAmount;
    }
    public double getInterestRate(){
        return interestRate;
    }
    public int getDurationMonths(){
        return durationMonths;
    }
    public String getStatus(){
        return status;
    }
    public String getApplicationDate(){
        return applicationDate;
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
