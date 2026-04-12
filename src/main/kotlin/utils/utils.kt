package utils

import models.*
import app.*
import repository.*

fun buscarElementoPorId(gestor: GestorOci, id: String): ElementOci? {
    return GestorRepositorio.repositorioElemento.encontrarPorId(id)
}



fun filtrarPorCategoria(gestor: GestorOci, nombreCategoria: String): List<ElementOci> {
    return GestorRepositorio.repositorioElemento.encontrarTodos()
        .filter { it.categoria.nombre.equals(nombreCategoria, ignoreCase = true) }
}