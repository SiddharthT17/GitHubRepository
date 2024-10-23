package com.example.githubrepository.data.model

data class ResponseRepos(
    var name: String? = null,
    var owner: Owner? = Owner(),
    var description: String? = null,
    var fork: Boolean? = null,
    var updated_at: String? = null,
    var language: String? = null,
    var forks: Int? = null,
)

data class Owner(
    var login: String? = null,
)

