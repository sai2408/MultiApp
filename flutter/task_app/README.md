# Flutter Task App

One Dart codebase for Android, iOS, web and desktop, using the `http` package to call the shared API.
Only `lib/` and `pubspec.yaml` are checked in; the platform folders are generated.

```bash
cd flutter/task_app
flutter create .          # one-time: generates android/, ios/, web/, windows/ ... (keeps lib/)
flutter pub get
flutter run               # pick a device/emulator
```

Start a backend first (default: Ktor on port 8083). To use another one, change `_port` in `lib/task_api.dart`.

**Android note:** plain `http://` is blocked by default on Android 9+. After `flutter create .`, add
`android:usesCleartextTraffic="true"` to the `<application>` tag in `android/app/src/main/AndroidManifest.xml`.

**iOS note:** add `NSAppTransportSecurity ▸ NSAllowsLocalNetworking = YES` to `ios/Runner/Info.plist`.

**Web note:** running in Chrome works because every backend sends CORS headers for `/api/*`.
