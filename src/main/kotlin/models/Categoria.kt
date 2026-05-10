package models

import kotlinx.serialization.Serializable

/**
 * Categoria del catàleg d'oci.
 *
 * @property id identificador únic de la categoria.
 * @property nombre nom visible de la categoria.
 */
@Serializable

data class Categoria(
    val id: String,
    val nombre: String
)
