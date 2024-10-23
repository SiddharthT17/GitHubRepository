package com.example.githubrepository.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.githubrepository.domain.model.Repos
import com.example.githubrepository.domain.usecases.GetUserRepositoryUsecase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GithubViewModel(
    private val getUserReposUseCase: GetUserRepositoryUsecase,
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState>(UiState.Success(emptyList()))
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    var githubUsername = mutableStateOf("")

    fun getRepos(username: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val repos = getUserReposUseCase(username)
                _uiState.value = UiState.Success(repos)
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Error fetching data")
            }
        }
    }
}

sealed class UiState {
    data object Loading : UiState()
    data class Success(val repos: List<Repos>) : UiState()
    data class Error(val message: String) : UiState()
}

class GithubViewModelFactory(private val getUserReposUseCase: GetUserRepositoryUsecase) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GithubViewModel::class.java)) {
            return GithubViewModel(getUserReposUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}