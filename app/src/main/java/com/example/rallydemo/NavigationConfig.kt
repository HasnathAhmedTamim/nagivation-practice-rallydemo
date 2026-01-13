package com.example.rallydemo

import android.net.Uri
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

sealed class RallyDestination(
//    Sealed classes and interfaces provide controlled inheritance of your class hierarchies.
//    All direct subclasses of a sealed class are known at compile time.
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Overview : RallyDestination(
        route = "overview",
        title = "Overview",
        icon = Icons.Filled.Home
    )

    object Accounts : RallyDestination(
        route = "accounts",
        title = "Accounts",
        icon = Icons.Filled.AccountBalance
    )

    object Bills : RallyDestination(
        route = "bills",
        title = "Bills",
        icon = Icons.Filled.Description
    )
}

val rallyTopDestinations = listOf(
    RallyDestination.Overview,
    RallyDestination.Accounts,
    RallyDestination.Bills
)

object SingleAccount {
    const val baseRoute = "single_account"
    const val argAccountType = "accountType"
    const val route = "$baseRoute/{$argAccountType}"

    fun createRoute(accountType: String) = "$baseRoute/${Uri.encode(accountType)}"
}
