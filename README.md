
# Rally Demo Navigation Architecture

A production-ready Jetpack Compose navigation implementation demonstrating modern Android app architecture with multi-screen navigation, state management, and deep linking support.

## 📚 Project Overview

This project implements a financial app (`Rally`) with three main screens (Overview, Accounts, Bills) and a detail screen for individual accounts. It showcases best practices for navigation in Compose applications.

## 🎯 Core Concepts Learned

### 1. Navigation Architecture
Single source of truth for app navigation using Jetpack Compose Navigation:

NavController (Single Source of Truth)
↓
Routes (Overview, Accounts, Bills, Details)
↓
Screens (Composables)
↓
User Interactions trigger navigation


### 2. State Management in Navigation
- **`rememberNavController()`** — Persists navigation state across recompositions
- **`currentBackStackEntryAsState()`** — Observes active screen reactively
- **`saveState/restoreState`** — Remembers UI state when returning to screens

### 3. Route Parameters
Pass data through navigation routes (not through function parameters):
```kotlin
Route: "single_account/{accountType}"
Data: Encoded in URL, extracted when screen loads
```

## ✨ Features Implemented

### ✅ Bottom Navigation Bar
- Shows three tabs: Overview, Accounts, Bills
- Active tab is highlighted based on current destination
- Smooth switching between tabs

**Implementation:** `RallyBottomBar()` with `hierarchy.any()` check

```
[Overview] [Accounts] [Bills]
    ↑
Active tab highlighted based on currentDestination
```

---

### ✅ Tab Navigation (No Duplicates in Backstack)
Prevents duplicate screens when switching tabs multiple times.

**Without feature:**
```
User taps "Accounts" twice
→ Overview → Accounts → Accounts
→ Press back twice to exit
```

**With feature:**
```
User taps "Accounts" twice
→ Overview → Accounts
→ Press back once to exit
```

**Implementation:** `navigateSingleTopTo()` with `launchSingleTop = true`

---

### ✅ Detail Screen Navigation
Navigate from a list of accounts to a specific account's detail screen.

```
Accounts List → User clicks "Checking" → Detail screen for "Checking"
```

**Implementation:** `navigateToSingleAccount(accountType)` with encoded parameters

---

### ✅ Data Passing via Routes
Pass data safely through route parameters (survives process death).

**Bad approach:**
```kotlin
navController.navigate(accountType)  // Data lost if process dies
```

**Good approach (implemented here):**
```kotlin
navigate("single_account/Checking")  // Data in route, persistent
```

**Implementation:** `SingleAccount.createRoute()` + `navArgument` extraction

---

### ✅ URL Encoding/Decoding
Handles special characters in account names safely.

```kotlin
"Savings & Checking"
    ↓ Uri.encode()
"Savings%20%26%20Checking" (safe for URL)
    ↓ Uri.decode()
"Savings & Checking" (display)
```

**Use case:** Account names with spaces, special characters, symbols

---

### ✅ State Restoration
UI state (scroll position, form input) is preserved when returning to screens.

```
User navigates: Overview → Accounts → Bills → Accounts
Accounts screen shows same scroll position (saveState = true)
```

**Implementation:** `restoreState = true` in navigation builder

---

### ✅ Deep Linking Ready
Routes can be triggered from multiple sources:
- Internal button clicks
- Deep links: `rally://single_account/Checking`
- App shortcuts
- Push notifications

---

## 🛠️ Skills Demonstrated

| Skill | Implementation |
|-------|-----------------|
| **Extension Functions** | `NavHostController.navigateSingleTopTo()` |
| **Lambda Callbacks** | `onTabSelected: (RallyDestination) -> Unit` |
| **State Observation** | `currentBackStackEntryAsState()` |
| **Type Safety** | `NavType.StringType` for argument validation |
| **URL Encoding** | Handling special characters in parameters |
| **Navigation Patterns** | SingleTop, state saving, parameter passing |
| **Compose Scope** | Using `navArgument` inside `NavHost` |

---

## 📋 Function Breakdown

### `RallyApp()`
Main entry point. Creates navigation controller, sets up Scaffold with bottom bar and content area.

```kotlin
@Composable
fun RallyApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    
    Scaffold(bottomBar = { ... }) { innerPadding -> ... }
}
```

---

### `RallyBottomBar()`
Creates three navigation tabs. Highlights active tab based on current destination.

```kotlin
@Composable
fun RallyBottomBar(
    destinations: List<RallyDestination>,
    currentDestination: NavDestination?,
    onTabSelected: (RallyDestination) -> Unit
)
```

