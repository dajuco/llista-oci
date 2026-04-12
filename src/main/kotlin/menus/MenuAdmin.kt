package menus

import app.*
import models.*
import exceptions.*
import repository.*


fun menuAdmin(gestor: GestorOci) {
     var option: String?
     do {
         println("\n--- Panell de Administrador ---")
         println("1. Crear element")
         println("2. Crear categoria")
         println("0. Sortir")
         println("Selecciona una opció: ")
         option = readlnOrNull()

         when (option) {
             "1" -> crearElemento(gestor)
             "2" -> crearCategoria(gestor)
             "0" -> println("Sortint del panell...")
             else -> println("Opció no vàlida.")
         }
     } while (option != "0")
}


fun crearElemento(gestor: GestorOci) {
    try {
        if (gestor.hayCategoriasDisponibles()) {

            println("--- Crear Nou Element ---")

            println("Introdueix la id: ")
            val id = readln().trim()

            if (id.isBlank())
                throw ValidacioException("l'id de l'element no pot estar buit.")
            if (GestorRepositorio.repositorioElemento.encontrarPorId(id) != null)
                throw ElementDuplicatException("Ja existeix un element amb l'ID: $id")
            val regexId = "^[a-zA-Z0-9_-]+$".toRegex()
            if (!regexId.matches(id))
                throw ValidacioException("l'id conte caràcters no vàlids. Només s'accepten lletres, números o guions.")

            println("Introdueix el títol: ")
            val titulo = readln().trim()

            if (titulo.isBlank())
                throw ValidacioException("El títol no pot estar buit.")
            if (GestorRepositorio.repositorioElemento.encontrarPorTitulo(titulo) != null)
                throw ElementDuplicatException("Ja existeix un element amb el títol: $titulo")

            println("Introdueix la descripció: ")
            val descripcion = readln().trim()

            if (descripcion.isBlank())
                throw ValidacioException("La descripció no pot estar buida.")

            println("Categories existents:")

            println("Categories existents: ${gestor.obtenerCategorias().joinToString { it.nombre }}")

            println("Introdueix la categoria: ")
            val nombreCategoria = readln().trim()
            val categoria = gestor.obtenerCategorias().find { it.id == nombreCategoria }

            if (categoria != null) {
                val elemento = ElementOci(id, titulo, descripcion, categoria)
                gestor.crearElemento(elemento)
            } else {
                throw ElementNoTrobatException("No s'ha trobat cap categoria amb el nom '$nombreCategoria'.")
            }
        }
        else {
            println("No hi ha categories creades.")
        }
    } catch (e: ValidacioException) {
        println("Error: ${e.message}")
    } catch (e: ElementDuplicatException) {
        println("Error: ${e.message}")
    } catch (e: ElementNoTrobatException) {
        println("Error: ${e.message}")
    } catch (e: Exception) {
        println(e.message)
    }

 }

fun crearCategoria(gestor: GestorOci) {
    try {
         println("--- Crear Nova Categoria ---")
         print("Introdueix el nom de la categoria: ")
         val nombre = readln().trim()
         if (nombre.isBlank())
             throw TextBuitException("El nom de la categoria no pot estar buit.")

         if (GestorRepositorio.repositorioCategoria.encontrarPorId(nombre) != null)
             throw ElementDuplicatException("La categoria '$nombre' ja existeix.")

         val categoria = Categoria(nombre, nombre)

         gestor.crearCategoria(categoria)
     } catch (e: TextBuitException) {
         println(e.message)
     } catch (e: ElementDuplicatException) {
         println(e.message)
     } catch (e: Exception) {
         println(e.message)
     }
}
