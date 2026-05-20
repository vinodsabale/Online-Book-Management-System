# 📚 Online Book Management System

A web-based Online Book Management System developed using Spring Boot, Thymeleaf, Spring Security, Hibernate/JPA, and MySQL.

This project allows librarians and members to manage books, issue/return books, and track overdue records with secure role-based authentication.

# 🚀 Features

## 👨‍💼 Librarian Features
- Add new books
- Update book details
- Delete books
- View all books
- Issue books to members
- Return books
- Track overdue books
- Manage members

## 👤 Member Features
- Login/Register
- View available books
- Search books
- View issued books
- Return books

## 🔐 Security Features
- Spring Security authentication
- Role-based authorization
- Secure login/logout
- Password encryption

## 🌐 REST API Features
- RESTful APIs
- Swagger API documentation
- JSON responses
- CRUD operations


# 🛠️ Technologies Used

| Technology | Description |
|------------|-------------|
| Java | Programming Language |
| Spring Boot | Backend Framework |
| Spring MVC | Web Layer |
| Spring Security | Authentication & Authorization |
| Spring Data JPA | Database Operations |
| Hibernate | ORM Framework |
| Thymeleaf | Frontend Template Engine |
| MySQL | Database |
| Maven | Build Tool |
| Swagger/OpenAPI | API Documentation |


# 📂 Project Structure
text
src
 ├── main
 │   ├── java
 │   │   └── com
 │   │       ├── controller
 │   │       ├── service
 │   │       ├── repository
 │   │       ├── entity
 │   │       ├── config
 │   │       └── security
 │   └── resources
 │       ├── templates
 │       ├── static
 │       └── application.properties
 └── test

# ⚙️ Installation & Setup

## 1️⃣ Clone Repository

command
git clone https://github.com/vinodsabale/Online-Book-Management-System.git


## 2️⃣ Open Project

Open the project in:
- STS (Spring Tool Suite)
- IntelliJ IDEA
- Eclipse

## 3️⃣ Configure MySQL Database

Create database:

sql
CREATE DATABASE book_management;


Update `application.properties`:

properties
spring.datasource.url=jdbc:mysql://localhost:3306/book_management
spring.datasource.username=root
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true


## 4️⃣ Run Application

bash
mvn spring-boot:run


OR run:

text
OnlineBookManagementSystemApplication.java

# 🌍 Application URLs

## Main Application
text
http://localhost:8080


## Swagger API Documentation
text
http://localhost:8080/swagger-ui/index.html


# 📸 Screenshots


Example:

md
![Home Page](screenshots/home.png)

# 🔮 Future Enhancements

- Email notifications
- Fine calculation
- PDF report generation
- Book reservation system
- Admin dashboard analytics
- Docker deployment


# 👨‍💻 Author

## Vinod Sabale

GitHub:
https://github.com/vinodsabale



# 📜 License

This project is developed for learning and educational purposes.
