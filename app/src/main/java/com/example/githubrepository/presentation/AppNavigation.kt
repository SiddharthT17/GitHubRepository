package com.example.githubrepository.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.githubrepository.domain.model.Repos
import com.google.gson.Gson

@Composable
fun AppNavigation(navController: NavHostController, viewModel: GithubViewModel) {
    NavHost(navController = navController, startDestination = "mainScreen") {
        composable("mainScreen") {
            RepoMainScreen(navController = navController, viewModel = viewModel)
        }
        composable("repoDetailsScreen/{repo}") { backStackEntry ->
            val repoString = backStackEntry.arguments?.getString("repo")
            val repo = Gson().fromJson(repoString, Repos::class.java)
            RepoInformation(navController = navController, repo = repo)
        }
    }
}