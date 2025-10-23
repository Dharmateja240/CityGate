package backend;

import java.util.List;

/**
 * Authentication service
 * Equivalent to the AuthContext in React
 */
public class AuthService {
    private User currentUser;
    
    public AuthService() {
        this.currentUser = DataStorage.loadCurrentUser();
    }
    
    public User getCurrentUser() {
        return currentUser;
    }
    
    public boolean login(String email, String password) throws AuthException {
    
        User user = MongoDBService.findUserByEmail(email);
        if (user == null) {
           
            List<User> users = DataStorage.loadUsers();
            for (User fileUser : users) {
                if (fileUser.getEmail().equals(email) && fileUser.getPassword().equals(password)) {
                    user = fileUser;
                    break;
                }
            }
        }
        
        if (user != null && user.getPassword().equals(password)) {
            this.currentUser = user;
            DataStorage.saveCurrentUser(user);
            return true;
        }
        
        throw new AuthException("Invalid credentials");
    }
    
    public boolean signup(String email, String password, String name) throws AuthException {
        
        if (email == null || email.trim().isEmpty()) {
            throw new AuthException("Email is required");
        }
        if (password == null || password.length() < 6) {
            throw new AuthException("Password must be at least 6 characters");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new AuthException("Name is required");
        }
        
        
        List<User> users = DataStorage.loadUsers();
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                throw new AuthException("User already exists");
            }
        }
        
        // Create new user
        User newUser = new User(email.trim(), password, name.trim());
        
        // Store in MongoDB for registration data (primary requirement)
        boolean savedInMongo = MongoDBService.createUser(newUser);
        if (savedInMongo) {
            System.out.println("User registration data stored in MongoDB: " + MongoDBService.getCollectionInfo());
        } else {
            System.out.println("MongoDB storage failed, but continuing with file storage");
        }
        
        // Also save in file storage for authentication purposes
        users.add(newUser);
        DataStorage.saveUsers(users);
        
        // Auto-login
        this.currentUser = newUser;
        DataStorage.saveCurrentUser(newUser);
        
        return true;
    }
    
    public void logout() {
        this.currentUser = null;
        DataStorage.clearCurrentUser();
    }
    
    public boolean isLoggedIn() {
        return currentUser != null;
    }
}