---

### `RallyNavHost()`
Defines all routes and maps them to screens:

| Route | Screen | When it shows |
|-------|--------|---------------|
| `"overview"` | `OverviewScreen` | App starts (default) |
| `"accounts"` | `AccountsScreen` | User taps Accounts tab |
| `"bills"` | `BillsScreen` | User taps Bills tab |
| `"single_account/{accountType}"` | `SingleAccountScreen` | User clicks specific account |

---

### `navigateSingleTopTo()`
Extension function for smart tab navigation. Prevents duplicate screens in backstack.

```kotlin
fun NavHostController.navigateSingleTopTo(route: String) {
    navigate(route) {
        popUpTo(graph.findStartDestination().id) {
            saveState = true  // Remember scroll position
        }
        launchSingleTop = true     // Don't recreate if already on screen
        restoreState = true        // Restore UI state when returning
    }
}
```

---

### `navigateToSingleAccount()`
Navigate to detail screen with encoded account name.

```kotlin
fun NavHostController.navigateToSingleAccount(accountType: String) {
    navigate(SingleAccount.createRoute(accountType))
}
```

---

## 🔄 Complete User Flow Example

```
1. App starts → RallyApp()
   ↓
2. Shows OverviewScreen (startDestination)
   ↓
3. User taps "Accounts" tab
   ↓
4. navigateSingleTopTo("accounts")
   ↓
5. AccountsScreen appears with bottom bar highlighting "Accounts"
   ↓
6. User clicks "Checking" account
   ↓
7. navigateToSingleAccount("Checking")
   ↓
8. Creates route: "single_account/Checking"
   ↓
9. NavHost matches composable with route pattern
   ↓
10. Extracts "Checking" from URL parameter
   ↓
11. SingleAccountScreen(accountType = "Checking") displays
   ↓
12. User presses back → Returns to AccountsScreen
   ↓
13. User taps "Overview" tab → navigateSingleTopTo("overview")
   ↓
14. Back at start (backstack cleared, efficient)
```

---

## ✅ Production-Ready Checklist

- ✅ No hardcoded strings — All routes in `RallyDestination` object
- ✅ Type-safe navigation — `navArgument` prevents runtime crashes
- ✅ Backstack management — `popUpTo + launchSingleTop`
- ✅ State preservation — Scroll positions remembered
- ✅ Proper encoding — Handles special characters
- ✅ Testable architecture — Each function has single responsibility
- ✅ Reusable helpers — `navigateSingleTopTo()` used across app
- ✅ Deep linking ready — Routes work with external triggers

---

## 🚀 Real-World Applications

This navigation pattern is used in:
- **E-commerce apps** — Product list → Product detail
- **Social media** — Feed → User profile → Comments
- **Banking apps** — Accounts list → Account details → Transaction history
- **Music apps** — Library → Playlist → Song details
- **Travel apps** — Destinations list → Destination details → Reviews

---

## 📚 Key Classes & Objects

### `SingleAccount` Navigation Object
```kotlin
object SingleAccount {
    const val baseRoute = "single_account"
    const val argAccountType = "accountType"
    const val route = "$baseRoute/{$argAccountType}"
    
    fun createRoute(accountType: String) = "$baseRoute/${Uri.encode(accountType)}"
}
```

---

## 🔧 Technologies Used

- **Kotlin** — Language
- **Jetpack Compose** — UI framework
- **Compose Navigation** — Navigation management
- **Material 3** — Design system
- **Android Studio** — IDE

---

## 🎓 What You Can Learn Next

1. **Deep link handling** — Open detail screen from notification
2. **Animation transitions** — Slide/fade when changing screens
3. **Argument validation** — Ensure account type exists before navigation
4. **Logout handling** — Clear backstack and reset app on logout
5. **Search navigation** — Jump directly to any screen from search
6. **Navigation testing** — Unit tests for navigation flows
7. **ViewModel integration** — Share data between screens
8. **Nested navigation graphs** — Organize complex apps

---

## 📝 Notes

- This implementation follows Android Architecture Components best practices
- State is preserved across configuration changes
- Backstack is managed efficiently to prevent memory leaks
- Routes are type-safe and prevent runtime crashes
- URL encoding handles all special characters properly

---

## 👨‍💻 Author

**HasnathAhmedTamim** on GitHub

---

## 📄 License

Open source — Feel free to use for learning and production projects
```
