# Android Task App (Kotlin + Jetpack Compose)

The frontend half of the Kotlin full stack: Compose UI, a `ViewModel`, and Retrofit calling the shared API.
By default it talks to the Ktor server in `../ktor-server`.

1. Start a backend, e.g. `../ktor-server` (port 8083).
2. Open **this folder** in Android Studio (Ladybug or newer). It downloads Gradle and syncs automatically.
3. Run the `app` configuration on an emulator.

To use another backend, change `API_BASE_URL` in `app/build.gradle.kts`
(e.g. `http://10.0.2.2:8082/` for Spring Boot). On a real phone use your PC's LAN IP instead of `10.0.2.2`.
