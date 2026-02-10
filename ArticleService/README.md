# Getting Started

## SpringBoot Application - Article Service

* SpringBoot 3.5.0
* Java 21
* Junit5 / Mockito
* Spring JPA
* H2 in memory database
* Global Exception handling with @RestControllerAdvice


## SQL 
* Refer to /resources/db/transactions.sql file for SQL solution for the transactions
   related question.


### run application
    mvn clean install -e
    mvn spring-boot:run

### test application

once application is running, a database named 'article' will be created with two tables.
Article and Tag. Tables will be empty as each app start will create a fresh copy of the database.

**Note:** I have used postman to save Article endpoint to insert to database and used the get endpoint to verify.
Postman screenshots attached below.

log into h2 console to verify. url: http://localhost:8080/h2-console/

![img.png](img.png)

#### Postman requests

* save article
![img_1.png](img_1.png)

* get article by id
![img_2.png](img_2.png)

* get article by title
![img_3.png](img_3.png)





