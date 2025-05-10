Here are clear step-by-step instructions to run your Student Vaccination Management System:
________________________________________
✅ Prerequisites
Make sure the following are installed on your system:
1.	✅ Java 21
2.	✅ Maven (at least v3.6)
3.	✅ MySQL 
4.	✅ Eclipse / IntelliJ / VS Code (any Java IDE)
5.	✅ NodeJs
________________________________________
 1. Clone or Open the Project
	1.User service – https://github.com/Niladri-bit/Users-microservice
	2.Vaccine service - https://github.com/Niladri-bit/VaccineService
	3.Vaccination management system (UI) - https://github.com/Niladri-bit/vaccination-management-system
________________________________________
2. Create the Database
In MySQL Workbench:
Login with username & password as root
 
CREATE userdb & vaccinationdb schemas

 
________________________________________
 3. Build & Run the Backend
Using Eclipse IDE
•	Open user-service & vaccination-service in eclipse 
•	Right-click YourApplication.java (the one with @SpringBootApplication)
  

•	Click Run As → Java Application
•	On successful start of the application you should receive console messages as below:-
 
 
•	Post that you can open postman and hit few of the apis as mentioned earlier and check ,you should get proper response of the apis
