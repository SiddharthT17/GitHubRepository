package com.example.githubrepository.data.repository

import com.example.githubrepository.data.api.ApiService
import com.example.githubrepository.domain.model.Repos
import com.example.githubrepository.domain.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GithubRepository(private val apiService: ApiService) : Repository {
    override suspend fun getRepos(username: String): List<Repos> {
        return withContext(Dispatchers.IO) {
            apiService.getRepos(username).map { repo ->
                Repos(
                    repo.name ?: "",
                    repo.description ?: "No description available",
                    repo.language ?: "",
                    repo.forks ?: 0,
                    repo.owner?.login ?: "",
                    repo.updated_at ?: ""
                )
            }
        }
    }
}