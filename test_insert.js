use userdetail
print('Starting test insertion...')
print('Database:', db.getName())
print('Available collections before:', db.getCollectionNames())

try {
    var result = db.userdetail.insertOne({
        "type": "user_registration",
        "name": "Test User",
        "email": "test@example.com", 
        "password": "testpass123",
        "registeredAt": "2025-10-08T19:00:00"
    })
    print('Insert result:', JSON.stringify(result))
    print('Insert successful:', result.acknowledged)
} catch (error) {
    print('Insert error:', error)
}

print('Available collections after:', db.getCollectionNames())
print('Documents in userdetail:', db.userdetail.countDocuments({}))
print('Documents in userdetails:', db.userdetails.countDocuments({}))

print('All documents in userdetail:')
db.userdetail.find().forEach(printjson)

print('All documents in userdetails:')
db.userdetails.find().forEach(printjson)