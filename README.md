# Synpulse8 Project

## _e-Banking Protal Application_

It's a backend microservice system that providing a reusable REST API for returning the paginated list of money account
transactions created in an arbitrary calendar month for a given customer who is logged-on in the portal. For each
transaction 'page' return the total credit and debit values at the current exchange rate.
[Reference.](https://challenges.synpulse8.com/backend-engineer/)

[CircleCI Pipeline](https://app.circleci.com/pipelines/circleci/MRBmi5go3TNZnWzFBFHiXV)

## Table of Contents

- [Requirement Implementation Idea](#requirement-implementation-idea)
- [Project Structure](#project-structure)
- [How to Play?](#how-to-play)
- [Reference](#reference)

## Requirement Implementation Idea

The microservice architecture can be constructed as auth service and transaction service independently. Auth service
provider sign up and sign in. Sign up is a simulation function for creating client's account. Sign in function will
issue JWT. Then client uses this JWT to get their accounts' transaction history record. In Transaction service, we also
provide simulation program to generate fake transaction history record. There is a kafka consumer listener to consume
the data and save to database. So, e-banking application subscribes a kafka topic. For pagination search, we need a
database to do that, the reason I choose RDBMS is that the relationship between client, account and transaction is
strong. Therefore, I define three business entities, CLIENT, ACCOUNT and TRANSACTION. Then we can define relationship
below.

* Relationship:
    * CLIENT 1 to n ACCOUNT
    * ACCOUNT 1 to n TRANSACTION

When simulate sign up function, system will create a new client and account. Due to one client can have many account
with different currency, when you sign up again with different currency, new account will be connected the origin
client. Every transaction record got from kafka topic will also be associated with specific account. Remember, uid and
currency can group an account. Each client has unique uid. Each transaction is also composed of uid and currency. That's
why transaction can be associated with account. For the data access efficient, we use redis to cache exchange rate from
external API service. The cache expiry period is one day because that the third party exchange rate API service will
only update the data every day. So, we can avoid the round trip in outside network. in the future, we can also set up
hibernate second level cache. Using redisson as the cache provider, we can achieve distributed cache in multiple
instance environment. In k8s, we deploy auth and transaction service a load balancer. In the future, we can put a
nginx-ingress in front of the backend services. I use logback to config the logs of these two modules, in transaction
service, I also use MDC to record the uid in log file for tracking convenient. In Spring security part, I use JWT. Each
time the request will be checked the JWT. When the token is valid. There will be an authorized principle. In the Spring
context, we can get the principle during request life cycle. At transaction history record API, I use the principle to
get Client's uid. So, it can make sure the transaction records are only the own can access. Finally, I also enable
authorize function on OpenAPI Swagger UI.

## Project Structure

* The project is separated into three modules, two web service modules, one common library module.
* Web service includes auth service and transaction service.
    * Auth service provides authentication and authorization.
    * Transaction service providers account transaction history record.
* In kube dir, you can find k8s related config files including kafka, mysql, redis and our app.
* In monitoring dir, you can find monitoring tool including prometheus and grafana.
* In local dir, you can find all supported tools for local env develop including kafka, mysql and redis.

````   
    /synpulse8
    │──/.circleci 
    │  └── config.yml  # CircleCi config file
    │── ebanking-auth-module  # auth web service 
    │   ├── /logs  # system logs
    │   ├── /src
    │   │   ├── /main
    │   │   │   ├── /java
    │   │   │   │   └── /com/synpulse8/ebanking
    │   │   │   │       ├── /aspect # AOP 
    │   │   │   │       │   └── LoggingAspect.java # log aspect
    │   │   │   │       ├── /auth
    │   │   │   │       │   ├── /controllers  # auth controller
    │   │   │   │       │   │   ├── AuthController.java  # auth controller interface 
    │   │   │   │       │   │   └── AuthControllerImpl.java  # auth controller interface implimentation
    │   │   │   │       │   ├── /dto # Data trasnfer object
    │   │   │   │       │   │   ├── LoginReq.java  # login request obejct
    │   │   │   │       │   │   └── LoginRes.java  # login response obejct
    │   │   │   │       │   └── /services # auth service
    │   │   │   │       │       ├── AuthService.java  # auth service interface
    │   │   │   │       │       └── AuthServiceImpl.java  # auth service interface implimentation
    │   │   │   │       ├── /config  # auth web service config
    │   │   │   │       │   ├── MetricsConfig.java  # Springboot prometheus registry
    │   │   │   │       │   ├── OpenAPIConfig.java  # Open API swagger config 
    │   │   │   │       │   ├── SecurityConfig.java  # Spring security config  
    │   │   │   │       │   └── UpperTableStrategy.java  # Table auto create table naming strategy
    │   │   │   │       ├── /test/auth  # realted auth simulation for test
    │   │   │   │       │   ├── / dto
    │   │   │   │       │   │   ├── CreateAccountReq.java  # create account request
    │   │   │   │       │   │   └── CreateAccountRes.java  # create account response
    │   │   │   │       │   └── AccountController.java  # auth simulation controller
    │   │   │   │       └── AuthApplication.java  # auth app entry point
    │   │   │   └── /resources
    │   │   │       ├── application.properties # springboot environment variable
    │   │   │       └── logback.xml  # logback config file 
    │   │   └── /test  # test code
    │   │       └── /java
    │   │           └── /com/synpulse8/ebanking/auth
    │   │               ├── /controllers  # controller tests 
    │   │               │   └── AuthControllerImplUnitTest.java
    │   │               └── /services  # service tests
    │   │                   └── AuthControllerImplUnitTest.java
    │   ├── Dockerfile  # dockerize config file
    │   └── pom.xml
    │── ebanking-common-module  # common library
    │   ├── /src
    │   │   ├── /main
    │   │   │   ├── /java
    │   │   │   │   └── /com/synpulse8/ebanking
    │   │   │   │       ├── /adivce
    │   │   │   │       │   └── RestControllerExceptionHandler.java  # @RestController gloabl exception handler
    │   │   │   │       ├── /dao  # data access object layer
    │   │   │   │       │   ├── /account  
    │   │   │   │       │   │   ├── /entity
    │   │   │   │       │   │   │   └── Account.java  # account business model
    │   │   │   │       │   │   └── /repo
    │   │   │   │       │   │       └── AccountRepository.java  # account jpa repository
    │   │   │   │       │   ├── /client  
    │   │   │   │       │   │   ├── /entity
    │   │   │   │       │   │   │   └── Client.java  # Client business model
    │   │   │   │       │   │   └── /repo
    │   │   │   │       │   │       └── ClientRepository.java  # Client jpa repository
    │   │   │   │       │   └── /transaction  
    │   │   │   │       │       ├── /entity
    │   │   │   │       │       │   └── Transaction.java  # transaction business model
    │   │   │   │       │       └── /repo
    │   │   │   │       │           └── TransactionRepository.java  # transaction jpa repository
    │   │   │   │       ├── /enums  # system level constant
    │   │   │   │       │   ├── BalanceChange.java
    │   │   │   │       │   ├── Currency.java
    │   │   │   │       │   └── Status.java
    │   │   │   │       ├── /exceptions  # system level exceptions 
    │   │   │   │       │   ├── /base  # abstract exception prototype
    │   │   │   │       │   │   ├── SynpulseSystemException.java 
    │   │   │   │       │   │   └── SynpulseSystemRuntimeException.java
    │   │   │   │       │   ├── ExchangeRateAPIRuntimeException.java
    │   │   │   │       │   └── UserDataNotFoundRuntimeException.java
    │   │   │   │       ├── /response/dto/  # system level response dto 
    │   │   │   │       │   ├── ErrorDto.java
    │   │   │   │       │   └── ResponseDto.java
    │   │   │   │       └── /security  # sprint security related 
    │   │   │   │           ├── EbankingPrincipal.java
    │   │   │   │           ├── EbankingUserDetailsService.java
    │   │   │   │           ├── JwtAuthorizationFilter.java
    │   │   │   │           ├── JwtUtils.java
    │   │   │   │           └── PrincipleUtils.java
    │   │   │   └── resources
    │   │   └── /test
    │   └── pom/.xml
    │──ebanking-transaction-module  # transaction web service
    │   ├── /logs  # system logs 
    │   ├── /src
    │   │   ├── /main
    │   │   │   ├── /java
    │   │   │   │   └── /com/synpulse8/ebanking
    │   │   │   │       ├── /aspect # AOP 
    │   │   │   │       │   └── LoggingAspect.java # log aspect
    │   │   │   │       ├── /config  # auth web service config
    │   │   │   │       │   ├── AppConfig.java  # app config file
    │   │   │   │       │   ├── MetricsConfig.java  # Springboot prometheus registry
    │   │   │   │       │   ├── OpenAPIConfig.java  # Open API swagger config
    │   │   │   │       │   ├── SecurityConfig.java  # Spring security config  
    │   │   │   │       │   └── UpperTableStrategy.java  # Table auto create table naming strategy
    │   │   │   │       ├── /filter  # request filter 
    │   │   │   │       │   └── MDCFilter.java
    │   │   │   │       ├── /test/kafka  # realted kafka simulation for test
    │   │   │   │       │   ├── / dto
    │   │   │   │       │   │   └── TransactionRecord.java  # transaction kafka message format
    │   │   │   │       │   ├── KafkaController.java  # create transaction controller
    │   │   │   │       │   └── KafkaProducerService.java  # send transaction message to kafka topic
    │   │   │   │       ├── /transaction
    │   │   │   │       │   ├── /controllers  # transaction controller
    │   │   │   │       │   │   ├── TransactionController.java  # transaction controller interface 
    │   │   │   │       │   │   └── TransactionControllerImpl.java  # transaction controller interface implimentation
    │   │   │   │       │   ├── /dto # Data trasnfer object
    │   │   │   │       │   │   ├── TransactionDto.java  # transaction data transfer obejct
    │   │   │   │       │   │   ├── TransactionListRes.java  # transaction search list response obejct
    │   │   │   │       │   │   └── TransactionSearchDto.java  # transaction search list request dto
    │   │   │   │       │   ├── /listener
    │   │   │   │       │   │   └── TransactionRecordListener.java  # kafka consumer listener for transaction record
    │   │   │   │       │   └── /services # transaction service
    │   │   │   │       │       ├── /dto
    │   │   │   │       │       │   └── ExchangeRateResponse.java
    │   │   │   │       │       ├── ExchangeRateService.java  # transaction service interface
    │   │   │   │       │       ├── ExchangeRateServiceImpl.java  # transaction service interface implimentation
    │   │   │   │       │       ├── TransactionService.java  # transaction service interface
    │   │   │   │       │       ├── TransactionServiceImpl.java  # transaction service interface implimentation
    │   │   │   │       │       └── TransactionSpecifications.java  # transaction dynamic search specifications
    │   │   │   │       └── TransactionApplication.java  # transaction app entry point
    │   │   │   └── /resources
    │   │   │       ├── application.properties # springboot environment variable
    │   │   │       ├── logback.xml  # logback config file 
    │   │   │       ├── redisson-local.yaml  # redission local env config file
    │   │   │       └── src/main/resources/redisson-stage.yaml  # redission stage env config file
    │   │   └── /test  # test code
    │   │       └── /java
    │   │           └── /com/synpulse8/ebanking/transaction
    │   │               ├── /controllers  # controller tests 
    │   │               │   └── TransactionControllerImplUnitTest.java
    │   │               └── /services  # service tests
    │   │                   ├── ExchangeRateServiceImplUnitTest.java
    │   │                   └── TransactionServiceImplUnitTest.java
    │   ├── Dockerfile  # dockerize config file
    │   └── pom.xml
    │── /kube
    │   ├── /ebanking
    │   │   ├── ebanking-auth-deployment.yaml
    │   │   ├── ebanking-transaction-deployment.yaml
    │   │   ├── exchange-rate-secrets.yaml
    │   │   └── jwt-secrets.yaml
    │   ├── /kafka
    │   │   └── kafka-configmap.yaml
    │   ├── /mysql
    │   │   ├── mysql-configmap.yaml
    │   │   ├── mysql-deployment.yaml
    │   │   └── mysql-secrets.yaml
    │   └── /redis
    │       ├── redis-configmap.yaml
    │       └── kube/redis/redis-deployment.yaml
    ├── /local  # for local development docker images
    │   └── /database
    │       └── docker-compose.yml  # local env database including mysql, kafka, redis
    ├── /monitoring
    │   ├── datasources.yml  # for grafna to config datasource
    │   ├── docker-compose.yml  # local env prometheus and grafana 
    │   └── prometheus.yml  # for prometheus config file
    ├── pom.xml # parent pom.xml for module managment
    ├── .gitignore # Git ignore file
    └── README.md # Project documentation
````

## How to Play?

### local env

    # run monitoring service
    cd monitoring
    docker-compose up -d

    # run database service
    cd local/database
    docker-compose up -d
    
    # run e-banking service
    mvn clean package
    mvn spring-boot:run

![image](https://drive.google.com/uc?export=view&id=1qUxnUfz6bF_hnMjfqPmsFt4CtEfCaYWD)

### stage env

```
kubectl create namespace synpulse8
kubectl apply -f ./kube/mysql/mysql-configmap.yaml --namespace=synpulse8
kubectl apply -f ./kube/mysql/mysql-secrets.yaml --namespace=synpulse8
kubectl apply -f ./kube/mysql/mysql-deployment.yaml --namespace=synpulse8 
kubectl apply -f ./kube/kafka/kafka-configmap.yaml --namespace=synpulse8
kubectl create -f 'https://strimzi.io/install/latest?namespace=synpulse8' -n synpulse8
kubectl apply -f https://strimzi.io/examples/latest/kafka/kafka-persistent-single.yaml -n synpulse8
kubectl apply -f ./kube/redis/redis-configmap.yaml --namespace=synpulse8
kubectl apply -f ./kube/redis/redis-deployment.yaml --namespace=synpulse8
kubectl apply -f ./kube/ebanking/exchange-rate-secrets.yaml --namespace=synpulse8
kubectl apply -f ./kube/ebanking/jwt-secrets.yaml --namespace=synpulse8
kubectl apply -f ./kube/ebanking/ebanking-auth-deployment.yaml --namespace=synpulse8
kubectl apply -f ./kube/ebanking/ebanking-transaction-deployment.yaml --namespace=synpulse8

# Prometheus and Grafana portal use local docker. 
```

![image](https://drive.google.com/uc?export=view&id=1pPQzjZb4L2skp3hployYMcfmM7UzUEau)

### Browser URL

* Auth API Service Swagger

```
http://localhost:8081/ebanking/swagger-ui/index.html
```

![image](https://drive.google.com/uc?export=view&id=19VJCQOAy3HmBK6OcvVaA4BOTMB3yb8EF)

* Transaction API Service Swagger

```
http://localhost:8082/ebanking/swagger-ui/index.html
```

![image](https://drive.google.com/uc?export=view&id=1fOYH4TZf53akdICS1_cTBv9MZnjzVexf)

* Prometheus portal

```
http://localhost:9090/targets
```

![image](https://drive.google.com/uc?export=view&id=1GK4EP99jnX7zB2o1DPpfq5u3ZfWBAxxs)

* Grafana portal

```
http://localhost:3000/d/spring_boot_21/spring-boot-3-x-statistics?orgId=1
```

![image](https://drive.google.com/uc?export=view&id=1hRmYo2Wm9fnspV-ohZs4G-nkszYQXkbv)

## Reference
