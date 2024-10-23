package com.example.githubrepository.presentation

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.githubrepository.R
import com.example.githubrepository.domain.model.Repos
import com.google.gson.Gson
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepoMainScreen(
    navController: NavHostController,
    viewModel: GithubViewModel,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("GitHub Repositories") },
            )
        }
    ) { innerPadding ->
        val uiState by viewModel.uiState.collectAsState()
        var repos = remember { mutableStateOf(emptyList<Repos>()) }
        var allRepos by remember { mutableStateOf(emptyList<Repos>()) }
        var progress by remember { mutableStateOf(false) }

        when (val state = uiState) {
            is UiState.Loading -> {
                progress = true
            }

            is UiState.Success -> {
                progress = false
                repos.value = state.repos
                allRepos = state.repos
            }

            is UiState.Error -> Unit
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = viewModel.githubUsername.value,
                    onValueChange = {
                        viewModel.githubUsername.value = it
                    },
                    label = { Text("Enter GitHub username") },
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp)
                )
                Button(
                    onClick = {
                        viewModel.getRepos(viewModel.githubUsername.value)
                    },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text("Search")
                }
            }
            FilterOption { filter ->
                if (filter.equals("All")) {
                    repos.value = allRepos
                } else {
                    repos.value = allRepos.filter { repo ->
                        repo.language == filter
                    }
                }
            }
            if (progress) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            RepoList(repos, navController)
        }
    }
}

@Composable
fun RepoList(
    repos: MutableState<List<Repos>>,
    navController: NavHostController
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        items(repos.value) { repo ->
            Log.d("LazyColumn", "Repo: $repo")
            RepoItem(repo, onClick = {
                // Display repo information in a dialog
                val repoString = Gson().toJson(repo)
                navController.navigate("repoDetailsScreen/${repoString}")
            })
        }
    }
}

@Composable
fun RepoItem(repo: Repos, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(
                onClick = onClick
            ),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            if (repo.name.isNotEmpty()) {
                Text(text = "Repo Name: " + repo.name)
                Spacer(modifier = Modifier.height(8.dp))
            }
            if (repo.description.isNotEmpty()) {
                Text(text = "Description: " + repo.description)
                Spacer(modifier = Modifier.height(8.dp))
            }
            if (repo.language.isNotEmpty()) {
                Text(text = "Language: " + repo.language)
                Spacer(modifier = Modifier.height(8.dp))
            }
            Text(text = "Forks: " + repo.forks.toString())
            Spacer(modifier = Modifier.height(8.dp))
            if (repo.login.isNotEmpty()) {
                Text(text = "User: " + repo.login)
                Spacer(modifier = Modifier.height(8.dp))
            }
            if (repo.updated_at.isNotEmpty()) {
                val readableDate = readableDate(repo.updated_at)
                Text(text = "Last Updated: $readableDate")
            }
        }
    }
}

fun readableDate(updatedAt: String): String {
    // Parse the date string
    val dateTime = OffsetDateTime.parse(updatedAt)

    // Define a formatter for the desired output format
    val formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy HH:mm:ss")

    // Format the date into a readable string
    return dateTime.format(formatter)
}

@Composable
fun FilterOption(onFilterSelected: (String) -> Unit) {
    var selectedFilter by remember { mutableStateOf("All") }
    var showDialog by remember { mutableStateOf(false) }
    val filterOptions = listOf("All", "Java", "Kotlin", "Python", "JavaScript")

    Row(
        modifier = Modifier
            .padding(start = 8.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Filter Language: $selectedFilter", modifier = Modifier.weight(1f))

        IconButton(
            onClick = { showDialog = true }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_filter_list_24),
                contentDescription = "Close"
            )
        }

        // Dialog for selecting filter
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Select a Filter") },
                text = {
                    Column {
                        filterOptions.forEach { option ->
                            Text(
                                text = option,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                                    .clickable {
                                        selectedFilter = option
                                        onFilterSelected(selectedFilter)
                                        showDialog = false
                                    }
                            )
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        showDialog = false
                    }) {
                        Text("Close")
                    }
                }
            )
        }
    }
}