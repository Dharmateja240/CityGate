use crudDB
db.tasks.find().limit(5).forEach(printjson)
print("Total documents in tasks collection: " + db.tasks.countDocuments({}))
print("User registration documents: " + db.tasks.countDocuments({type: "user_registration"}))