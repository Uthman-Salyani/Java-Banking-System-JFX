# Java-Banking-System-JFX

# Banking System

A comprehensive JavaFX-based banking management system with separate interfaces for administrators and customers, featuring secure authentication, transaction management, loan processing, and account operations.

## 📋 Table of Contents
- [Project Overview](#project-overview)
- [Features](#features)
- [Technologies Used](#technologies-used)
- [System Requirements](#system-requirements)
- [Installation & Setup](#installation--setup)
- [Database Schema](#database-schema)
- [Project Structure](#project-structure)
- [Usage Guide](#usage-guide)
- [Security Features](#security-features)
- [Future Enhancements](#future-enhancements)

---

## 🎯 Project Overview

This Banking System is a desktop application developed as part of an Object-Oriented Programming course project. It demonstrates core OOP principles including encapsulation, inheritance, polymorphism, and abstraction while providing a realistic banking interface for both administrative staff and customers.

### Key Objectives
- Implement OOP concepts in a real-world scenario
- Integrate database operations with Java applications
- Create intuitive user interfaces using JavaFX
- Develop secure authentication and authorization systems
- Handle complex business logic (transactions, loans, overdrafts)

---

## ✨ Features

### 🔐 Authentication System
- **Dual Login Interface**: Separate login portals for Admin and Customer
- **Remember Me Functionality**: Option to save user type preference for faster login
- **Secure Password Hashing**: All passwords stored using BCrypt hashing
- **Logout Functionality**: Secure session termination for both user types

### 👨‍💼 Admin Features
- **User Management**
  - Create new customer accounts
  - Assign account types (Savings or Current/Checking)
  - Delete customer accounts
  - View all customer records with masked sensitive data
  
- **Account Operations**
  - View customer transaction history
  - Export customer records to CSV/PDF
  
- **Loan & Overdraft Management**
  - Review loan applications
  - Approve or decline loan requests
  - Process overdraft requests
  
- **Admin Settings**
  - Change admin password
  - View system statistics

### 👤 Customer Features
- **Account Operations**
  - Deposit money
  - Withdraw money
  - View account balance
  
- **Financial Services**
  - Apply for loans
  - Request overdraft protection
  - View loan application status
  
- **Account Management**
  - Change password
  - View account statement (requires password re-entry for security)
  - View Terms and Conditions
  
- **Security**
  - Re-authentication required for sensitive operations
  - Session timeout after inactivity

---

## 🛠️ Technologies Used

| Technology | Purpose |
|------------|---------|
| **Java 21** | Core programming language |
| **JavaFX 21** | GUI framework for desktop interface |
| **MySQL 8.0+** | Relational database management |
| **JDBC** | Database connectivity |
| **BCrypt** | Password hashing and security |
| **Maven** (Optional) | Dependency management |

---

## 💻 System Requirements

### Minimum Requirements
- **Java**: JDK 21 or higher
- **JavaFX**: SDK 21
- **MySQL**: Version 8.0 or higher

### Development Tools
- **IDE**: Visual Studio Code with Java Extension Pack
- **Database**: MySQL Server
- **Version Control**: Git (recommended)

---

## 📦 Installation & Setup

### Step 1: Clone the Repository
```bash
git clone https://github.com/yourusername/banking-system.git
cd banking-system
```

### Step 2: Install MySQL
```bash
# For Red Hat/Fedora
sudo dnf install mysql-server

# Start MySQL service
sudo systemctl start mysqld
sudo systemctl enable mysqld
```

### Step 3: Create Database
```bash
mysql -u root -p
```

```sql
CREATE DATABASE banking_system;
USE banking_system;
```

Run the SQL scripts in `/database/schema.sql` to create all necessary tables.

### Step 4: Configure Database Connection
Edit `src/database/DatabaseConfig.java`:
```java
private static final String URL = "jdbc:mysql://localhost:3306/banking_system";
private static final String USER = "your_mysql_username";
private static final String PASSWORD = "your_mysql_password";
```

### Step 5: Install JavaFX
1. Download JavaFX SDK 21 from [Gluon](https://gluonhq.com/products/javafx/)
2. Extract to a permanent location (e.g., `/usr/local/javafx-sdk-21`)
3. Update `.vscode/settings.json` with your JavaFX path

### Step 6: Run the Application
```bash
# Open in VS Code
code .

# Press F5 or use Run > Start Debugging
```

---

## 🗄️ Database Schema

### Tables Overview

#### 1. `users`
Stores authentication credentials for all system users.

| Column | Type | Description |
|--------|------|-------------|
| user_id | INT (PK) | Unique user identifier |
| username | VARCHAR(50) | Unique login username |
| password | VARCHAR(255) | BCrypt hashed password |
| role | ENUM | 'admin' or 'customer' |
| created_date | TIMESTAMP | Account creation date |

#### 2. `customers`
Contains personal information for customers.

| Column | Type | Description |
|--------|------|-------------|
| customer_id | INT (PK) | Unique customer identifier |
| user_id | INT (FK) | References users.user_id |
| first_name | VARCHAR(50) | Customer's first name |
| last_name | VARCHAR(50) | Customer's last name |
| email | VARCHAR(100) | Contact email |
| phone | VARCHAR(20) | Phone number |
| address | TEXT | Physical address |
| date_of_birth | DATE | Birth date |

#### 3. `accounts`
Bank account information.

| Column | Type | Description |
|--------|------|-------------|
| account_id | INT (PK) | Unique account identifier |
| customer_id | INT (FK) | References customers.customer_id |
| account_number | VARCHAR(20) | Unique account number |
| account_type | ENUM | 'savings' or 'current' |
| balance | DECIMAL(15,2) | Current balance |
| status | ENUM | 'active', 'frozen', 'closed' |
| created_date | TIMESTAMP | Account opening date |

#### 4. `transactions`
Transaction history for all accounts.

| Column | Type | Description |
|--------|------|-------------|
| transaction_id | INT (PK) | Unique transaction ID |
| account_id | INT (FK) | References accounts.account_id |
| transaction_type | ENUM | 'deposit', 'withdrawal', 'transfer' |
| amount | DECIMAL(15,2) | Transaction amount |
| balance_after | DECIMAL(15,2) | Balance after transaction |
| description | TEXT | Transaction details |
| transaction_date | TIMESTAMP | When transaction occurred |

#### 5. `loans`
Loan applications and status.

| Column | Type | Description |
|--------|------|-------------|
| loan_id | INT (PK) | Unique loan identifier |
| customer_id | INT (FK) | References customers.customer_id |
| loan_amount | DECIMAL(15,2) | Requested amount |
| interest_rate | DECIMAL(5,2) | Interest rate % |
| duration_months | INT | Loan duration |
| status | ENUM | 'pending', 'approved', 'declined' |
| application_date | TIMESTAMP | When applied |
| processed_date | TIMESTAMP | When reviewed |

#### 6. `overdrafts`
Overdraft protection requests.

| Column | Type | Description |
|--------|------|-------------|
| overdraft_id | INT (PK) | Unique overdraft ID |
| account_id | INT (FK) | References accounts.account_id |
| overdraft_limit | DECIMAL(10,2) | Requested limit |
| status | ENUM | 'pending', 'approved', 'declined' |
| request_date | TIMESTAMP | When requested |

---

## 📁 Project Structure

```
BankingSystem/
├── src/
│   ├── main/
│   │   └── application/
│   │       └── Main.java                 # Application entry point
│   ├── models/
│   │   ├── User.java                     # Abstract base class
│   │   ├── Admin.java                    # Admin user class
│   │   ├── Customer.java                 # Customer user class
│   │   ├── Account.java                  # Abstract account class
│   │   ├── SavingsAccount.java           # Savings account
│   │   ├── CurrentAccount.java           # Current/Checking account
│   │   ├── Transaction.java              # Transaction model
│   │   ├── Loan.java                     # Loan model
│   │   └── Overdraft.java                # Overdraft model
│   ├── controllers/
│   │   ├── LoginController.java          # Login screen logic
│   │   ├── AdminController.java          # Admin operations
│   │   └── CustomerController.java       # Customer operations
│   ├── views/
│   │   ├── LoginView.java                # Login interface
│   │   ├── AdminDashboard.java           # Admin UI
│   │   ├── CustomerDashboard.java        # Customer UI
│   │   └── components/                   # Reusable UI components
│   ├── database/
│   │   ├── DatabaseConnection.java       # DB connection manager
│   │   ├── DatabaseConfig.java           # Configuration
│   │   ├── UserDAO.java                  # User data access
│   │   ├── AccountDAO.java               # Account data access
│   │   ├── TransactionDAO.java           # Transaction data access
│   │   └── LoanDAO.java                  # Loan data access
│   └── utils/
│       ├── PasswordHasher.java           # BCrypt utilities
│       ├── Validator.java                # Input validation
│       ├── SessionManager.java           # User session handling
│       └── ExportUtil.java               # CSV/PDF export
├── resources/
│   ├── images/                           # Application icons/images
│   ├── styles/
│   │   └── styles.css                    # JavaFX CSS styling
│   └── fxml/                             # FXML layout files (if used)
├── database/
│   └── schema.sql                        # Database creation scripts
├── lib/                                  # External JAR files
├── .vscode/
│   ├── settings.json                     # VS Code Java settings
│   └── launch.json                       # Run configurations
├── README.md                             # This file
└── .gitignore                            # Git ignore rules
```

---

## 📖 Usage Guide

### First Time Setup

1. **Default Admin Account**
   - Username: `admin`
   - Password: `admin123`
   - ⚠️ **Change this immediately after first login!**

2. **Creating Your First Customer**
   - Login as admin
   - Navigate to "Create Customer"
   - Fill in all required fields
   - Assign account type (Savings/Current)
   - System generates unique account number automatically

### Admin Workflow

```
Login as Admin → Dashboard
    ├── Create Customer → Enter Details → Assign Account Type → Save
    ├── View Customers → Select Customer → View Details/Delete
    ├── Manage Loans → View Applications → Approve/Decline
    ├── Manage Overdrafts → Review Requests → Approve/Decline
    ├── Export Records → Select Format (CSV/PDF) → Save
    └── Settings → Change Password → Logout
```

### Customer Workflow

```
Login as Customer → Dashboard
    ├── View Balance → Check Current Balance
    ├── Deposit → Enter Amount → Confirm
    ├── Withdraw → Enter Amount → Verify Funds → Confirm
    ├── Apply for Loan → Fill Application → Submit
    ├── Request Overdraft → Specify Limit → Submit
    ├── View Statement → Re-enter Password → View Transactions
    ├── View Terms & Conditions → Read T&Cs
    └── Settings → Change Password → Logout
```

---

## 🔒 Security Features

### Password Security
- **BCrypt Hashing**: All passwords hashed with salt
- **Minimum Requirements**: 8+ characters, mix of letters and numbers
- **No Plain Text Storage**: Passwords never stored in readable format

### Session Management
- **Single Session per User**: Prevents multiple simultaneous logins
- **Auto-Logout**: Inactive sessions terminated after 15 minutes
- **Secure Logout**: Proper session cleanup on logout

### Data Protection
- **Re-authentication**: Sensitive operations require password re-entry
- **Masked Display**: Passwords shown as asterisks in admin view
- **SQL Injection Prevention**: Prepared statements used throughout
- **Input Validation**: All user inputs sanitized and validated

### Access Control
- **Role-Based Access**: Strict separation between admin and customer privileges
- **Authorization Checks**: Every operation validates user permissions
- **Audit Trail**: All transactions logged with timestamps

---

## 🚀 Future Enhancements

### Planned Features (v2.0)
- [ ] Email notifications for transactions
- [ ] Two-factor authentication (2FA)
- [ ] Mobile responsive web version
- [ ] Interest calculation automation
- [ ] Multiple currency support
- [ ] Account statements via email
- [ ] Transaction disputes system
- [ ] Credit score tracking
- [ ] Scheduled/recurring payments
- [ ] Account freezing/unfreezing by customer

### Technical Improvements
- [ ] Implement MVC pattern more strictly
- [ ] Add unit tests (JUnit)
- [ ] Integrate logging framework (Log4j)
- [ ] Add connection pooling
- [ ] Implement caching for frequent queries
- [ ] Add API for mobile app integration

---

## 👥 Contributors

**Developer**: [Your Name]  
**Course**: Object-Oriented Programming 1  
**Institution**: [Your University]  
**Semester**: Second Semester  
**Year**: 2025

---

## 📄 License

This project is developed for educational purposes as part of coursework requirements.

---

## 📝 Version History

### v1.0.0 (Current)
- Initial release
- Basic admin and customer functionality
- Database integration
- Secure authentication
- Loan and overdraft management

---

**Last Updated**: October 31, 2025
