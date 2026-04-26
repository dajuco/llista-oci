package menus

import app.*
import models.*
import exceptions.*

fun menuUsuari(gestor: GestorOci, userlogged: UserNormal) {
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
              "1" -> mostrarElements(gestor)
              "2" -> mostrarMeusElements(gestor, userlogged)
              "3" -> mostrarCategories(gestor)
              "4" -> afegirElement(gestor, userlogged)
              "5" -> gestionarEstats(gestor, userlogged)
             "0" -> println("Sortint del panell...")
             else -> println("Opció no vàlida.")
         }
     } while (option != "0")
}

fun gestionarEstats(gestor: GestorOci, userlogged: UserNormal) {
     var option: String?
     do {
         println("\n--- Gestionar Estats ---")
         println("1. Avançar estat")
         println("2. Retrocedir estat")
         println("0. Tornar")
         print("Selecciona una opció: ")
         option = readlnOrNull()

         when (option) {
              "1" -> avançarEstat(gestor, userlogged)
              "2" -> retrocedirEstat(gestor, userlogged)
             "0" -> println("Tornant al menú anterior...")
             else -> println("Opció no vàlida.")
         }
     } while (option != "0")
}

fun mostrarElements(gestor: GestorOci){
    gestor.mostrarElements()
}

fun mostrarMeusElements(gestor: GestorOci, userlogged: UserNormal) {
    gestor.mostrarMeusElements(userlogged)
}

fun mostrarCategories(gestor: GestorOci) {
    gestor.mostrarCategories()
}

fun afegirElement(gestor: GestorOci, userlogged: UserNormal){
    try {
        if (!gestor.hiHaElementsDisponibles()) {
            println("No hi ha elements disponibles per afegir.")
            return
        }
        else {
            mostrarElements(gestor)
        }

        println("\nEscriu quin element vols guardar (introdueix el seu ID):")
        val idElemento = readln().trim()

        val titulo = gestor.afegirElementAUsuari(userlogged, idElemento)
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

fun avançarEstat(gestor: GestorOci, userlogged: UserNormal) {
    try {
      if (!gestor.usuariTeElements(userlogged)) {
          throw ElementNoTrobatException("No tens elements per modificar l'estat.")
      }

      println("Elements disponibles:")
      gestor.mostrarMeusElements(userlogged)

      println("Selecciona un element (introdueix el seu ID):")
      val idElemento = readln().trim()

      if (idElemento.isBlank())
          throw TextBuitException("L'ID de l'element no pot estar buit.")

      val nuevoEstado = gestor.avancarEstatElementUsuari(userlogged, idElemento)
      println("Estat avançat a: $nuevoEstado")
    } catch (e: ElementNoTrobatException) {
        println("Error: ${e.message}")
    } catch (e: TextBuitException) {
        println("Error: ${e.message}")
    } catch (e: Exception) {
        println(e.message)
    }
}

fun retrocedirEstat(gestor: GestorOci, userlogged: UserNormal) {
    try {
      if (!gestor.usuariTeElements(userlogged)) {
          throw ElementNoTrobatException("No tens elements per modificar l'estat.")
      }

      println("Elements disponibles:")
      gestor.mostrarMeusElements(userlogged)

      println("Selecciona un element (introdueix el seu ID):")
      val idElemento = readln().trim()

      if (idElemento.isBlank())
          throw TextBuitException("L'ID de l'element no pot estar buit.")

      val nuevoEstado = gestor.retrocedirEstatElementUsuari(userlogged, idElemento)
      println("Estat retrocedit a: $nuevoEstado")
    } catch (e: ElementNoTrobatException) {
        println("Error: ${e.message}")
    } catch (e: TextBuitException) {
        println("Error: ${e.message}")
    } catch (e: Exception) {
        println(e.message)
    }
}
