package utils

import app.*
import models.*
import repository.*

fun buscarElementPerId(gestor: GestorOci, id: String): ElementOci? {
    return GestorRepositorio.repositorioElemento.trobarPerId(id)
}

fun filtrarPerCategoria(gestor: GestorOci, nomCategoria: String): List<ElementOci> {
    return GestorRepositorio.repositorioElemento.trobarTots()
        .filter { it.categoria.nombre.equals(nomCategoria, ignoreCase = true) }
}