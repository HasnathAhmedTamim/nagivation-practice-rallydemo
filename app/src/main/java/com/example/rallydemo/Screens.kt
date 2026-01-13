package com.example.rallydemo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

//screens in the Rally demo app for navigation purposes.


@Composable
fun OverviewScreen(
    onSeeAllAccounts: () -> Unit,
    onSeeAllBills: () -> Unit,
    onAccountClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Overview", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))
        Button(onClick = onSeeAllAccounts) { Text("See all accounts") }
        Spacer(Modifier.height(8.dp))
        Button(onClick = onSeeAllBills) { Text("See all bills") }
        Spacer(Modifier.height(16.dp))
        Button(onClick = { onAccountClick("Checking") }) {
            Text("Open Checking account")
        }
    }
}



@Composable
fun AccountsScreen(
    onAccountClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Accounts", style = MaterialTheme.typography.headlineMedium)
        // sample content: more items will scroll
        repeat(6) { index ->
            Button(
                onClick = { onAccountClick("Savings$index") },
                modifier = Modifier.padding(top = if (index == 0) 16.dp else 8.dp)
            ) {
                Text("Open Savings account #$index")
            }
        }
    }
}

@Composable
fun BillsScreen(
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Bills", style = MaterialTheme.typography.headlineMedium)
        // add more bill items here; column will scroll if content grows
    }
}

@Composable
fun SingleAccountScreen(
    accountType: String,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Single account: $accountType",
            style = MaterialTheme.typography.headlineMedium
        )
        // add details below; will scroll on overflow
    }
}

/* Preview functions */
@Preview(showBackground = true)
@Composable
fun AccountsScreenPreview() {
    AccountsScreen(onAccountClick = {})
}

@Preview(showBackground = true)
@Composable
fun BillsScreenPreview() {
    BillsScreen()
}

@Preview(showBackground = true)
@Composable
fun SingleAccountScreenPreview() {
    SingleAccountScreen(accountType = "Checking")
}
