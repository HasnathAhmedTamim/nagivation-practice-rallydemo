package com.example.rallydemo

import android.net.Uri
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument

@Composable
fun RallyApp() {



    /*What it does:
    Creates a navController to manage navigation
    Tracks the current screen using currentBackStackEntryAsState()
    Sets up a Scaffold with:
    Bottom Navigation Bar (shows Overview/Accounts/Bills tabs)
    Content Area (shows the current screen)*/



    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        bottomBar = {
            RallyBottomBar(
                destinations = rallyTopDestinations,
                currentDestination = currentDestination,
                onTabSelected = { destination ->
                    navController.navigateSingleTopTo(destination.route)
                }
            )
        }
    ) { innerPadding ->
        RallyNavHost(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun RallyBottomBar(
    destinations: List<RallyDestination>,
    currentDestination: NavDestination?,
    onTabSelected: (RallyDestination) -> Unit
) {
    NavigationBar {
        destinations.forEach { destination ->
            val selected = currentDestination
                ?.hierarchy
                ?.any { it.route == destination.route } == true

            NavigationBarItem(
                selected = selected,
                onClick = { onTabSelected(destination) },
                icon = { Icon(destination.icon, contentDescription = destination.title) },
                label = { Text(destination.title) }
            )
        }
    }
}

@Composable
fun RallyNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = RallyDestination.Overview.route,
        modifier = modifier
    ) {
        composable(RallyDestination.Overview.route) {
            OverviewScreen(
                onSeeAllAccounts = {
                    navController.navigateSingleTopTo(RallyDestination.Accounts.route)
                },
                onSeeAllBills = {
                    navController.navigateSingleTopTo(RallyDestination.Bills.route)
                },
                onAccountClick = { accountType ->
                    navController.navigateToSingleAccount(accountType)
                }
            )
        }
        composable(RallyDestination.Accounts.route) {
            AccountsScreen(
                onAccountClick = { accountType ->
                    navController.navigateToSingleAccount(accountType)
                }
            )
        }
        composable(RallyDestination.Bills.route) {
            BillsScreen()
        }
        composable(
            route = SingleAccount.route,
            arguments = listOf(
                navArgument(SingleAccount.argAccountType) {
                    type = NavType.StringType
                }
            )
        ) { entry ->
            val encoded = entry.arguments?.getString(SingleAccount.argAccountType) ?: "Unknown"
            val accountType = Uri.decode(encoded)
            SingleAccountScreen(accountType = accountType)
        }
    }
}

// SingleTop navigation helper – prevents duplicate tabs in backstack
fun NavHostController.navigateSingleTopTo(route: String) {
    navigate(route) {
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}

// Navigate to detail screen with encoded argument
fun NavHostController.navigateToSingleAccount(accountType: String) {
    navigate(SingleAccount.createRoute(accountType))
}



/*

* rememberNavController()
Creates navigation manager
*
*
navigateSingleTopTo()
Navigate between tabs (no duplicates)
*
*
navigateToSingleAccount()
Navigate to detail screen with parameter
*
*
navArgument()
Extracts parameters from route
*
*
Uri.encode()/decode()
Handles special characters in URLs
*
*
currentBackStackEntryAsState()
Tracks which screen is active (for highlighting tabs
* */
