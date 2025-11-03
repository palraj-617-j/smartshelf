package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Screen;
import javafx.stage.Stage;
import models.User;
import database.BookDAO;
import database.TransactionDAO;

public class DashboardController {
    
    @FXML
    private Label lblWelcome;
    
    @FXML
    private Label lblTotalBooks;
    
    @FXML
    private Label lblIssuedBooks;
    
    @FXML
    private Label lblAvailableBooks;
    
    private User currentUser;
    private BookDAO bookDAO = new BookDAO();
    private TransactionDAO transactionDAO = new TransactionDAO();
    
    public void setCurrentUser(User user) {
        this.currentUser = user;
        lblWelcome.setText("Welcome, " + user.getName() + "!");
        loadStatistics();
    }
    
    private void loadStatistics() {
        int totalBooks = bookDAO.getAllBooks().size();
        int issuedBooks = transactionDAO.getAllTransactions().stream()
            .filter(t -> t.getStatus().equals("issued"))
            .toArray().length;
        
        lblTotalBooks.setText(String.valueOf(totalBooks));
        lblIssuedBooks.setText(String.valueOf(issuedBooks));
        lblAvailableBooks.setText(String.valueOf(totalBooks - issuedBooks));
    }
    
    @FXML
    public void openBookManagement(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/fxml/BookManagement.fxml"));
            Parent root = loader.load();
            
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/resources/css/style.css").toExternalForm());
            stage.setTitle("Book Management");
            stage.setScene(scene);
            
            // Set window to cover full screen (respecting taskbar)
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            stage.setX(screenBounds.getMinX());
            stage.setY(screenBounds.getMinY());
            stage.setWidth(screenBounds.getWidth());
            stage.setHeight(screenBounds.getHeight());
            
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    public void openIssueReturn(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/fxml/IssueReturn.fxml"));
            Parent root = loader.load();
            
            IssueReturnController controller = loader.getController();
            controller.setCurrentUser(currentUser);
            
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/resources/css/style.css").toExternalForm());
            stage.setTitle("Issue / Return Books");
            stage.setScene(scene);
            
            // Center window on screen with 80% size
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            double width = screenBounds.getWidth() * 0.8;
            double height = screenBounds.getHeight() * 0.85;
            
            stage.setWidth(width);
            stage.setHeight(height);
            stage.centerOnScreen();
            
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
