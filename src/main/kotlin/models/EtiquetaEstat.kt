package models

class EtiquetaEstat(
    id: String,
    nom: String,
    val estat: Int
) : Etiqueta(id, nom) {

}