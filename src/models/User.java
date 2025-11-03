package models;

import org.bson.types.ObjectId;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class User {
    private ObjectId id;
    private String userId;
    private String name;
    private String email;
    private String phone;
    private String password;
    private String role;
    private LocalDate registeredDate;
    private List<ObjectId> borrowedBooks;
    
    public User() {
        this.borrowedBooks = new ArrayList<>();
    }
    
    public User(String userId, String name, String email, String phone, 
                String password, String role) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.role = role;
        this.registeredDate = LocalDate.now();
        this.borrowedBooks = new ArrayList<>();
    }
    
    public ObjectId getId() { return id; }
    public void setId(ObjectId id) { this.id = id; }
    
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    
    public LocalDate getRegisteredDate() { return registeredDate; }
    public void setRegisteredDate(LocalDate registeredDate) { this.registeredDate = registeredDate; }
    
    public List<ObjectId> getBorrowedBooks() { return borrowedBooks; }
    public void setBorrowedBooks(List<ObjectId> borrowedBooks) { this.borrowedBooks = borrowedBooks; }
}
