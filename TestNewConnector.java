package backend;

/**
 * Simple test to verify new DirectMongoDBConnector functionality
 */
public class TestNewConnector {
    
    public static void main(String[] args) {
        System.out.println("=== Testing New DirectMongoDBConnector ===");
        
        // Test with a simple user
        User testUser = new User("testuser@example.com", "password123", "Test User");
        
        System.out.println("Testing user registration...");
        boolean result = DirectMongoDBConnector.storeUserRegistration(testUser);
        
        System.out.println("Registration result: " + result);
        System.out.println("Connection info: " + DirectMongoDBConnector.getConnectionInfo());
        System.out.println("Collection info: " + DirectMongoDBConnector.getCollectionInfo());
    }
}