# 📌 Project Title

Backend System for a Simple E-Commerce Application

---

## 🎯 Project Description

This project focuses on building a backend for a simple e-commerce platform using **Spring Boot** and **MongoDB**. It provides APIs to manage products, handle shopping carts, process orders, and integrate online payments using **Razorpay**. The project highlights important backend concepts such as RESTful services, data persistence, and webhook-based payment status updates.

---

## 🏗️ Tech Stack

* Java (17 or higher)
* Spring Boot Framework
* Spring Data MongoDB
* MongoDB Database
* Razorpay Payment Integration
* Postman for API testing

---

## ⚙️ Setup Instructions

1. Clone the project repository to your local machine.
2. Install MongoDB and ensure it is running on port **27017**.
3. Open the project in IntelliJ IDEA or any preferred IDE.
4. Add Razorpay API keys in the `application.properties` file.
5. Run the `ECommerceApplication` class.
6. The application will be available at **[http://localhost:8080](http://localhost:8080)**.

---

## 📊 ER Diagram (Logical View)

```
USER ──< CART_ITEM >── PRODUCT
  │
  └──< ORDER ──< ORDER_ITEM >── PRODUCT
          │
          └── PAYMENT
```

---

## 📋 API Documentation

### User APIs

| Method | Endpoint        | Description        |
| ------ | --------------- | ------------------ |
| POST   | /api/users      | Create a new user  |
| GET    | /api/users/{id} | Fetch user details |

---

### Product APIs

| Method | Endpoint                |
| ------ | ----------------------- |
| POST   | /api/products           |
| GET    | /api/products           |
| GET    | /api/products/search?q= |

---

### Cart APIs

| Method | Endpoint                 |
| ------ | ------------------------ |
| POST   | /api/cart/add            |
| GET    | /api/cart/{userId}       |
| DELETE | /api/cart/{userId}/clear |

---

### Order APIs

| Method | Endpoint                     |
| ------ | ---------------------------- |
| POST   | /api/orders                  |
| GET    | /api/orders/{orderId}        |
| GET    | /api/orders/user/{userId}    |
| POST   | /api/orders/{orderId}/cancel |

---

### Payment APIs

| Method | Endpoint              |
| ------ | --------------------- |
| POST   | /api/payments/create  |
| POST   | /api/webhooks/payment |

---

## 🔄 Implementation Flow

1. User account is created.
2. Products are added to the system.
3. User adds products to the cart.
4. Order is generated from the cart.
5. Payment is initiated via Razorpay.
6. Webhook updates order status after successful payment.

---

## ✅ Conclusion

This backend project demonstrates how different e-commerce components interact with each other, starting from cart management to order processing and payment completion, using REST APIs and webhook callbacks.

