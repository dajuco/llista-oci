package models

import kotlinx.serialization.Serializable

@Serializable

data class Categoria(
    val id: String,
    val nombre: String
)
