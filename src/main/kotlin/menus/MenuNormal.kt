package menus

import app.*
import models.*
import exceptions.*

fun menuNormal(gestor: GestorOci, userlogged: UserNormal) {
     var option: String?
     do {
         println("\n--- Panell d'Usuari ---")
         println("1. Mostrar tots els elements")
         println("2. Mostrar els meus elements")
         println("3. Mostrar categories")
         println("4. Afegir elements a la llista")
         println("5. Gestionar estats")
         println("0. Sortir")
         print("Selecciona una opció: ")
         option = readlnOrNull()

         when (option) {
             "1" -> mostrarElementos(gestor)
             "2" -> mostrarMisElementos(gestor, userlogged)
             "3" -> mostrarCategorias(gestor)
             "4" -> añadirElemento(gestor, userlogged)
             "5" -> gestionarEstados(gestor, userlogged)
             "0" -> println("Sortint del panell...")
             else -> println("Opció no vàlida.")
         }
     } while (option != "0")
}

fun gestionarEstados(gestor: GestorOci, userlogged: UserNormal) {
     var option: String?
     do {
         println("\n--- Gestionar Estats ---")
         println("1. Avançar estat")
         println("2. Retrocedir estat")
         println("0. Tornar")
         print("Selecciona una opció: ")
         option = readlnOrNull()

         when (option) {
             "1" -> avanzarEstado(gestor, userlogged)
             "2" -> retrocederEstado(gestor, userlogged)
             "0" -> println("Tornant al menú anterior...")
             else -> println("Opció no vàlida.")
         }
     } while (option != "0")
}

fun mostrarElementos(gestor: GestorOci){
    gestor.mostrarElementos()
}

fun mostrarMisElementos(gestor: GestorOci, userlogged: UserNormal) {
    gestor.mostrarMisElementos(userlogged)
}

fun mostrarCategorias(gestor: GestorOci) {
    gestor.mostrarCategorias()
}

fun añadirElemento(gestor: GestorOci, userlogged: UserNormal){
    try {
        if (!gestor.hayElementosDisponibles()) {
            println("No hi ha elements disponibles per afegir.")
            return
        }
        else {
            mostrarElementos(gestor)
        }

        println("\nEscriu quin element vols guardar (introdueix el seu ID):")
        val idElemento = readln().trim()

        val titulo = gestor.añadirElementoAUsuario(userlogged, idElemento)
        println("Element '$titulo' afegit correctament a la teva llista!")

     } catch (e: ElementNoTrobatException) {
         println(e.message)
     } catch (e: ElementDuplicatException) {
         println(e.message)
     } catch (e: TextBuitException) {
         println(e.message)
     } catch (e: Exception) {
         println(e.message)
     }
}

fun avanzarEstado(gestor: GestorOci, userlogged: UserNormal) {
    try {
     if (!gestor.usuarioTieneElementos(userlogged)) {
         throw ElementNoTrobatException("No tens elements per modificar l'estat.")
     }

     println("Elements disponibles:")
     gestor.mostrarMisElementos(userlogged)

     println("Selecciona un element (introdueix el seu ID):")
     val idElemento = readln().trim()

        if (idElemento.isBlank())
            throw TextBuitException("L'ID de l'element no pot estar buit.")


        val nuevoEstado = gestor.avanzarEstadoElementoUsuario(userlogged, idElemento)
        println("Estat avançat a: $nuevoEstado")
     } catch (e: ElementNoTrobatException) {
         println("Error: ${e.message}")
     } catch (e: TextBuitException) {
         println("Error: ${e.message}")
     } catch (e: Exception) {
         println(e.message)
     }
}

fun retrocederEstado(gestor: GestorOci, userlogged: UserNormal) {
    try {
     if (!gestor.usuarioTieneElementos(userlogged)) {
         throw ElementNoTrobatException("No tens elements per modificar l'estat.")
     }

     println("Elements disponibles:")
        gestor.mostrarMisElementos(userlogged)

     println("Selecciona un element (introdueix el seu ID):")
     val idElemento = readln().trim()

        if (idElemento.isBlank())
            throw TextBuitException("L'ID de l'element no pot estar buit.")


        val nuevoEstado = gestor.retrocederEstadoElementoUsuario(userlogged, idElemento)
        println("Estat retrocedit a: $nuevoEstado")
     } catch (e: ElementNoTrobatException) {
         println("Error: ${e.message}")
     } catch (e: TextBuitException) {
         println("Error: ${e.message}")
     } catch (e: Exception) {
         println(e.message)
     }
}
