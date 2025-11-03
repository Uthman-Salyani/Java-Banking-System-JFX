package models;

public class Customer extends User{
    //customer specific attributes 
    private final int customerId;
    private final String firstName;
    private final String lastName;
    private String email;
    private String phone;
    private String address;
    private final String dateOfBirth;

    //constructor
    public Customer(int userId, String userName, String password, String role, String createdDate, int customerId, String firstName, String lastName, String email, String phone, String address, String dateOfBirth){
        super(userId, userName, password, role, createdDate);
        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
    }
    
    //what a customer must do 
    @Override
    public void login(){
        System.out.println("Customer logged in");
    }
    @Override
    public void logout(){
        System.out.println("Customer logged out");
    }
    @Override
    public void changePassword(String newPassword){
        setPassword(newPassword);
        System.out.println("Password changed");
    }

    //getters 
    public int getCustomerId(){
        return customerId;
    }
    public String getFirstName(){
        return firstName;
    }
    public String getLastName(){
        return lastName;
    }
    public String getEmail(){
        return email;
    }
    public String getPhone(){
        return phone;
    }
    public String getAddress(){
        return address;
    }
    public String getDateOfBirth(){
        return dateOfBirth;
    }

    //setters
    public void setEmail(String email){
        this.email = email;
    }
    public void setPhone(String phone){
        this.phone = phone;
    }
    public void setAddress(String address){
        this.address = address;
    }

    //customer specific methods 
    public void deposit(double amount){
        //later
    }
    public void withdraw(double amount){
        //later
    }
    public void applyForLoan(){
        //later
    }
    public void requestOverdraft(){
        //later
    }
    public void viewStatement(){
        //later
    }
    public void viewTermsAndConditions(){
        //later
    }

}
