package models

import kotlinx.serialization.Serializable

@Serializable
sealed class User {
    abstract val id: String
    abstract val username: String
    abstract val password: String
    abstract val display: String
}