**Development Environment**
- OS: Windows 10
- Database: PostgreSQL 14.3
- Java: Oracle Open JDK 16.0.1
- Node.js: v14.16.1

**Development Tools**
- pgAdmin 4
- IntelliJ IDEA  2021.3.3 (Community Edition)
- Visual Stuido Code 1.67.2

**Programming Languages and Frameworks**
- Java 8
- Springboot
- Liquibase
- React JS


**Useful UI Inspection Tools (Not mandatory)**
- Google Chrome Extension, Redux DevTools: Inspecting Redux action-state
- Google Chrome Extension, React Developer Tools: Inspecting render loops of React components 

**Running**

Frontend: 
1. npm install 
2. npm start

Backend: 
1. (Optional step: Run only if database is needed to be reset) liquibase:dropAll
2. maven clean install
3. run/debug SpringbootrestapiApplication class

**Notes**

Project requires a database. The current version is set to default of pgAdmin's PostgreSQL server 
with a username(root)-password(root) change. Database name is testdb. 

Database table creation and data queries are in the Liquibase xml files in the backend part of the project. 
Thus, an empty database suffice and after running the project it will be automatically filled by Liquibase. 
If database is needed to be reset at some point, by IntelliJ IDEA, or with other IDEs with Maven support, 
run the _dropAll_ command under the Maven's plugin _liquibase_ (liquibase:dropAll)

If desired, a different PostgreSQL database server can be used, but application.properties and liquibase.properties 
files in backend -> src/main/resources could be modified.

Frontend app runs on latest versions of Google Chrome, Mozilla Firefox and Opera without problems, 
other browsers are not tested but it is predicted to run on every browser with HTML 5 features.
