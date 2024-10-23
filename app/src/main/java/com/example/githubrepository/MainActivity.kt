package com.example.githubrepository

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.example.githubrepository.data.api.RetrofitInstance
import com.example.githubrepository.data.repository.GithubRepository
import com.example.githubrepository.domain.usecases.GetUserRepositoryUsecase
import com.example.githubrepository.presentation.AppNavigation
import com.example.githubrepository.presentation.GithubViewModel
import com.example.githubrepository.presentation.GithubViewModelFactory
import com.example.githubrepository.ui.theme.GithubRepositoryTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GithubRepositoryTheme {
                val snackbarHostState = remember { SnackbarHostState() }
                val navController = rememberNavController()
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val apiService = RetrofitInstance.retrofitApi
                    val repo = GithubRepository(apiService)
                    val getReposUseCase = GetUserRepositoryUsecase(repo)
                    val viewModel = ViewModelProvider(
                        this,
                        GithubViewModelFactory(getReposUseCase)
                    )[GithubViewModel::class.java]
                    //GitHubMainScreen(viewModel, innerPadding, snackbarHostState)
                    AppNavigation(navController = navController, viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GithubRepositoryTheme {
        Greeting("Android")
    }
}