package database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import models.Transaction;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TransactionDAO {
    private MongoCollection<Document> collection;
    
    public TransactionDAO() {
        MongoDatabase database = DatabaseConnection.getDatabase();
        this.collection = database.getCollection("transactions");
    }
    
    public boolean addTransaction(Transaction transaction) {
        try {
            Document doc = new Document("bookId", transaction.getBookId())
                    .append("userId", transaction.getUserId())
                    .append("issueDate", Date.from(transaction.getIssueDate().atStartOfDay(ZoneId.systemDefault()).toInstant()))
                    .append("dueDate", Date.from(transaction.getDueDate().atStartOfDay(ZoneId.systemDefault()).toInstant()))
                    .append("returnDate", transaction.getReturnDate() != null ? 
                        Date.from(transaction.getReturnDate().atStartOfDay(ZoneId.systemDefault()).toInstant()) : null)
                    .append("status", transaction.getStatus())
                    .append("fine", transaction.getFine());
            
            collection.insertOne(doc);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Transaction> getAllTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        try {
            for (Document doc : collection.find()) {
                transactions.add(documentToTransaction(doc));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return transactions;
    }
    
    public List<Transaction> getActiveTransactionsByUser(ObjectId userId) {
        List<Transaction> transactions = new ArrayList<>();
        try {
            for (Document doc : collection.find(Filters.and(
                Filters.eq("userId", userId),
                Filters.eq("status", "issued")
            ))) {
                transactions.add(documentToTransaction(doc));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return transactions;
    }
    
    public boolean returnBook(ObjectId bookId, ObjectId userId) {
        try {
            collection.updateOne(
                Filters.and(
                    Filters.eq("bookId", bookId),
                    Filters.eq("userId", userId),
                    Filters.eq("status", "issued")
                ),
                Updates.combine(
                    Updates.set("returnDate", new Date()),
                    Updates.set("status", "returned")
                )
            );
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private Transaction documentToTransaction(Document doc) {
        Transaction transaction = new Transaction();
        transaction.setId(doc.getObjectId("_id"));
        transaction.setBookId(doc.getObjectId("bookId"));
        transaction.setUserId(doc.getObjectId("userId"));
        
        Date issueDate = doc.getDate("issueDate");
        if (issueDate != null) {
            transaction.setIssueDate(issueDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        }
        
        Date dueDate = doc.getDate("dueDate");
        if (dueDate != null) {
            transaction.setDueDate(dueDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        }
        
        Date returnDate = doc.getDate("returnDate");
        if (returnDate != null) {
            transaction.setReturnDate(returnDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        }
        
        transaction.setStatus(doc.getString("status"));
        transaction.setFine(doc.getDouble("fine"));
        
        return transaction;
    }
}
