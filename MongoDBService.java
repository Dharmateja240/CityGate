package backend;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * MongoDB service for local MongoDB connection
 * Connects to mongodb://localhost:27017/userdetail
 * Stores user registration data only
 */
public class MongoDBService {
    
    // Local MongoDB connection details
    private static final String MONGODB_HOST = "localhost";
    private static final int MONGODB_PORT = 27017;
    private static final String DATABASE_NAME = "userdetail";
    private static final String COLLECTION_NAME = "userdetails";
    
    // MongoDB connection check
    private static boolean isMongoDBAvailable() {
        try (Socket socket = new Socket()) {
            socket.connect(new java.net.InetSocketAddress(MONGODB_HOST, MONGODB_PORT), 2000);
            System.out.println("MongoDB connection successful at " + MONGODB_HOST + ":" + MONGODB_PORT);
            return true;
        } catch (Exception e) {
            System.out.println("MongoDB not available at " + MONGODB_HOST + ":" + MONGODB_PORT + " - " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Store user registration data in MongoDB
     * This method only stores data, doesn't fetch
     */
    public static boolean createUser(User user) {
        if (!isMongoDBAvailable()) {
            System.out.println("MongoDB not available, falling back to file storage");
            return false;
        }
        
        // Use the direct MongoDB connector for direct storage
        return DirectMongoDBConnector.storeUserRegistration(user);
    }
    
    /**
     * This service only stores data, doesn't fetch from MongoDB
     * Returns null to fall back to file storage for authentication
     */
    public static User findUserByEmail(String email) {
        System.out.println("MongoDB service configured for storage only, falling back to file storage for authentication");
        return null;
    }
    
    /**
     * This service only stores data, doesn't fetch from MongoDB
     * Returns empty list to fall back to file storage
     */
    public static List<User> getAllUsers() {
        System.out.println("MongoDB service configured for storage only, falling back to file storage");
        return new ArrayList<>();
    }
    
    /**
     * Get collection information
     */
    public static String getCollectionInfo() {
        return DirectMongoDBConnector.getCollectionInfo();
    }
    
    /**
     * Test MongoDB connection
     */
    public static boolean testConnection() {
        return isMongoDBAvailable();
    }
}