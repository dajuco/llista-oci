package models

import kotlinx.serialization.Serializable

/**
 * Element del catàleg d'oci.
 *
 * @property id identificador únic.
 * @property titulo títol visible.
 * @property descripcion descripció breu.
 * @property categoria categoria associada.
 */
@Serializable

data class ElementOci(
    val id: String,
    val titulo: String,
    var descripcion: String,
    val categoria: Categoria
)

