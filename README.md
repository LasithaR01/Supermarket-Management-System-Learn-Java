# 🛒 CMart - Supermarket POS System

A Java Swing-based Point of Sale (POS) and Inventory Management System. This project manages products, suppliers, customers, employees, and processes sales.

## 🛠️ Prerequisites

Before you run this project, ensure you have the following installed:
*   **Java Development Kit (JDK):** Version 8 or higher (JDK 19 recommended based on project settings).
*   **NetBeans IDE:** Version 12 or higher (Project is built with Ant).
*   **MySQL Server:** Version 8.0 or higher.
*   **MySQL Workbench:** For database management.

---

## 🚀 Setup Instructions

### 1. Database Setup
1. Open **MySQL Workbench**.
2. Create a new database (Schema) named `cmartdb`.
3. Open the `pos.sql` file located in the root of this project.
4. In MySQL Workbench, go to **Server > Data Import**.
5. Select **Import from Self-Contained File** and choose the `pos.sql` file.
6. Set the **Default Target Schema** to `cmartdb` and click **Start Import**.

### 2. Configure Database Connection
Open `src/pos/pro/db.java` and update the connection string with your local MySQL credentials:

```java
// Change the password "78563" to your actual MySQL root password
con = DriverManager.getConnection("jdbc:mysql://localhost:3307/cmartdb", "root", "78563");