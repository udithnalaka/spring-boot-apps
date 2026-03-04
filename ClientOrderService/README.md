# Spring Boot app  - Client Order Service

A simple spring application to act as the client service to call another REST service 
"CustomerOrders" (GIT repo: https://github.com/udithnalaka/spring-boot-apps/tree/master/CustomerOrders)


### Technologies used

* SpringBoot 3.5
* Java 21
  * CompletableFuture
* RestClient
* Resilience4j
  * Circuit breaker
  * Retry
  * Timeout
  * Fallback
* Swagger

### Run the application

1. run the downstream service (CustomerOrders)
   
   refer to the readme.md file (https://github.com/udithnalaka/spring-boot-apps/blob/master/CustomerOrders/README.md)


2. create some dummy data in the database

   ![img_2.png](img_2.png)

   ![img_4.png](img_4.png)


3. build and run the client application (this app)

       mvn clean install
       mvn spring-boot:run


4. from Swagger, access the endpoint and should be able to get the response with the two orders available in the downstream service.

    ![img_3.png](img_3.png)


### Resilience4j configuration

 1) application.yml: below configuration is for resilience4j setup

    ![img_5.png](img_5.png)

 2) Service class (ClientOrderServiceImpl)

    ![img_7.png](img_7.png)

### Test scenarios

   * **Scenario 1**: downstream system is down


   When the downstream system is unavailable, the circuit braker should be called according to the configured values.
   The fallback method will be called and a response will be sent accordingly.

   ![img_8.png](img_8.png)

   Error in logs

    2026-03-04T21:55:59.669+10:00 DEBUG 30800 --- [ClientOrderService] [onPool-worker-1] i.g.r.c.i.CircuitBreakerStateMachine     
    : Event ERROR published: 2026-03-04T21:55:59.669999400+10:00[Australia/]: CircuitBreaker 'orderService' recorded an error: 
    'org.springframework.web.client.ResourceAccessException: I/O error on GET request for "http://localhost:8080/orders/customer/1": 
    Connect to http://localhost:8080 failed: Connection refused: getsockopt'. Elapsed time: 141 ms

