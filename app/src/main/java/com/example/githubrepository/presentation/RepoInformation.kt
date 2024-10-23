package com.example.githubrepository.presentation

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.githubrepository.domain.model.Repos

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepoInformation(navController: NavController, repo: Repos) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Repo Information") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding).padding(16.dp)
        ) {
            Text(text = "Repo Name: " + repo.name)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "User: " + repo.login)
            Spacer(modifier = Modifier.height(8.dp))
            if (repo.description.isNotEmpty()) {
                Text(text = "Description: " + repo.description)
                Spacer(modifier = Modifier.height(8.dp))
            }
            val readableDate = readableDate(repo.updated_at)
            Text(text = "Updated: " + readableDate)
        }
    }
}