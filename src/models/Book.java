package models;

import org.bson.types.ObjectId;

public class Book {
    private ObjectId id;
    private String isbn;
    private String title;
    private String author;
    private String category;
    private String publisher;
    private int year;
    private String shelfLocation;
    private int totalCopies;
    private int availableCopies;
    private String status;
    
    public Book() {}
    
    public Book(String isbn, String title, String author, String category, 
                String publisher, int year, String shelfLocation, int totalCopies) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.category = category;
        this.publisher = publisher;
        this.year = year;
        this.shelfLocation = shelfLocation;
        this.totalCopies = totalCopies;
        this.availableCopies = totalCopies;
        this.status = "available";
    }
    
    public ObjectId getId() { return id; }
    public void setId(ObjectId id) { this.id = id; }
    
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { this.publisher = publisher; }
    
    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }
    
    public String getShelfLocation() { return shelfLocation; }
    public void setShelfLocation(String shelfLocation) { this.shelfLocation = shelfLocation; }
    
    public int getTotalCopies() { return totalCopies; }
    public void setTotalCopies(int totalCopies) { this.totalCopies = totalCopies; }
    
    public int getAvailableCopies() { return availableCopies; }
    public void setAvailableCopies(int availableCopies) { this.availableCopies = availableCopies; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
