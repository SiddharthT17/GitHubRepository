package com.example.githubrepository.domain.repository

import com.example.githubrepository.domain.model.Repos
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun getRepos(username: String): List<Repos>
}