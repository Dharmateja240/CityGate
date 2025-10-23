package backend;

import java.io.*;

/**
 * Verification script to check current data state
 */
public class VerifyDataState {
    
    public static void main(String[] args) {
        System.out.println("=== Data State Verification ===");
        
        // Check local files
        checkLocalFiles();
        
        // Check old MongoDB collection
        checkOldMongoDBCollection();
        
        // Check new MongoDB collection
        checkNewMongoDBCollection();
        
        // Test user registration with new system
        testUserRegistration();
        
        System.out.println("\n=== Verification Complete ===");
    }
    
    private static void checkLocalFiles() {
        System.out.println("\n--- Local File Status ---");
        
        String[] files = {
            "data/users.txt",
            "data/current_user.txt",
            "../data/users.txt", 
            "../data/current_user.txt"
        };
        
        for (String filePath : files) {
            File file = new File(filePath);
            if (file.exists()) {
                try {
                    long size = file.length();
                    System.out.println(filePath + ": " + size + " bytes" + (size == 0 ? " (CLEARED)" : ""));
                } catch (Exception e) {
                    System.out.println(filePath + ": Error reading - " + e.getMessage());
                }
            } else {
                System.out.println(filePath + ": Does not exist");
            }
        }
    }
    
    private static void checkOldMongoDBCollection() {
        System.out.println("\n--- Old MongoDB Collection (crudDB.tasks) ---");
        
        String script = 
            "use crudDB\n" +
            "print('Database exists:', db.getName())\n" +
            "print('Total documents in tasks:', db.tasks.countDocuments({}))\n" +
            "print('User registrations in tasks:', db.tasks.countDocuments({type: 'user_registration'}))\n";
        
        executeMongoScript(script, "check_old_db");
    }
    
    private static void checkNewMongoDBCollection() {
        System.out.println("\n--- New MongoDB Collection (userdetail.userdetail) ---");
        
        String script = 
            "use userdetail\n" +
            "print('Database exists:', db.getName())\n" +
            "print('Total documents in userdetail:', db.userdetail.countDocuments({}))\n" +
            "print('User registrations in userdetail:', db.userdetail.countDocuments({type: 'user_registration'}))\n" +
            "print('Recent registrations:')\n" +
            "db.userdetail.find({type: 'user_registration'}).sort({registeredAt: -1}).limit(3).forEach(printjson)\n";
        
        executeMongoScript(script, "check_new_db");
    }
    
    private static void testUserRegistration() {
        System.out.println("\n--- Testing User Registration ---");
        
        User testUser = new User("verify@example.com", "verify123", "Verification User");
        
        System.out.println("Testing DirectMongoDBConnector...");
        boolean success = DirectMongoDBConnector.storeUserRegistration(testUser);
        
        if (success) {
            System.out.println("✅ User registration test successful");
            
            // Verify it was stored
            String verifyScript = 
                "use userdetail\n" +
                "print('Latest registration:')\n" +
                "db.userdetail.find({email: 'verify@example.com'}).forEach(printjson)\n";
            
            executeMongoScript(verifyScript, "verify_registration");
        } else {
            System.err.println("❌ User registration test failed");
        }
        
        System.out.println("\nTesting connection info:");
        System.out.println("Connection: " + DirectMongoDBConnector.getConnectionInfo());
        System.out.println("Collection: " + DirectMongoDBConnector.getCollectionInfo());
    }
    
    private static void executeMongoScript(String script, String scriptName) {
        try {
            File tempScript = File.createTempFile(scriptName, ".js");
            try (FileWriter writer = new FileWriter(tempScript)) {
                writer.write(script);
            }
            
            String[] commands = {
                "mongosh\\\\bin\\\\mongosh.exe --quiet " + tempScript.getAbsolutePath(),
                "mongosh --quiet " + tempScript.getAbsolutePath(),
                "mongo --quiet " + tempScript.getAbsolutePath()
            };
            
            for (String command : commands) {
                try {
                    Process process = Runtime.getRuntime().exec(command);
                    
                    // Read output
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            System.out.println("  " + line);
                        }
                    }
                    
                    // Read errors
                    try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                        String line;
                        while ((line = errorReader.readLine()) != null) {
                            System.err.println("  ERROR: " + line);
                        }
                    }
                    
                    int exitCode = process.waitFor();
                    if (exitCode == 0) {
                        tempScript.delete();
                        return; // Success
                    }
                } catch (Exception e) {
                    System.err.println("Command failed: " + e.getMessage());
                }
            }
            
            tempScript.delete();
            System.err.println("❌ All MongoDB commands failed for " + scriptName);
            
        } catch (Exception e) {
            System.err.println("Error executing " + scriptName + ": " + e.getMessage());
        }
    }
}