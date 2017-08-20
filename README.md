# TCF

## Services

The services are based on Representational State Transfer (REST) which are understood as the lightweight approach. One important behaviour of rest is the stateless approach, which means that no information is retained in communication between the client and the serve.
Following are the services provided by the framework for validation purpose.

- ImportTable: the import table accepts a JSON String defining user extracted table in table JSON format as described in section 5.2.2. This service stores the tables and allows for further processing. It uses HTTP POST method to retrieve pdf JSON format from clients and responds with simple text formatted message whether the information sent was accepted or rejected by the server.

- DeleteUserTable: Removes all imported tables from the framework. It allows for adding different tables, it uses HTTP DELETE method. The service responds with simple JSON message containing information whether all imported tables were removed or responds with an error in case of failure.

- CalculateScore: Calculates the scoring value for imported tables. It starts the scoring algorithm and sends the imported tables into the system for scoring calculation and return a value that represents the scoring value. This service uses HTTP GET method to response to the client with the scoring value.


