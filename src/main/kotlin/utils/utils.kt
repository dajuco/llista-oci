package utils

import models.*
import app.*


class Utils{
    companion object{

        fun buscarElementoPorId(gestor: GestorOci, id: String): ElementOci? {
            return gestor.elements.find { it.id == id }

        }

        fun filtrarPorEstado(gestor: GestorOci, idEstado: Int): List<ElementOci> {
            return gestor.elements.filter { it.estado == idEstado }
        }

        fun filtrarPorCategoria(gestor: GestorOci, nombreCategoria: String): List<ElementOci> {
            return gestor.elements.filter { it.categoria.nombre.equals(nombreCategoria, ignoreCase = true) }
        }

    }

}