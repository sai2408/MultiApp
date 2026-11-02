# Spring MVC Task Manager (no Spring Boot)

Classic Spring: a WAR with hand-written configuration — `WebInitializer` replaces `web.xml`,
`WebConfig` wires MVC, Thymeleaf and CORS. Data is kept in memory (resets on restart).
Compare with `../springboot-app`, where Boot auto-configures all of this.

Requires JDK 17+. No Maven install needed: the Maven Wrapper (`mvnw`) downloads it on first run.

```bash
.\mvnw.cmd jetty:run      # macOS/Linux: ./mvnw jetty:run
```

- Web UI: http://localhost:8081
- API: http://localhost:8081/api/tasks

To deploy to Tomcat 10.1+ instead: `.\mvnw.cmd package`, then copy `target/spring-app.war` into Tomcat's `webapps/`.
