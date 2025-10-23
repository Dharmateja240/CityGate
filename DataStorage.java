package backend;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Data storage service using file system
 * Equivalent to localStorage in the React app
 */
public class DataStorage {
    
    private static final String DATA_DIR = "data";
    private static final String USERS_FILE = "users.txt";
    private static final String CURRENT_USER_FILE = "current_user.txt";
    
    public static void initialize() {
        try {
            Files.createDirectories(Paths.get(DATA_DIR));
        } catch (IOException e) {
            System.err.println("Error creating data directory: " + e.getMessage());
        }
    }
    
    public static List<User> loadUsers() {
        List<User> users = new ArrayList<>();
        Path usersPath = Paths.get(DATA_DIR, USERS_FILE);
        
        if (Files.exists(usersPath)) {
            try {
                List<String> lines = Files.readAllLines(usersPath);
                for (String line : lines) {
                    if (!line.trim().isEmpty()) {
                        String[] parts = line.split("\\|");
                        if (parts.length == 3) {
                            users.add(new User(parts[0], parts[1], parts[2]));
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println("Error reading users file: " + e.getMessage());
            }
        }
        
        return users;
    }
    
    public static void saveUsers(List<User> users) {
        Path usersPath = Paths.get(DATA_DIR, USERS_FILE);
        
        try {
            List<String> lines = new ArrayList<>();
            for (User user : users) {
                lines.add(user.getEmail() + "|" + user.getPassword() + "|" + user.getName());
            }
            Files.write(usersPath, lines);
        } catch (IOException e) {
            System.err.println("Error saving users file: " + e.getMessage());
        }
    }
    
    public static User loadCurrentUser() {
        Path currentUserPath = Paths.get(DATA_DIR, CURRENT_USER_FILE);
        
        if (Files.exists(currentUserPath)) {
            try {
                List<String> lines = Files.readAllLines(currentUserPath);
                if (!lines.isEmpty() && !lines.get(0).trim().isEmpty()) {
                    String[] parts = lines.get(0).split("\\|");
                    if (parts.length >= 2) {
                        return new User(parts[0], "", parts[1]); // Don't store password for current user
                    }
                }
            } catch (IOException e) {
                System.err.println("Error reading current user file: " + e.getMessage());
            }
        }
        
        return null;
    }
    
    public static void saveCurrentUser(User user) {
        Path currentUserPath = Paths.get(DATA_DIR, CURRENT_USER_FILE);
        
        try {
            if (user != null) {
                String userData = user.getEmail() + "|" + user.getName();
                Files.write(currentUserPath, Arrays.asList(userData));
            } else {
                Files.deleteIfExists(currentUserPath);
            }
        } catch (IOException e) {
            System.err.println("Error saving current user file: " + e.getMessage());
        }
    }
    
    public static void clearCurrentUser() {
        saveCurrentUser(null);
    }
}