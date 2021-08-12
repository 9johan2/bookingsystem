# Restaurant booking manager
Create App which will be capable of managing restaurant reservations. 

Required features
* Webpage for creating new booking request (with date, time, customer’s name and 
customer’s email)
* Webpage for checking current state of booking request (using uuid of a booking 
request)
* Webpage for accepting or denying the booking request
* Webpage for list of all active requests (active request are the ones which has the 
booking time before current real time. In other words, the visit of a restaurant not 
happened yet)

Requirements
* Use Spring Boot to create the app
* Use MongoDB database for storing booking requests
* Database connection parameters should be configurable using environment variables
* Create, accept, deny, and list requests should be created as REST API so any external 
app can use this API
* Don’t worry about security (authentication, secured connections, …)

Nice to have
* Database of restaurant available tables and how many people can be located at
every table
* Don’t allow to create new booking of already taken table in specified time
* When creating new booking request on webpage only allow to create request with 
not already taken table at specified tim
