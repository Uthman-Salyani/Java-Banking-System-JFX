package models;

public abstract class User {
    //attributes
    private final int userId;
    private final String userName;
    private String password;
    private final String role;
    private final String createdDate;

    //constructor
    public User(int userId, String userName, String password, String role, String createdDate){
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.role = role;
        this.createdDate = createdDate;
    }

    //what all users can do 
    public abstract void login();
    public abstract void logout();
    public abstract void changePassword(String newPassword);

    //getters
    public int getUserId(){
        return userId;
    }
    public String getUserName(){
        return userName;
    }
    public String getPassword(){
        return password;
    }
    public String getRole(){
        return role;
    }
    public String getCreatedDate(){
        return createdDate;
    }

    //setters
    public void setPassword(String password){
        this.password = password;
    }
}
