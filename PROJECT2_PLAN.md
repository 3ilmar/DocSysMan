# Project 2 Plan

Main upgrades:
1. Convert CUI system into Java GUI.
2. Replace text file storage with Apache Derby Embedded database.
3. Add DAO classes for database access.
4. Add JUnit 4 tests.
5. Keep business logic separate from GUI and database code.

Planned structure:
- gui package for GUI screens
- model package for data classes
- dao package for database access
- service/manager package for business logic
- test package for JUnit tests