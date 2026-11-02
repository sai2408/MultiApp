# Spring Boot Task Manager

Spring Boot 3 with auto-configuration: Spring Data JPA repository + service layer, H2 file database,
Thymeleaf UI, embedded Tomcat. Compare with `../spring-app`, where all of this is wired by hand.

Requires JDK 17+. No Maven install needed: the Maven Wrapper (`mvnw`) downloads it on first run
(or open the folder in IntelliJ IDEA and run `TaskApplication`).

```bash
.\mvnw.cmd spring-boot:run      # macOS/Linux: ./mvnw spring-boot:run
```

- Web UI: http://localhost:8082
- API: http://localhost:8082/api/tasks
- H2 console: http://localhost:8082/h2-console (JDBC URL `jdbc:h2:file:./data/tasks`, user `sa`)
