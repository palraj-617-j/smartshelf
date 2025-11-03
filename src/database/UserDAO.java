package database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import models.User;
import org.bson.Document;
import org.bson.types.ObjectId;
import utils.PasswordUtil;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserDAO {
    private MongoCollection<Document> collection;
    
    public UserDAO() {
        MongoDatabase database = DatabaseConnection.getDatabase();
        this.collection = database.getCollection("users");
    }
    
    public boolean addUser(User user) {
        try {
            // Hash the password before storing
            String hashedPassword = PasswordUtil.hashPassword(user.getPassword());
            
            Document doc = new Document("userId", user.getUserId())
                    .append("name", user.getName())
                    .append("email", user.getEmail())
                    .append("phone", user.getPhone())
                    .append("password", hashedPassword)
                    .append("role", user.getRole())
                    .append("registeredDate", Date.from(user.getRegisteredDate().atStartOfDay(ZoneId.systemDefault()).toInstant()))
                    .append("borrowedBooks", user.getBorrowedBooks());
            
            collection.insertOne(doc);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public User authenticateUser(String userId, String password) {
        try {
            Document doc = collection.find(Filters.eq("userId", userId)).first();
            
            if (doc != null) {
                String storedHash = doc.getString("password");
                
                // Verify password against stored hash
                if (PasswordUtil.verifyPassword(password, storedHash)) {
                    return documentToUser(doc);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public User getUserById(String userId) {
        try {
            Document doc = collection.find(Filters.eq("userId", userId)).first();
            if (doc != null) {
                return documentToUser(doc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public User getUserByObjectId(ObjectId id) {
        try {
            Document doc = collection.find(Filters.eq("_id", id)).first();
            if (doc != null) {
                return documentToUser(doc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try {
            for (Document doc : collection.find()) {
                users.add(documentToUser(doc));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }
    
    private User documentToUser(Document doc) {
        User user = new User();
        user.setId(doc.getObjectId("_id"));
        user.setUserId(doc.getString("userId"));
        user.setName(doc.getString("name"));
        user.setEmail(doc.getString("email"));
        user.setPhone(doc.getString("phone"));
        user.setPassword(doc.getString("password"));
        user.setRole(doc.getString("role"));
        
        Date date = doc.getDate("registeredDate");
        if (date != null) {
            user.setRegisteredDate(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        }
        
        List<ObjectId> borrowedBooks = (List<ObjectId>) doc.get("borrowedBooks");
        if (borrowedBooks != null) {
            user.setBorrowedBooks(borrowedBooks);
        }
        
        return user;
    }
}
