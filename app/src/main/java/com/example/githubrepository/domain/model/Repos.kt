package com.example.githubrepository.domain.model

data class Repos(
    val name: String,
    val description: String,
    val language: String,
    val forks: Int,
    val login: String,
    val updated_at: String
)
