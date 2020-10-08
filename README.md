# Tidsbanken

This project is made by Sebastian Porling, Alicia Grevsten and Josefine Salomaa.

## Installation, Compile, Execute and Deploy

This project utilizes **npm** and **maven** for building the project, we then will need a SSL key, databases and a hosting solution this is how we implemented our deployment.

You will have to build the proj
ect as follows (BASH GUIDE):

We have to create an *application-prod.yml* in the */server/server/src/resources/* folder and add the following information:

```yml
spring:
  jpa:
    hibernate:
      database-platform: org.hibernate.dialect.MySQLDialect
      ddl-auto: update
  datasource:
    url: <jdbc mysql with login credentials and database>
    hikari:
      connection-timeout: 10000
      maximum-pool-size: 5
  data:
    mongodb:
      uri: <mongodb uri with login credentials>
server:
  port: ${PORT:443}
  host: <private ip for server>
  ssl:
    key-store: <key name>
    key-store-password: <key password>
    keyStoreType: <key store type>
    keyAlias: <key alias>
  forward-headers-strategy: NATIVE
  tomcat:
    remote_ip_header: x-forwarded-for
    protocol_header: x-forwarded-proto
socket:
  port: 4121
jwt:
  secret: <a random secret string>
```

- Run `cd client` -> `npm init` -> `npm run build` -> `cp -rf dist/* ../server/server/src/resources/static/`.
- We need a SSL key, remote or local SQL AND MongoDB database.
- Run from root folder -> `cd server/server/` -> `mvn clean` -> `mvn package`.