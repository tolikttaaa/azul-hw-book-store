# Run Guide

To launch the application you need to complete the following list of steps:
1. Setup PostgreSQL database
2. Setup environment variables
3. Launch application

## Setup PostgreSQL database
You will need to set up `PostgreSQL` database with to run the app. 
One of the possible way is to do it by `Docker`

## Setup environment variables

- `DB_IP`, default value - `localhost`
    
   Database host

- `DB_PORT`, default value - `5432`

   Database port

- `DB_NAME`, default value - `bookStore`

   Database name

- `SPRING_DATASOURCE_USERNAME`, default value - `user`

   Database username

- `SPRING_DATASOURCE_PASSWORD`, default value - `password`

   Database password   

- `JWT_SECRET`, default value - `secretKey`

   Secret to generate JWT token for authentication

- `EXPIRATION_TIME`, default value - `3600000` (1 hour in millis)

   Expiration time to JWT tokens in millis

- `SYSTEM_USER_ENABLED`, default value - `true`

   Flag to init system user

- `SYSTEM_USERNAME`, default value - `system`

   Username for system user with SYSTEM role

- `SYSTEM_PASSWORD`, default value - `password`

   Username for system user with SYSTEM role

- `SHOW_SQL_LOG`, default value - `false`

   Flag to log SQL request to database. Can be used in case of testing

- `RESPONSE_TRACE`, default value - `false`

   Flag to insert stack trace into the error response. Can be used in case of testing 


## Launch application 
To lunch application you need to execute that command from the root of the project  

```bash
./gradlew bootRun
```
