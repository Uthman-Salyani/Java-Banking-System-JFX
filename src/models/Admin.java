package models;

public class Admin extends User{
    public Admin(int userId, String userName, String password, String role, String createdDate){
        super(userId, userName, password, role, createdDate);
    }
    //what admin as a user must do
    @Override
    public void login(){
        System.out.println("Admin logged in");
    }

    @Override
    public void logout(){
        System.out.println("Admin has logged out");
    }

    @Override
    public void changePassword(String newPassword){
        setPassword(newPassword);
        System.out.println("Admin has changed the password");
    }

    //what admin can do 
    public void createCustomer(){
        //later
    }
    public void deleteCustomer(){
        //later
    }
    public void viewAllCustomers(){
        //later
    }
    public void approveLoan(){
        //later
    }
    public void approveOverdraft(){
        //later
    }
    public void exportRecords(){
        //later
    }
}
