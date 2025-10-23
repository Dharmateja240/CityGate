package backend;

import java.io.*;

/**
 * Simple MongoDB verification using mongosh
 */
public class VerifyMongoDB {
    
    public static void main(String[] args) {
        System.out.println("=== MongoDB Database Verification ===");
        
        try {
            // Check collections in info database
            System.out.println("1. Checking collections in 'info' database...");
            executeMongoCommand("use info; show collections");
            
            // Count documents in user_registrations
            System.out.println("\n2. Counting documents in user_registrations...");
            executeMongoCommand("use info; db.user_registrations.countDocuments()");
            
            // Show first few documents
            System.out.println("\n3. Showing sample documents...");
            executeMongoCommand("use info; db.user_registrations.find().limit(3).forEach(printjson)");
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
        
        System.out.println("\n=== Verification Complete ===");
    }
    
    private static void executeMongoCommand(String command) {
        try {
            String mongoshPath = "mongosh\\\\bin\\\\mongosh.exe";
            String fullCommand = mongoshPath + " --quiet --eval \"" + command + "\"";
            
            System.out.println("Executing: " + command);
            
            Process process = Runtime.getRuntime().exec(fullCommand);
            
            // Read output
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            boolean foundOutput = false;
            while ((line = reader.readLine()) != null) {
                if (line.trim().length() > 0) {
                    System.out.println("Result: " + line);
                    foundOutput = true;
                }
            }
            
            // Read error output
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            while ((line = errorReader.readLine()) != null) {
                if (line.trim().length() > 0) {
                    System.err.println("Error: " + line);
                }
            }
            
            int exitCode = process.waitFor();
            if (exitCode == 0 && !foundOutput) {
                System.out.println("Command executed successfully (no output)");
            } else if (exitCode != 0) {
                System.err.println("Command failed with exit code: " + exitCode);
            }
            
        } catch (Exception e) {
            System.err.println("Failed to execute command: " + e.getMessage());
        }
    }
}