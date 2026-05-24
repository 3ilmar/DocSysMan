# Document Management System

This is a Java Document Management System project.

Project 1 included:
- Java CUI version
- Admin and User login
- Add, view, search, update, archive, and delete document records
- File I/O using text files
- Role-based access control

Project 2 will extend the system with:
- Java GUI
- Apache Derby Embedded database
- DAO layer
- JUnit 4 testing
- Clear separation between GUI, business logic, and data access
- Git/GitHub version control

## Project 2 Database and Testing Update

The system was updated to use an Apache Derby embedded database. I added a DatabaseManager class in the g10docmansys.db package to handle the database connection and create the tables when the program starts.

The database has two main tables:

- documents: stores document details such as ID, title, description, file path, category, and creation date.
- users: stores user details such as ID, username, password, and role.

I also added DAO classes to keep the database code separate from the rest of the application. DocumentDAO handles document actions such as adding, viewing, searching, updating, and deleting documents. UserDAO handles user actions such as adding users, checking login details, getting roles, and deleting users.

JUnit tests were added to check that the DAO classes work correctly. DocumentDAOTest checks the document add, search, and view methods. UserDAOTest checks user creation, login validation, and role retrieval. Both tests passed successfully in NetBeans.
