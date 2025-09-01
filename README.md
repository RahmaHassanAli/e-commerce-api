# E-Commerce API

A simple **CRUD-based E-Commerce REST API** built with **Java Spring Boot** and **PostgreSQL**.  
This project was developed as part of a task to demonstrate backend API development, hosting, and database integration.

---

## 🚀 Features
- User management
- Product management (CRUD)
- Cart functionality
- Order checkout flow
- PostgreSQL database integration
- Exception handling

---

## 📂 Project Structure
.
ecommerce-api/
├── src/main/java/com/cgc/e_commerce/
│   ├── controller/          # REST Controllers
│   ├── service/            # Business Logic Layer
│   ├── repository/         # Data Access Layer
│   ├── model/              # Entity Classes
│   ├── dto/                # Data Transfer Objects
│   ├── exception/          # Custom Exception Classes
│   └── ECommerceApplication.java
├── src/main/resources/
│   ├── application.properties
│   └── application-prod.properties
├── db/                     # SQL Scripts
│   └── schema.sql         # Database schema
├── postman/               # API Testing
│   └── E-Commerce-API.postman_collection.json
├── Dockerfile             # Container configuration
├── pom.xml               # Maven dependencies
└── README.md

Postman Collection

A Postman collection is included to test the APIs:
