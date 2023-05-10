Instructions to run the application
1. Clone the repo
2. In application.properties file replace the line shown below with path where the project is cloned

```spring.datasource.url=jdbc:h2:file:/Users/shashikl/Downloads/assessment/assessment/src/main/resources/db/loyaltyrewardsdb```

Replace the above line with 
```spring.datasource.url=jdbc:h2:file:{path of the project on your local}/src/main/resources/db/loyaltyrewardsdb```

3. Once you clone the repo and load the project in IDE, perform below steps to create the customer database
    1. Hit the link http://localhost:8080/h2-console shown in screenshot below
    ![Screenshot 2023-05-10 at 9.27.06 AM.png](..%2F..%2F..%2FDesktop%2FScreenshot%202023-05-10%20at%209.27.06%20AM.png)
    2. In password field provide the password specified in `application.properties` file
    3. Create customer and transaction tables using below DDL

    ```
   CREATE TABLE customers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE
   );

    CREATE TABLE transactions (
        id INT AUTO_INCREMENT PRIMARY KEY,
        customer_id INT NOT NULL,
        transaction_date DATE NOT NULL,
        amount DECIMAL(10, 2) NOT NULL,
        points INT default 0,
        FOREIGN KEY (customer_id) REFERENCES customers(customer_id)
    );
   
   ```
   4. Run sql queries to test if the tables are created successfully.
4. API Endpoints:
    1. Create a customer: POST Request  `http://localhost:8080/customer` 
        
        ```
       Request Body
       {"name": "Bar", "email": "bar@xyz.com"}
    2. Save Transactions: POST Request `http://localhost:8080/transactions/save`
        ```
       Request Body
       [
         {
           "customerId": 1,
           "transactionDate": "2023-05-09 12:34:56",
           "amount": 100.00
         },
         {
           "customerId": 1,
           "transactionDate": "2023-05-01 09:45:23",
           "amount": 75.50
         },
         {
           "customerId": 1,
           "transactionDate": "2023-04-15 14:30:00",
           "amount": 120.25
         },
         {
           "customerId": 1,
           "transactionDate": "2023-04-10 08:15:30",
           "amount": 50.00
         },
         {
           "customerId": 1,
           "transactionDate": "2023-03-28 16:20:15",
           "amount": 85.75
         },
         {
           "customerId": 1,
           "transactionDate": "2023-03-20 13:00:00",
           "amount": 110.00
         },
         {
           "customerId": 1,
           "transactionDate": "2023-03-12 11:15:45",
           "amount": 67.80
         },
         {
           "customerId": 1,
           "transactionDate": "2023-03-05 08:00:00",
           "amount": 40.50
         },
         {
           "customerId": 1,
           "transactionDate": "2023-02-20 15:30:10",
           "amount": 125.75
         },
         {
           "customerId": 1,
           "transactionDate": "2023-02-10 10:05:00",
           "amount": 95.20
         }
       ]
    3. Get Monthly Rewards For Customer, GET `localhost:8080/customer/1/points`
        ```
        Sample Response for above transactions
       {
         "customer": {
           "id": 1,
           "name": "Foo",
           "email": "foo@xyz.com"
         },
         "rewardsList": [
           {
             "month": "FEBRUARY",
             "rewards": 145
           },
           {
             "month": "MARCH",
             "rewards": 122
           },
           {
             "month": "APRIL",
             "rewards": 90
           },
           {
             "month": "MAY",
             "rewards": 75
           }
         ]
       }