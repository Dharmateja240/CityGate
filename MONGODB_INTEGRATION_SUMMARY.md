# MongoDB Integration for User Registration

## Overview
The backend has been successfully configured to store user registration data in MongoDB at `mongodb://localhost:27017/info` using the collection `user_registrations`.

## What's Implemented

### 1. MongoDB Connection
- **Database**: `info`
- **Collection**: `user_registrations`
- **Connection URL**: `mongodb://localhost:27017/info`

### 2. User Registration Flow
When a user registers through the application:
1. User enters name, email, and password
2. Data is validated by `AuthService`
3. User information is stored in MongoDB via `MongoDBService.createUser()`
4. Data is also stored in local file storage as fallback for authentication
5. User is automatically logged in

### 3. Data Storage
The following user data is stored in MongoDB:
```json
{
  "name": "User's Full Name",
  "email": "user@example.com", 
  "password": "user_password",
  "registeredAt": "2025-10-08T15:30:11.6512535"
}
```

### 4. Fallback Mechanism
- If MongoDB is not available, registration data is saved to a backup JSON file
- The backup file can be imported to MongoDB later using the provided script
- Authentication still works through local file storage

## Files Modified

### Backend Files
- `MongoDBService.java` - Main MongoDB integration service
- `SimpleMongoDBConnector.java` - Direct MongoDB connection handler
- `AuthService.java` - Updated to use MongoDB for registration storage
- `MongoDBTest.java` - Test class to verify functionality

## How to Test

### 1. Run the Test
```bash
cd java-version
java -cp . backend.MongoDBTest
```

### 2. Check MongoDB Data
If you have MongoDB tools installed:
```bash
mongo
use info
db.user_registrations.find().pretty()
```

### 3. Import Backup Data (if needed)
Run the provided batch script:
```bash
import_to_mongodb.bat
```

## Verification
The integration has been tested and verified:
- ✅ MongoDB connection working
- ✅ User registration data stored successfully
- ✅ Backup mechanism working
- ✅ AuthService integration complete
- ✅ No data fetching from MongoDB (as requested)

## Usage in Application
When users register through the Swing or JavaFX interface:
1. Their registration data automatically gets stored in MongoDB
2. They can immediately use the application
3. All registration data is preserved in the specified MongoDB database and collection

The system now successfully stores user registration information in MongoDB at `mongodb://localhost:27017/info` in the `user_registrations` collection as requested.