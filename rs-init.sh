#!/bin/bash

mongosh <<EOF
try {
  rs.status();
  console.log("Successfully configured replica set");
}
catch (err) {
  console.error("An error occurred. Error message:" + err.message);

  rs.initiate(
    {
        "_id": "dbrs",
        "version": 1,
        "members": [
            { "_id": 1, "host": "mongo1:27017", "priority": 1 },
            { "_id": 2, "host": "mongo2:27017", "priority": 0.5 },
            { "_id": 3, "host": "mongo3:27017", "priority": 0.5 }
        ]
    }
  );
}
EOF