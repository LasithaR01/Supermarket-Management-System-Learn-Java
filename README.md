# CMart - Supermarket POS System

A Java Swing-based Point of Sale (POS) and Inventory Management System. This project manages products, suppliers, customers, employees, and processes sales.

## Docs

| File | What it covers |
|------|----------------|
| [docs/ACTIVITY.md](docs/ACTIVITY.md) | Team timeline — who / when / module / feat·fix·docs (auto on push) |
| [docs/COMMIT_GUIDE.md](docs/COMMIT_GUIDE.md) | How to write commits so ACTIVITY stays clean |
| [docs/ARCHITECTURE.md](docs/ARCHITECTURE.md) | Package layout (`cmart`) and layers |
| [docs/SMART_POS_ROADMAP.md](docs/SMART_POS_ROADMAP.md) | Step-by-step plan to grow into a real-world smart POS |
| [docs/MODULES.md](docs/MODULES.md) | Current modules + status (Done / Needs update / Planned) |
| [docs/CHANGELOG.md](docs/CHANGELOG.md) | Milestone changelog |

## Prerequisites

Before you run this project, ensure you have the following installed:

* **Java Development Kit (JDK):** Version 8 or higher (JDK 19 recommended based on project settings).
* **NetBeans IDE:** Version 12 or higher (Project is built with Ant).
* **MySQL Server:** Version 8.0 or higher.
* **MySQL Workbench:** For database management.

---

## Setup Instructions

### 1. Database Setup
1. Open **MySQL Workbench**.
2. Create a new database (Schema) named `cmartdb`.
3. Open the `pos.sql` file located in the root of this project.
4. In MySQL Workbench, go to **Server > Data Import**.
5. Select **Import from Self-Contained File** and choose the `pos.sql` file.
6. Set the **Default Target Schema** to `cmartdb` and click **Start Import**.

### 2. Configure Database Connection
1. Copy `config/db.properties.example` to `config/db.properties`.
2. Edit `config/db.properties` with your MySQL URL, user, and password.

```properties
jdbc.url=jdbc:mysql://localhost:3307/cmartdb
jdbc.user=root
jdbc.password=YOUR_PASSWORD_HERE
```

### 3. Run
Open the project in NetBeans and run main class **`cmart.App`**.

On first start the app creates `app_user` / `app_setting` tables and seeds demo logins (passwords stored hashed, not plain text):

| Username | Password | Role |
|----------|----------|------|
| `admin` | `admin123` | Full access + Settings |
| `cashier` | `cashier123` | Sales / invoices / customers |
| `store` | `store123` | Products / suppliers |

Change these after install (Admin → **Users** in the header: create accounts and change passwords).
