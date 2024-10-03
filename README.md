# Spring Boot Rest API

## Start the app

### Using maven command
Navigate to the project directory and run:  
`mvn spring-boot:run`

Specify Profile:  
`mvn spring-boot:run -Dspring-boot.run.profiles=h2`  
`mvn spring-boot:run -Dspring-boot.run.profiles=postgres` 

### Using java command
- Make sure to run mvn clean package so that jar file is available.  
`java -Dspring.profiles.active=h2 -jar ./target/*.jar` 

### Using mvnw command (is basically the same as using the mvn command)
`./mvnw clean package -DskipTests`  
`./mvnw spring-boot:run -Dspring-boot.run.profiles=h2` 

## Serving static resources (html, external css, and image inside html)
My example (external css + image): http://localhost:8080/html_with_external_css_and_image.html  
Basic html: http://localhost:8080/test.html  
Basic html: http://localhost:8080/testB.html  
