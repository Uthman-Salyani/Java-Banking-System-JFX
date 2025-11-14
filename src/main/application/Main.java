package main.application;

import javafx.application.Application;
import javafx.stage.Stage;
import views.LoginView;

public class Main extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Nix - My Banking App");
        
        LoginView loginView = new LoginView(primaryStage);
        primaryStage.setScene(loginView.createLoginScene());
        
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}