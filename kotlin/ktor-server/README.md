# Ktor Task API (Kotlin backend)

The backend half of the Kotlin full stack. The frontend is `../android-app`.
In-memory storage (resets on restart).

Requires JDK 17+ (tested on JDK 26). No Gradle install needed: the Gradle Wrapper downloads Gradle 9.8.2
on first run. You can also open this folder in IntelliJ IDEA and run `main()` in `Application.kt`.

```bash
.\gradlew.bat run      # macOS/Linux: ./gradlew run
```

- API: http://localhost:8083/api/tasks
- From the Android emulator: http://10.0.2.2:8083/api/tasks
