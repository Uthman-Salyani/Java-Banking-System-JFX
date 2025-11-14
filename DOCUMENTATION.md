# Banking System Documentation

This document provides a comprehensive and detailed overview of the Banking System application, including its structure, the technologies it uses, its functionalities, and an in-depth look at its source code.

## 1. Project Structure

The project is organized into a clear and logical directory structure that separates source code, compiled code, libraries, and other project-related files.

-   **`/src`**: This is the main directory for all the Java source code files (`.java`). The code is further organized into packages to ensure a clean and maintainable structure.
    -   **`/controllers`**: This package is currently empty but is intended to hold the application's controller classes. In a Model-View-Controller (MVC) architecture, controllers handle user input and interact with the model and the view.
    -   **`/database`**: This package contains the Data Access Object (DAO) classes. These classes are responsible for all interactions with the MySQL database, abstracting the database logic from the rest of the application.
    -   **`/main/application`**: This package contains the `Main.java` class, which is the main entry point of the application.
    -   **`/models`**: This package contains the data model classes. These are Plain Old Java Objects (POJOs) that represent the application's core entities, such as `Account`, `Customer`, and `Loan`.
    -   **`/utils`**: This package contains utility classes that provide common, reusable functions, such as password hashing and session management.
    -   **`/views`**: This package contains the JavaFX view classes. These classes are responsible for creating and managing the application's graphical user interface (GUI).

-   **`/bin`**: This directory contains the compiled Java class files (`.class`). When the `.java` source files are compiled, the resulting `.class` files are stored here.

-   **`/lib`**: This directory contains the external libraries (JAR files) that the project depends on. These libraries provide additional functionalities that are not part of the standard Java library.

-   **`.vscode`**: This directory contains configuration files for the Visual Studio Code editor, such as `launch.json` and `settings.json`.

-   **`README.md`**: The main README file for the project, which typically contains a brief introduction to the project and instructions on how to get it running.

-   **`DOCUMENTATION.md`**: This file, which provides a detailed documentation of the project.

## 2. Tools and Technologies

The Banking System application is built using a combination of robust and widely-used technologies:

-   **Java**: The core programming language for the application. Java is a powerful, object-oriented language that is well-suited for building a wide range of applications.

-   **JavaFX**: A modern, open-source framework for building rich client applications. JavaFX is used to create the application's graphical user interface (GUI), providing a set of graphics and media packages that enable developers to design, create, test, debug, and deploy rich client applications.

-   **MySQL**: A popular open-source relational database management system (RDBMS). MySQL is used to store all the application's data, including user information, account details, and transaction history.

-   **jbcrypt**: A Java library for securely hashing and verifying passwords. jbcrypt implements the OpenBSD Blowfish password hashing algorithm, which is a strong and widely-used password hashing function.

-   **mysql-connector-j**: The official MySQL JDBC (Java Database Connectivity) driver. This driver allows the Java application to connect to and interact with the MySQL database.

## 3. Functionalities

The Banking System application provides a range of functionalities for both customers and administrators.

### 3.1. User Authentication

-   **Login**: The application provides a secure login mechanism for both customers and administrators. Users need to enter their username and password to access the system.
-   **Password Hashing**: To enhance security, passwords are not stored in plain text. Instead, they are securely hashed using the jbcrypt library before being stored in the database. When a user logs in, the entered password is hashed and compared with the stored hash.
-   **Session Management**: The application uses a session manager to keep track of the currently logged-in user. The session manager stores the user's username, user ID, and customer ID, which can be accessed throughout the application.

### 3.2. Customer Dashboard

The customer dashboard provides a range of features that allow customers to manage their accounts and perform various banking operations:

-   **View Balance**: Customers can view the current balance of all their accounts.
-   **Deposit Money**: Customers can deposit money into their accounts through various methods, such as Mpesa.
-   **Withdraw Money**: Customers can withdraw money from their accounts.
-   **Apply for Loan**: Customers can submit a loan application, specifying the loan amount and duration.
-   **Request Overdraft**: Customers can request an overdraft for their current accounts, specifying the desired overdraft limit.
-   **View Statement**: Customers can view a detailed statement of their transactions for a selected account.
-   **Change Password**: Customers can change their password for security purposes.
-   **View Profile**: Customers can view their personal profile information.
-   **Opt Out of Account**: Customers have the option to permanently delete their account and all associated data.

