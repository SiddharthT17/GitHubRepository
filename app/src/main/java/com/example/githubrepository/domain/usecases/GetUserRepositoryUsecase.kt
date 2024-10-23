package com.example.githubrepository.domain.usecases

import com.example.githubrepository.domain.repository.Repository

class GetUserRepositoryUsecase(private val repository: Repository) {
    suspend operator fun invoke(username: String) = repository.getRepos(username)
}