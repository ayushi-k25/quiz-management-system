# 🎯 Quiz Management System

A Java-based desktop application designed to manage quizzes efficiently with separate interfaces for **Admin** and **Students**. This system allows seamless quiz creation, participation, and result tracking using a connected MySQL database.

---

## 🚀 Features

✨ **Admin Panel**

* Create and manage quizzes
* Add, update, and delete questions
* View student performance

🎓 **Student Panel**

* Login and attempt quizzes
* View scores instantly
* User-friendly interface

📊 **Database Integration**

* Stores quiz data, users, and results
* Ensures data consistency using MySQL

---

## 🛠️ Technologies Used

* 💻 Java (Swing for GUI)
* 🗄️ MySQL (Database)
* 🔗 JDBC (Database Connectivity)

---

## 📂 Project Structure

```
Quiz-Management-System/
│── QuizManagementSystem.java
│── quiz_management.sql
│── README.md
```

---

## ⚙️ Setup Instructions

### 1️⃣ Clone the Repository

```
git clone https://github.com/ayushi-k25/Quiz-Management-System.git
```

### 2️⃣ Setup Database

* Open MySQL
* Create a database:

```
CREATE DATABASE quiz_db;
```

* Import the SQL file:

```
USE quiz_db;
SOURCE quiz_management.sql;
```

---

### 3️⃣ Add MySQL Connector

Download MySQL Connector (JDBC Driver) and add it to your project.

---

### 4️⃣ Run the Project

Compile and run:

```
javac QuizManagementSystem.java
java QuizManagementSystem
```

---

## 📸 Screenshots

*(Add your project screenshots here for better presentation)*

---

## 🌟 Future Enhancements

* Web-based version
* Timer-based quizzes
* Leaderboard system
* Improved UI/UX

---

## 🤝 Contributing

Feel free to fork this repository and contribute!

---

## 📜 License

This project is for educational purposes.

---

## 👩‍💻 Author

**Ayushi Karlekar**

---

⭐ If you like this project, don’t forget to star the repo!
