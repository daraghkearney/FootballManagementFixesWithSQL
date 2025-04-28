## Database Setup Instructions

**You must set up the database before running the application.**

Follow these steps:

1. **Create a new database in MySQL** :
   - Name the database **`football_db`** (exact spelling).
     

2. **Import through MySQL Workbench**.
   - Select the `fpdv3.sql` file (included in this repository).
   - Make sure you have selected to import onto the `football_db` schema that you have created.

3. **Import to IntelliJ Project**.
   - Open IntelliJ > Database tab (on the right side, click Database).
   - Click "+" ➔ Data Source ➔ MySQL.
   - Enter your database connection info (host, port, username, password).
   - Once connected, right-click on the database name, and choose "Run SQL Script".
   - Select your .sql file.
   - It will run and create all the tables/data you need.



---

## Notes

- The application expects the database to be named **`football_db`** exactly.

---
