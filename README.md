# Virtual Power Plant System

## Description

The Virtual Power Plant (VPP) is a system that integrates various power resources to provide grid powermanagement.
A VPP typically sells its output to an electric utility. VPPs allow energy resources that are individually too small to
be of interest to a utility to aggregate and market their power.

# **Project Setup**

## **Prerequisites**

- ***Java***

- ***Spring Boot***

- ***PostgreSQL***

- ***IDE(IntelliJ or any)***

## **Version**

- ***Java 17***

- ***Spring Boot 3.1***

- ***PostgreSQL(latest)***

# **Installation**

- For Java, install the suitable jdk in the project structure of the
  IDE [Java Installation](https://www.oracle.com/java/technologies/downloads/#java8)

- For PostgreSQL, install the required drivers and run configuration files or run the database in a
  docker [PostgreSQL installation](https://www.postgresql.org/download/)

# **Start**

- For gradle build `./gradlew clean build` and  `./gradlew bootRun`
- To run the application, the following environment variables must be configured. You can set them in your .env file at
  /resources.
- POSTGRES_USER: The username to connect to the PostgreSQL database. Example: `POSTGRES_USER=username`
  POSTGRES_PASSWORD: The password for the PostgreSQL database user. Example: `POSTGRES_PASSWORD=123456789`
  POSTGRES_URL: The JDBC URL to connect to the PostgreSQL database. It should include the host, port, and database name.
  Example: `POSTGRES_URL=jdbc:postgresql://localhost:{port}/{db_name}`
  LOGS_FOLDER_PATH: The path where the application logs will be stored. Example: `LOGS_FOLDER_PATH=./logs`

# **API Documentation - Swagger UI Access**

API Documentation - Swagger UI Access
Once the application is running, access the Swagger UI for API documentation
at: [Swagger UI](http://localhost:8080/swagger-ui.html)
[![swagger.png](https://i.postimg.cc/zGF3P7c0/swagger.png)](https://postimg.cc/jDCsDHK7)

# **API Test Response**

Here's some Test Data response from the **POST /api/v1/battery endpoint:

```json
{
  "status": true,
  "message": "Battery saved successfully.",
  "data": [
    {
      "id": "0f10b690-6b74-4906-9605-dfb05a9d04f4",
      "name": "Cannington",
      "postcode": 6107,
      "capacity": 13500
    },
    {
      "id": "4f1a1edc-457d-43f8-bbe1-fa3b55337b6c",
      "name": "Midland",
      "postcode": 6057,
      "capacity": 50500
    },
    {
      "id": "aafe6a57-7270-49e5-8062-0cdc36f861e5",
      "name": "Hay Street",
      "postcode": 6000,
      "capacity": 23500
    },
    {
      "id": "e9ba473f-5803-4e89-92ee-255c88e264d0",
      "name": "Mount Adams",
      "postcode": 6525,
      "capacity": 12000
    },
    {
      "id": "0b137c0c-3d73-4f62-9b7f-df2a4bd52cb3",
      "name": "Koolan Island",
      "postcode": 6733,
      "capacity": 10000
    },
    {
      "id": "9881da6a-ede7-4839-8e73-f97d249a95f0",
      "name": "Armadale",
      "postcode": 6992,
      "capacity": 25000
    },
    {
      "id": "d9b32624-780a-4412-ae36-8db8fd9ae1b0",
      "name": "Lesmurdie",
      "postcode": 6076,
      "capacity": 13500
    },
    {
      "id": "4ed833a5-6768-4cb8-9ae1-0a8f968a1fdc",
      "name": "Kalamunda",
      "postcode": 6076,
      "capacity": 13500
    },
    {
      "id": "2f0cc873-034c-4a1c-b6bb-128b670d36aa",
      "name": "Carmel",
      "postcode": 6076,
      "capacity": 36000
    },
    {
      "id": "28494271-831b-4193-81e5-fb8a48aa0066",
      "name": "Bentley",
      "postcode": 6102,
      "capacity": 85000
    },
    {
      "id": "be4e7e8c-da4b-4232-819f-2d3e70da7b9f",
      "name": "Akunda Bay",
      "postcode": 2084,
      "capacity": 13500
    },
    {
      "id": "afe5e43c-b3bb-4e4f-85ee-4ceddb0d24bc",
      "name": "Werrington County",
      "postcode": 2747,
      "capacity": 13500
    },
    {
      "id": "ce78b165-9bd5-40b3-ae2d-496b597c7040",
      "name": "Bagot",
      "postcode": 820,
      "capacity": 27000
    },
    {
      "id": "a9a85100-d3cd-42d1-991e-7f85e227dddd",
      "name": "Yirrkala",
      "postcode": 880,
      "capacity": 13500
    },
    {
      "id": "a21ec1d5-8e7b-4570-bdbd-204ac72111ae",
      "name": "University of Melbourne",
      "postcode": 3010,
      "capacity": 85000
    },
    {
      "id": "d8dcd8d4-8cd3-4c73-b103-1ad9ee7f1f26",
      "name": "Norfolk Island",
      "postcode": 2899,
      "capacity": 13500
    },
    {
      "id": "f44adbaf-0640-4d4d-a142-1c4b8efe4d9a",
      "name": "Ootha",
      "postcode": 2875,
      "capacity": 13500
    },
    {
      "id": "a071e9da-b64d-41b2-ac29-46cc0cea18de",
      "name": "Kent Town",
      "postcode": 5067,
      "capacity": 13500
    },
    {
      "id": "2096b16d-7331-4ed5-a7bd-a0f8225eb845",
      "name": "Northgate Mc",
      "postcode": 9464,
      "capacity": 13500
    },
    {
      "id": "25b2a41b-fa7f-419d-bdb2-ef6414396e91",
      "name": "Gold Coast Mc",
      "postcode": 9729,
      "capacity": 50000
    }
  ],
  "error": null
}
```

To ensure the system can handle large numbers of concurrent battery registrations efficiently, I maintained a batch size
for saving batteries in bulk, reducing database calls. Connection pooling with HikariCP will optimize the reuse of
database connections,
minimizing connection overhead. Additionally, transaction management were implemented to ensures data integrity by
grouping
operations into a single transaction, enabling the system to handle large datasets with improved performance and
consistency.

# **Logging**

The application logs important events, including battery registrations, to a file regularly. Here’s a sample log of the
battery registration process:

[![logs.png](https://i.postimg.cc/G2H2tCh5/logs.png)](https://postimg.cc/BLfs7RFT)

**Log File Configuration:**

- Logs are stored in according to LOGS_FOLDER_PATH.
