package models

import kotlinx.serialization.Serializable

@Serializable

data class ElementOci(
    val id: String,
    val titulo: String,
    var descripcion: String,
    val categoria: Categoria
)

