package com.example.githubrepository.presentation

import com.example.githubrepository.domain.model.Repos
import com.example.githubrepository.domain.usecases.GetUserRepositoryUsecase
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class GithubViewModelTest {

    private lateinit var viewModel: GithubViewModel
    private lateinit var getUserReposUseCase: GetUserRepositoryUsecase

    @Before
    fun setUp() {
        getUserReposUseCase = mock(GetUserRepositoryUsecase::class.java)
        viewModel = GithubViewModel(getUserReposUseCase)
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @Test
    fun `fetchData_updateUiState_whenSuccess`() = runTest {
        val mockRepos = listOf(
            Repos("repo1", "desc1", "owner1", 10, "url1", "date1"),
            Repos("repo2", "desc2", "owner2", 20, "url2", "date2")
        )

        whenever(getUserReposUseCase("username")).thenReturn(mockRepos)

        viewModel.getRepos("username")

        val expectedUiState = UiState.Success(mockRepos)
        assertEquals(expectedUiState, viewModel.uiState.value)
    }

    @Test
    fun `fetchData_updateUiState_whenError`() = runTest {
        val errorMessage = "Error fetching data"
        val exception = RuntimeException(errorMessage) // Use the actual message
        whenever(getUserReposUseCase("username")).thenThrow(exception)

        viewModel.getRepos("username")

        // Wait for the coroutine to finish
        advanceUntilIdle()

        val expectedUiState = UiState.Error(errorMessage) // Ensure this matches the error message
        assertEquals(expectedUiState, viewModel.uiState.value)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // Reset the main dispatcher
    }
}