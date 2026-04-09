package menus
import app.GestorOci
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
     if (gestor.elements.isEmpty()) {
         println("No hi ha elements registrats.")
         return
     }
     println("Elements existents: ${gestor.elements.joinToString { it.titulo }}")
}

fun mostrarMisElementos(gestor: GestorOci, userlogged: UserNormal) {
     if (userlogged.elementsUser.isEmpty()) {
         println("No tens elements a la teva llista.")
         return
     }

     userlogged.elementsUser.forEach { elemento ->
         val elementoOriginal = gestor.elements.find { it.id == elemento.elementOciId }
         if (elementoOriginal != null) {
             println("[${elemento.elementOciId}] ${elementoOriginal.titulo} - Estat: ${elemento.estado.descripcion}")
         }
     }
}

fun mostrarCategorias(gestor: GestorOci) {
     if (gestor.categories.isEmpty()) {
         println("No hi ha categories registrades.")
         return
     }
     println("Categories existents: ${gestor.categories.joinToString { it.nombre }}")
}

fun añadirElemento(gestor: GestorOci, userlogged: UserNormal){
    try {
        if (gestor.elements.isEmpty()) {
            println("No hi ha elements disponibles per afegir.")
            return
        }
        else {
            println("Elements disponibles:\n${gestor.elements.joinToString("\n") { "[${it.id}] ${it.titulo}" }}")
        }

        println("\nEscriu quin element vols guardar (introdueix el seu ID):")
        val idElemento = readln().trim()

        if (idElemento.isBlank())
            throw TextBuitException("L'ID de l'element no pot estar buit.")

        val elementoOriginal = gestor.elements.find { it.id == idElemento }

        if (elementoOriginal == null)
            throw ElementNoTrobatException("No s'ha trobat cap element amb l'ID '$idElemento'.")
        if (userlogged.elementsUser.any { it.elementOciId == idElemento })
            throw ElementDuplicatException("Aquest element ja està a la teva llista.")


        val elemento = ElementUsuari(idElemento, estado = Estado.PENDIENTE)
        userlogged.crearElemento(elemento)
        println("Element '${elementoOriginal.titulo}' afegit correctament a la teva llista!")

     } catch (e: ElementNoTrobatException) {
         println(e.message)
     } catch (e: ElementDuplicatException) {
         println(e.message)
     } catch (e: TextBuitException) {
         println(e.message)
     }
}

fun avanzarEstado(gestor: GestorOci, userlogged: UserNormal) {
    try {
     if (userlogged.elementsUser.isEmpty()) {
         throw ElementNoTrobatException("No tens elements per modificar l'estat.")
     }

     println("Elements disponibles:")
     userlogged.elementsUser.forEach { elemento ->
         val elementoOriginal = gestor.elements.find { it.id == elemento.elementOciId }
         if (elementoOriginal == null)
             throw ElementNoTrobatException("L'element amb ID '${elemento.elementOciId}' no es troba a la teva llista.")

         println("[${elemento.elementOciId}] ${elementoOriginal.titulo} - Estat: ${elemento.estado.descripcion}")
     }

     println("Selecciona un element (introdueix el seu ID):")
     val idElemento = readln().trim()

        if (idElemento.isBlank())
            throw TextBuitException("L'ID de l'element no pot estar buit.")


         val elemento = userlogged.elementsUser.find { it.elementOciId == idElemento }
             ?: throw ElementNoTrobatException("L'element amb ID '$idElemento' no es troba a la teva llista.")

         elemento.AvanzarEstado()
         println("Estat avançat a: ${elemento.estado.descripcion}")
     } catch (e: ElementNoTrobatException) {
         println("Error: ${e.message}")
     } catch (e: TextBuitException) {
         println("Error: ${e.message}")
     }
}

fun retrocederEstado(gestor: GestorOci, userlogged: UserNormal) {
     if (userlogged.elementsUser.isEmpty()) {
         println("No tens elements per modificar l'estat.")
         return
     }

     println("Elements disponibles:")
     userlogged.elementsUser.forEach { elemento ->
         val elementoOriginal = gestor.elements.find { it.id == elemento.elementOciId }
         if (elementoOriginal != null) {
             println("[${elemento.elementOciId}] ${elementoOriginal.titulo} - Estat: ${elemento.estado.descripcion}")
         }
     }

     println("Selecciona un element (introdueix el seu ID):")
     val idElemento = readln().trim()

     try {
         val elemento = userlogged.elementsUser.find { it.elementOciId == idElemento }
             ?: throw ElementNoTrobatException("L'element amb ID '$idElemento' no es troba a la teva llista.")

         elemento.RetrocederEstado()
         println("Estat retrocedido a: ${elemento.estado.descripcion}")
     } catch (e: ElementNoTrobatException) {
         println("Error: ${e.message}")
     }
}
