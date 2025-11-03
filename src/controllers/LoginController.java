package controllers;

import database.UserDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import models.User;
import utils.AlertUtils;

public class LoginController {
    
    @FXML
    private TextField txtUserId;
    
    @FXML
    private PasswordField txtPassword;
    
    @FXML
    private Label lblStatus;
    
    @FXML
    private VBox loginBox;
    
    @FXML
    private VBox signupBox;
    
    // Sign up fields
    @FXML
    private TextField txtSignupUserId;
    
    @FXML
    private TextField txtSignupName;
    
    @FXML
    private TextField txtSignupEmail;
    
    @FXML
    private TextField txtSignupPhone;
    
    @FXML
    private PasswordField txtSignupPassword;
    
    @FXML
    private PasswordField txtSignupConfirmPassword;
    
    @FXML
    private Label lblSignupStatus;
    
    private UserDAO userDAO = new UserDAO();
    
    @FXML
    public void initialize() {
        loginBox.setVisible(true);
        loginBox.setManaged(true);
        signupBox.setVisible(false);
        signupBox.setManaged(false);
    }
    
    @FXML
    public void handleLogin(ActionEvent event) {
        String userId = txtUserId.getText().trim();
        String password = txtPassword.getText().trim();
        
        if (userId.isEmpty() || password.isEmpty()) {
            lblStatus.setText("Please enter all fields!");
            return;
        }
        
        User user = userDAO.authenticateUser(userId, password);
        
        if (user != null) {
            lblStatus.setText("Login successful!");
            try {
                Stage loginStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                loginStage.close();
                
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/fxml/Dashboard.fxml"));
                Parent root = loader.load();
                
                DashboardController controller = loader.getController();
                controller.setCurrentUser(user);
                
                Stage dashboardStage = new Stage();
                Scene scene = new Scene(root);
                scene.getStylesheets().add(getClass().getResource("/resources/css/style.css").toExternalForm());
                dashboardStage.setTitle("Smart Shelf - Dashboard");
                dashboardStage.setScene(scene);
                
                // Set window to cover full screen width but not height (avoid covering taskbar)
                Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
                dashboardStage.setX(screenBounds.getMinX());
                dashboardStage.setY(screenBounds.getMinY());
                dashboardStage.setWidth(screenBounds.getWidth());
                dashboardStage.setHeight(screenBounds.getHeight());
                
                dashboardStage.show();
                
            } catch (Exception e) {
                e.printStackTrace();
                AlertUtils.showError("Error", "Failed to open dashboard!");
            }
        } else {
            lblStatus.setText("Invalid credentials!");
        }
    }
    
    @FXML
    public void handleSignUp(ActionEvent event) {
        String userId = txtSignupUserId.getText().trim();
        String name = txtSignupName.getText().trim();
        String email = txtSignupEmail.getText().trim();
        String phone = txtSignupPhone.getText().trim();
        String password = txtSignupPassword.getText().trim();
        String confirmPassword = txtSignupConfirmPassword.getText().trim();
        
        if (userId.isEmpty() || name.isEmpty() || email.isEmpty() || 
            phone.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            lblSignupStatus.setText("Please fill all fields!");
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            lblSignupStatus.setText("Passwords do not match!");
            return;
        }
        
        if (password.length() < 6) {
            lblSignupStatus.setText("Password must be at least 6 characters!");
            return;
        }
        
        if (userDAO.getUserById(userId) != null) {
            lblSignupStatus.setText("User ID already exists!");
            return;
        }
        
        User newUser = new User(userId, name, email, phone, password, "student");
        
        if (userDAO.addUser(newUser)) {
            AlertUtils.showInfo("Success", "Account created successfully! You can now login.");
            showLogin(event);
            clearSignupFields();
        } else {
            lblSignupStatus.setText("Failed to create account. Try again!");
        }
    }
    
    @FXML
    public void showSignup(ActionEvent event) {
        loginBox.setVisible(false);
        loginBox.setManaged(false);
        signupBox.setVisible(true);
        signupBox.setManaged(true);
        lblSignupStatus.setText("");
    }
    
    @FXML
    public void showLogin(ActionEvent event) {
        signupBox.setVisible(false);
        signupBox.setManaged(false);
        loginBox.setVisible(true);
        loginBox.setManaged(true);
        lblStatus.setText("");
    }
    
    private void clearSignupFields() {
        txtSignupUserId.clear();
        txtSignupName.clear();
        txtSignupEmail.clear();
        txtSignupPhone.clear();
        txtSignupPassword.clear();
        txtSignupConfirmPassword.clear();
    }
}
