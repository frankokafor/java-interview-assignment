# java-interview-assignment
Java Backend Developer Interview

#### ui Link
The link to the user interface where these functionalities
localhost:8089/api/v1/

NB: The client is a simple thymeleaf model and view template

The app can be cloned ran locally and tested with postman too.

Example request to register a new card and get the details.
- localhost:8089/api/v1/card-scheme/verify/41874518

response: 
```json
{
    "success": true,
    "payload": {
        "scheme": "visa",
        "type": "debit",
        "bank": "Access"
    }
}
```
to get statistics of all saved cards
- localhost:8080/api/v1/card-scheme/stats?start=1&limit=3
default param = start=0, limit=25
```json
{
    "success": true,
    "start": Number,
    "limit": Number,
    "size": Number,
    "payload": {
      "string" : Integer (or null)
      }
```

Thank you for reviewing my application.

###### Frank Okafor
