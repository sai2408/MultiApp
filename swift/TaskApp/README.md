# iOS Task App (Swift + SwiftUI)

SwiftUI list, an `ObservableObject` view model, and `URLSession` with async/await calling the shared API.
Requires a Mac with Xcode 15+. Swipe a row left to delete; pull down to refresh.

**Option A — XcodeGen (one command):**

```bash
brew install xcodegen
xcodegen generate        # creates TaskApp.xcodeproj from project.yml
open TaskApp.xcodeproj
```

**Option B — by hand:** in Xcode create *File ▸ New ▸ Project ▸ iOS App* named `TaskApp` (SwiftUI),
replace its Swift files with the four in `TaskApp/`, and add `NSAppTransportSecurity ▸ NSAllowsLocalNetworking = YES`
to the target's Info settings so plain `http://localhost` is allowed.

Start a backend first (default: Ktor on port 8083). To use another one, change `baseURL` in `TaskAPI.swift`.
