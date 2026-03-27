package models

data class ElementOci(
    val id: String,
    val titulo: String,
    var descripcion: String? = null,
    var estado: Int,
    val categoria: Categoria
)
{
    constructor(id: String, titulo: String, categoria: Categoria) : this(id, titulo, descripcion = null,1, categoria)

    fun AvanzarEstado() {
        if (estado < 3) estado++
    }

    fun RetrocederEstado() {
        if (estado > 1) estado--
    }

}