### 3.3. Admin Dashboard

The admin dashboard provides a set of tools for administrators to manage the banking system and its users:

-   **Create Customer**: Admins can create new customer accounts by providing the necessary personal details.
-   **Add Account to Customer**: Admins can add new bank accounts to existing customers.
-   **Remove Account From Customer**: Admins can remove bank accounts from customers.
-   **View All Customers**: Admins can view a list of all registered customers in the system.
-   **Approve Loans**: Admins can review pending loan applications and either approve or decline them.
-   **Approve Overdrafts**: Admins can review pending overdraft requests and either approve or decline them.
-   **Change Password**: Admins can change their own password.

## 4. Source Code Overview

The source code is organized into several packages, each with a specific responsibility.

### 4.1. `database` Package

This package is the data layer of the application, responsible for all communication with the MySQL database.

-   **`AccountDAO.java`**: Manages all database operations related to bank accounts. It includes methods for creating, reading, updating, and deleting accounts.
-   **`CustomerDAO.java`**: Manages all database operations related to customers, such as creating new customers and retrieving customer details.
-   **`DatabaseConnection.java`**: A utility class that provides a static method to establish a connection to the MySQL database.
-   **`LoanDAO.java`**: Manages all database operations related to loans. It includes methods for creating loan applications, retrieving pending loans, and updating the status of a loan.
-   **`OverdraftDAO.java`**: Manages all database operations related to overdrafts, including creating overdraft requests and updating their status.
-   **`TransactionDAO.java`**: Manages all database operations related to transactions. It includes methods for creating new transactions and retrieving a list of transactions for a specific account.
-   **`UserDAO.java`**: Manages all database operations related to users. This class is responsible for user authentication, password updates, and retrieving user information.

### 4.2. `models` Package

This package contains the data model classes, which are simple Java objects (POJOs) that represent the application's entities.

-   **`Account.java`**: Represents a bank account, with properties such as `accountId`, `accountNumber`, `accountType`, and `balance`.
-   **`Customer.java`**: Represents a customer, with properties such as `customerId`, `firstName`, `lastName`, `email`, and `phone`.
-   **`Loan.java`**: Represents a loan, with properties such as `loanId`, `loanAmount`, `interestRate`, and `status`.
-   **`Overdraft.java`**: Represents an overdraft, with properties such as `overdraftId`, `overdraftLimit`, and `status`.
-   **`Transaction.java`**: Represents a financial transaction, with properties such as `transactionId`, `transactionType`, `amount`, and `description`.
-   **`User.java`**: Represents a user of the system, with properties such as `userId`, `username`, and `password`.

### 4.3. `utils` Package

This package contains utility classes that provide common, reusable functionalities.

-   **`PasswordHasher.java`**: A utility class that provides methods for hashing and checking passwords using the jbcrypt library.
-   **`SessionManager.java`**: A singleton class that manages the user's session. It stores the username, user ID, and customer ID of the currently logged-in user, making this information accessible throughout the application.
-   **`Validator.java`**: This class is currently empty but is intended to be used for input validation, ensuring that user input is in the correct format and meets the required criteria.

### 4.4. `views` Package

This package contains the JavaFX view classes, which are responsible for the application's user interface.

-   **`AdminDashboard.java`**: Implements the admin dashboard UI using JavaFX. It provides the interface for all the admin functionalities, such as creating customers, approving loans, and managing accounts.
-   **`CustomerDashboard.java`**: Implements the customer dashboard UI using JavaFX. It provides the interface for all the customer functionalities, such as viewing balances, making deposits, and applying for loans.
-   **`LoginView.java`**: Implements the login screen UI using JavaFX. It provides the interface for users to enter their credentials and log in to the system.

### 4.5. `main.application` Package

-   **`Main.java`**: This is the main entry point of the application. It contains the `main` method, which launches the JavaFX application and displays the initial login view.
