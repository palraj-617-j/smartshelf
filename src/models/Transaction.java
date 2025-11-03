package models;

import org.bson.types.ObjectId;
import java.time.LocalDate;

public class Transaction {
    private ObjectId id;
    private ObjectId bookId;
    private ObjectId userId;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private String status;
    private double fine;
    
    public Transaction() {}
    
    public Transaction(ObjectId bookId, ObjectId userId, LocalDate issueDate, LocalDate dueDate) {
        this.bookId = bookId;
        this.userId = userId;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.status = "issued";
        this.fine = 0.0;
    }
    
    public ObjectId getId() { return id; }
    public void setId(ObjectId id) { this.id = id; }
    
    public ObjectId getBookId() { return bookId; }
    public void setBookId(ObjectId bookId) { this.bookId = bookId; }
    
    public ObjectId getUserId() { return userId; }
    public void setUserId(ObjectId userId) { this.userId = userId; }
    
    public LocalDate getIssueDate() { return issueDate; }
    public void setIssueDate(LocalDate issueDate) { this.issueDate = issueDate; }
    
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    
    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public double getFine() { return fine; }
    public void setFine(double fine) { this.fine = fine; }
}
