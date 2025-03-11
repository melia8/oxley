## Oxley Code Test

### Build and Test

To build project and artifacts and runs all tests (unit and integration)
- ```./mvnw clean install ``` 

To run unit tests
- ```./mvnw test```

To run integration tests
- ```./mvnw verify```

To start service and corresponding Postgres container
- ``` ./mvnw spring-boot:run```


### Using API

API reacheable at http://localhost:8080/transactions

#### POST
```POST http://localhost:8080/transactions```

example body
```
{ 
    "manufacturer": "Coca Cola",
    "retailer_id": "Tesco",
    "product_code": "A1236",
    "transaction_id": "e1c7343e-101e-42ff-b26c-c9e2d441a351",
    "transaction_date": "2024-01-01",
    "quantity": 123.3456,
    "value": 446.12 
}
```


#### GET
  
Bring back all transactions

```GET http://localhost:8080/transactions```
      
Search transactions

```GET http://localhost:8080/transactions/search```

Example payloads
``` 
{ 
    "manufacturer": "Coca Cola",
    "retailer_id": "Tesco",
    "product_list": ["A1235", "A1236"],
    "start_date": "2010-10-10",
    "end_date": "2025-03-03",
    "sum": "VALUE"
} 
```

```
{ 
    "manufacturer": "Coca Cola",
    "retailer_id": "Tesco",
    "product_list": ["A1235", "A1236"],
    "start_date": "2010-10-10",
    "end_date": "2025-03-03",
    "sum": "QUANTITY"
}
```

#### Delete
 ```DELETE http://localhost:8080/transactions```

Need to provide the following bearer token in the Authorization header of the request
```
Authorization : Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiJ9.U5uekccSES74PO9gGPdHUJLyjf9qIG0mRtQ6IPx1hDM
```


