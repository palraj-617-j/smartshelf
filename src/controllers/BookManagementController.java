package controllers;

import database.BookDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Book;
import utils.AlertUtils;

import java.net.URL;
import java.util.ResourceBundle;

public class BookManagementController implements Initializable {
    
    @FXML
    private TextField txtIsbn;
    @FXML
    private TextField txtTitle;
    @FXML
    private TextField txtAuthor;
    @FXML
    private TextField txtCategory;
    @FXML
    private TextField txtPublisher;
    @FXML
    private TextField txtYear;
    @FXML
    private TextField txtShelfLocation;
    @FXML
    private TextField txtTotalCopies;
    @FXML
    private TextField txtSearch;
    
    @FXML
    private TableView<Book> tableBooks;
    @FXML
    private TableColumn<Book, String> colIsbn;
    @FXML
    private TableColumn<Book, String> colTitle;
    @FXML
    private TableColumn<Book, String> colAuthor;
    @FXML
    private TableColumn<Book, String> colCategory;
    @FXML
    private TableColumn<Book, Integer> colYear;
    @FXML
    private TableColumn<Book, String> colShelfLocation;
    @FXML
    private TableColumn<Book, Integer> colAvailable;
    
    private BookDAO bookDAO = new BookDAO();
    private ObservableList<Book> bookList = FXCollections.observableArrayList();
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colIsbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colYear.setCellValueFactory(new PropertyValueFactory<>("year"));
        colShelfLocation.setCellValueFactory(new PropertyValueFactory<>("shelfLocation"));
        colAvailable.setCellValueFactory(new PropertyValueFactory<>("availableCopies"));
        
        loadBooks();
        
        tableBooks.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                fillFields(newSelection);
            }
        });
    }
    
    private void loadBooks() {
        bookList.clear();
        bookList.addAll(bookDAO.getAllBooks());
        tableBooks.setItems(bookList);
    }
    
    private void fillFields(Book book) {
        txtIsbn.setText(book.getIsbn());
        txtTitle.setText(book.getTitle());
        txtAuthor.setText(book.getAuthor());
        txtCategory.setText(book.getCategory());
        txtPublisher.setText(book.getPublisher());
        txtYear.setText(String.valueOf(book.getYear()));
        txtShelfLocation.setText(book.getShelfLocation());
        txtTotalCopies.setText(String.valueOf(book.getTotalCopies()));
    }
    
    @FXML
    public void handleAddBook(ActionEvent event) {
        try {
            Book book = new Book(
                txtIsbn.getText().trim(),
                txtTitle.getText().trim(),
                txtAuthor.getText().trim(),
                txtCategory.getText().trim(),
                txtPublisher.getText().trim(),
                Integer.parseInt(txtYear.getText().trim()),
                txtShelfLocation.getText().trim(),
                Integer.parseInt(txtTotalCopies.getText().trim())
            );
            
            if (bookDAO.addBook(book)) {
                AlertUtils.showInfo("Success", "Book added successfully!");
                clearFields();
                loadBooks();
            } else {
                AlertUtils.showError("Error", "Failed to add book!");
            }
        } catch (NumberFormatException e) {
            AlertUtils.showError("Error", "Please enter valid numbers for year and copies!");
        }
    }
    
    @FXML
    public void handleUpdateBook(ActionEvent event) {
        Book selectedBook = tableBooks.getSelectionModel().getSelectedItem();
        if (selectedBook == null) {
            AlertUtils.showWarning("Warning", "Please select a book to update!");
            return;
        }
        
        try {
            selectedBook.setTitle(txtTitle.getText().trim());
            selectedBook.setAuthor(txtAuthor.getText().trim());
            selectedBook.setCategory(txtCategory.getText().trim());
            selectedBook.setPublisher(txtPublisher.getText().trim());
            selectedBook.setYear(Integer.parseInt(txtYear.getText().trim()));
            selectedBook.setShelfLocation(txtShelfLocation.getText().trim());
            selectedBook.setTotalCopies(Integer.parseInt(txtTotalCopies.getText().trim()));
            
            if (bookDAO.updateBook(selectedBook)) {
                AlertUtils.showInfo("Success", "Book updated successfully!");
                loadBooks();
            } else {
                AlertUtils.showError("Error", "Failed to update book!");
            }
        } catch (NumberFormatException e) {
            AlertUtils.showError("Error", "Please enter valid numbers!");
        }
    }
    
    @FXML
    public void handleDeleteBook(ActionEvent event) {
        Book selectedBook = tableBooks.getSelectionModel().getSelectedItem();
        if (selectedBook == null) {
            AlertUtils.showWarning("Warning", "Please select a book to delete!");
            return;
        }
        
        if (bookDAO.deleteBook(selectedBook.getId())) {
            AlertUtils.showInfo("Success", "Book deleted successfully!");
            clearFields();
            loadBooks();
        } else {
            AlertUtils.showError("Error", "Failed to delete book!");
        }
    }
    
    @FXML
    public void handleSearch(ActionEvent event) {
        String searchTerm = txtSearch.getText().trim();
        if (searchTerm.isEmpty()) {
            loadBooks();
        } else {
            bookList.clear();
            bookList.addAll(bookDAO.searchBooksByTitle(searchTerm));
            tableBooks.setItems(bookList);
        }
    }
    
    @FXML
    public void handleClear(ActionEvent event) {
        clearFields();
    }
    
    private void clearFields() {
        txtIsbn.clear();
        txtTitle.clear();
        txtAuthor.clear();
        txtCategory.clear();
        txtPublisher.clear();
        txtYear.clear();
        txtShelfLocation.clear();
        txtTotalCopies.clear();
        tableBooks.getSelectionModel().clearSelection();
    }
}
