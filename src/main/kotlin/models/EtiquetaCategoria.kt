package models

class EtiquetaCategoria(
    id: String,
    nom: String,
    val categoria: Categoria
) : Etiqueta(id, nom) {

}