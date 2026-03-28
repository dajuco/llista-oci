package utils

import models.*
import app.*

fun buscarElementoPorId(gestor: GestorOci, id: String): ElementOci? {
    return gestor.elements.find { it.id == id }
}



fun filtrarPorCategoria(gestor: GestorOci, nombreCategoria: String): List<ElementOci> {
    return gestor.elements.filter { it.categoria.nombre.equals(nombreCategoria, ignoreCase = true) }
}