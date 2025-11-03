package controllers;

import database.BookDAO;
import database.TransactionDAO;
import database.UserDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import models.Book;
import models.Transaction;
import models.User;
import utils.AlertUtils;

import java.time.LocalDate;

public class IssueReturnController {
    
    @FXML
    private TextField txtBookIsbn;
    @FXML
    private TextField txtUserId;
    @FXML
    private TextField txtDays;
    
    private User currentUser;
    private BookDAO bookDAO = new BookDAO();
    private UserDAO userDAO = new UserDAO();
    private TransactionDAO transactionDAO = new TransactionDAO();
    
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }
    
    @FXML
    public void handleIssueBook(ActionEvent event) {
        String isbn = txtBookIsbn.getText().trim();
        String userId = txtUserId.getText().trim();
        String daysStr = txtDays.getText().trim();
        
        if (isbn.isEmpty() || userId.isEmpty() || daysStr.isEmpty()) {
            AlertUtils.showWarning("Warning", "Please fill all fields!");
            return;
        }
        
        try {
            Book book = bookDAO.getBookByIsbn(isbn);
            if (book == null) {
                AlertUtils.showError("Error", "Book not found!");
                return;
            }
            
            if (book.getAvailableCopies() <= 0) {
                AlertUtils.showError("Error", "No copies available!");
                return;
            }
            
            User user = userDAO.getUserById(userId);
            if (user == null) {
                AlertUtils.showError("Error", "User not found!");
                return;
            }
            
            int days = Integer.parseInt(daysStr);
            LocalDate issueDate = LocalDate.now();
            LocalDate dueDate = issueDate.plusDays(days);
            
            Transaction transaction = new Transaction(book.getId(), user.getId(), issueDate, dueDate);
            
            if (transactionDAO.addTransaction(transaction)) {
                bookDAO.updateAvailableCopies(book.getId(), -1);
                AlertUtils.showInfo("Success", "Book issued successfully!\nDue Date: " + dueDate);
                clearFields();
            } else {
                AlertUtils.showError("Error", "Failed to issue book!");
            }
        } catch (NumberFormatException e) {
            AlertUtils.showError("Error", "Please enter valid number of days!");
        }
    }
    
    @FXML
    public void handleReturnBook(ActionEvent event) {
        String isbn = txtBookIsbn.getText().trim();
        String userId = txtUserId.getText().trim();
        
        if (isbn.isEmpty() || userId.isEmpty()) {
            AlertUtils.showWarning("Warning", "Please enter ISBN and User ID!");
            return;
        }
        
        Book book = bookDAO.getBookByIsbn(isbn);
        if (book == null) {
            AlertUtils.showError("Error", "Book not found!");
            return;
        }
        
        User user = userDAO.getUserById(userId);
        if (user == null) {
            AlertUtils.showError("Error", "User not found!");
            return;
        }
        
        if (transactionDAO.returnBook(book.getId(), user.getId())) {
            bookDAO.updateAvailableCopies(book.getId(), 1);
            AlertUtils.showInfo("Success", "Book returned successfully!");
            clearFields();
        } else {
            AlertUtils.showError("Error", "Failed to return book or no active transaction found!");
        }
    }
    
    private void clearFields() {
        txtBookIsbn.clear();
        txtUserId.clear();
        txtDays.clear();
    }
}
