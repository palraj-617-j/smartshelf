package database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import models.Book;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class BookDAO {
    private MongoCollection<Document> collection;
    
    public BookDAO() {
        MongoDatabase database = DatabaseConnection.getDatabase();
        this.collection = database.getCollection("books");
    }
    
    public boolean addBook(Book book) {
        try {
            Document doc = new Document("isbn", book.getIsbn())
                    .append("title", book.getTitle())
                    .append("author", book.getAuthor())
                    .append("category", book.getCategory())
                    .append("publisher", book.getPublisher())
                    .append("year", book.getYear())
                    .append("shelfLocation", book.getShelfLocation())
                    .append("totalCopies", book.getTotalCopies())
                    .append("availableCopies", book.getAvailableCopies())
                    .append("status", book.getStatus());
            
            collection.insertOne(doc);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        try {
            for (Document doc : collection.find()) {
                books.add(documentToBook(doc));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return books;
    }
    
    public List<Book> searchBooksByTitle(String title) {
        List<Book> books = new ArrayList<>();
        try {
            for (Document doc : collection.find(Filters.regex("title", title, "i"))) {
                books.add(documentToBook(doc));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return books;
    }
    
    public Book getBookByIsbn(String isbn) {
        try {
            Document doc = collection.find(Filters.eq("isbn", isbn)).first();
            if (doc != null) {
                return documentToBook(doc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public Book getBookById(ObjectId id) {
        try {
            Document doc = collection.find(Filters.eq("_id", id)).first();
            if (doc != null) {
                return documentToBook(doc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean updateBook(Book book) {
        try {
            collection.updateOne(
                Filters.eq("_id", book.getId()),
                Updates.combine(
                    Updates.set("title", book.getTitle()),
                    Updates.set("author", book.getAuthor()),
                    Updates.set("category", book.getCategory()),
                    Updates.set("publisher", book.getPublisher()),
                    Updates.set("year", book.getYear()),
                    Updates.set("shelfLocation", book.getShelfLocation()),
                    Updates.set("totalCopies", book.getTotalCopies()),
                    Updates.set("availableCopies", book.getAvailableCopies()),
                    Updates.set("status", book.getStatus())
                )
            );
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteBook(ObjectId id) {
        try {
            collection.deleteOne(Filters.eq("_id", id));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean updateAvailableCopies(ObjectId bookId, int change) {
        try {
            collection.updateOne(
                Filters.eq("_id", bookId),
                Updates.inc("availableCopies", change)
            );
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private Book documentToBook(Document doc) {
        Book book = new Book();
        book.setId(doc.getObjectId("_id"));
        book.setIsbn(doc.getString("isbn"));
        book.setTitle(doc.getString("title"));
        book.setAuthor(doc.getString("author"));
        book.setCategory(doc.getString("category"));
        book.setPublisher(doc.getString("publisher"));
        book.setYear(doc.getInteger("year"));
        book.setShelfLocation(doc.getString("shelfLocation"));
        book.setTotalCopies(doc.getInteger("totalCopies"));
        book.setAvailableCopies(doc.getInteger("availableCopies"));
        book.setStatus(doc.getString("status"));
        return book;
    }
}
