# Register Form Application

## Technologies used

I developed the application in Java using IntelliJ IDEA, using the Swing UI Designer for form creation. The implementation also includes the use of an SQL connector API and the javax.mail API.

## Functionalities Implemented and Their Realization

* __Data Validation__ - User input undergoes validation, triggering an error message for incorrect entries. Textfield data is processed through IF statements. If entered correctly, a method connects to the database to verify the user during login or creates a new user during registration.

* __Data Saved In A Database__ - Utilizing the SQL connector API, the application connects to a server (in this case I used XAMPP), executing queries to store and retrieve data.

* __Sending an email verification__ - Originally intended for successful registration notification via email, I encountered issues due to Gmail's removal of third-party program support in 2022. The code using the class was commented out. The javax.mail API, with the JavaBeans Activation Framework for MIME type compatibility, was employed to implement this functionality.

* __Login/Logout__ - The login is achieved through a button that verifies user information, opening the user form upon success. Logout is realized by a button within the user form, closing the current window and returning to the login form.

* __CRUD interface__ - The application allows profile creation, information viewing in the user form, and includes buttons for updating information in the database and deleting the user. Both actions return users to the login form after completion.
 
* __Unit testing__ - I opted not to implement unit testing due to the code structure, which wasn't conducive to testing. In the remaining time, I prioritized maintaining the working code over initiating a refactoring session to enable testing.

## Utilized Auto-generated and Ready Functions

I employed auto-generated functions for creating a listener and utilized ready functions from APIs for database connectivity, query execution, and email sending.

## Which files to what do they refer to

* __SendEmail class__ - Manages email sending, although Gmail compatibility issues currently hinder its functionality.
* __User class__ - Contains user information (name, email, password) for easier access and information passing.
* __Login class and form__ - Manages logic behind buttons for the login, including database connections during login, and the associated form with text fields and buttons.
* __Registration class and form__ - Manages logic behind buttons for the registration, including database connections during registration, and the associated form with text fields and buttons.
* __User class and form__ - Manages logic behind buttons for the user panel, including database connections during saving and deleting, and the associated form with text fields and buttons.
* __Jar files__ - These are essential external APIs used for connecting to the SQL database and enabling email sending.
