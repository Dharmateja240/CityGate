# MongoDB Configuration Guide

## Setting up MongoDB Atlas for TravelExplorer

### 1. Create MongoDB Atlas Account
1. Go to [MongoDB Atlas](https://www.mongodb.com/cloud/atlas)
2. Sign up for a free account
3. Create a new project called "TravelExplorer"

### 2. Create a Cluster
1. Click "Build a Database"
2. Choose "M0 Sandbox" (Free tier)
3. Select your preferred region
4. Name your cluster (e.g., "travelexplorer-cluster")

### 3. Set up Database Access
1. Go to "Database Access" in the left sidebar
2. Click "Add New Database User"
3. Choose "Password" authentication
4. Create username and password
5. Set role to "Atlas admin" for simplicity

### 4. Set up Network Access
1. Go to "Network Access" in the left sidebar
2. Click "Add IP Address"
3. Choose "Allow Access from Anywhere" (0.0.0.0/0) for development
4. Confirm the entry

### 5. Enable Data API
1. Go to "Data API" in the left sidebar
2. Click "Create Data API Key"
3. Copy the API key (you'll need this)
4. Enable the Data API for your cluster

### 6. Configure the Application
1. Open `backend/MongoDBService.java`
2. Replace the following constants:
   ```java
   private static final String API_KEY = "your-actual-api-key-here";
   private static final String CLUSTER_NAME = "your-cluster-name";
   ```

### 7. Database Structure
The application will automatically create:
- Database: `travelexplorer`
- Collection: `users`

User documents will have this structure:
```json
{
  "_id": ObjectId("..."),
  "email": "user@example.com",
  "password": "plaintext-password",
  "name": "User Name"
}
```

### 8. Testing the Connection
The application will automatically test MongoDB connectivity on startup.
If MongoDB is not configured or unavailable, it falls back to local file storage.

### Security Notes
- In production, use proper password hashing (bcrypt)
- Restrict network access to specific IPs
- Use database-specific users with minimal permissions
- Store API keys in environment variables, not source code

### File Storage Fallback
If MongoDB is not configured, the application continues to work with local file storage:
- `data/users.txt` - User accounts
- `data/current_user.txt` - Current logged-in user

This ensures the application works even without MongoDB setup.