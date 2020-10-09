# Tidsbanken

This project is made by Sebastian Porling, Alicia Grevsten and Josefin Salomaa.

## Installation, Compile, Execute and Deploy

This project utilizes **npm** and **maven** for building the project, we then will need a SSL key, databases and a hosting solution this is how we implemented our deployment.

You will have to build the project as follows (BASH GUIDE):

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

We used **ClearDB MySQL** through **Heroku**, which gave us 5 days of daily backups. We used **mLab** for the **MongoDB** database and AWS for deployment as we needed to ports and not to many free deployment solutions allow that. So this guide will use these three.

When you have set up ClearDB and mLab you can enter their links in the *application-prod.yml* and then generate a **secret** for the *JWT* this can be really anything.

Then in order to make SSL work for the HTTPS and WSS you will have to either get a certifified certificate or make your self signed one. We used a selfsigned certificate. This can be done with the following:

- `keytool -genkeypair -alias keystore -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore keystore.p12 -validity 3650`

When this is done you can build the application, we build it with the following commands:

- Run `cd client` -> `npm init` -> `npm run build` -> `cp -rf dist/* ../server/server/src/resources/static/`.
- We need a SSL key, remote or local SQL AND MongoDB database.
- Run from root folder -> `cd server/server/` -> `mvn clean` -> `mvn package`.

We hosted the application through a EC2 intance on AWS. Then installed Java 14 JDK and uploaded the *JAR* file made with maven and the ssl key. 

So you will have to make a free tier account on AWS then create a EC2 instance using Amazon Linux 2. You will then get the key that is used for connecting and uploading files through SSH. 

When you have created the EC2 instance and generated the key for connecting you can do the following:

- Download Java JDK 14 (LINUX) and do the following: `scp -i key.pem <jdk zip file> ec2-user@<ec2 instance public ip>:~` 
- Login to the EC2 instance using SSH or using the AWS website and do the following:
    - `mkdir /usr/lib/jvm` and then unzip your jdk in the *jvm* folder.
    - Run `vim /etc/enviroment` and then add:
        ```bash
        PATH="usr/lib/jvm/jdk/jdk-14.0.2/bin"
        JAVA_HOME="/usr/lib/jdk-14.0.2"
        ```
    - Lastly we will have to link the java and javac with the system doing the following:
        - `update-alternatives --install "/usr/bin/java" "java" "/usr/lib/jvm/jdk-14.0.2/bin/java" 0`
        - `update-alternatives --install "/usr/bin/javac" "javac" "/usr/lib/jvm/jdk-14.0.2/bin/javac" 0`
        - `update-alternatives --set java /usr/lib/jvm/jdk-14.0.2/bin/java`
        - `update-alternatives --set java /usr/lib/jvm/jdk-14.0.2/bin/java`
- You have to now upload the SSL key and the JAR file using the command on the first bullet but with the SSL key and JAR file instead of java zip.
- We can finally on the EC2 instance just run `sudo java -jar server-0.0.1-SNAPSHOT.jar` while having the SSL key in the same folder!
- The site should now be running.

## Comments

We did get some problems where the WI-FI Network at Manpower blocked WSS, so you might have to try another network in order to make the notifications work.
