package menus
import app.GestorOci
import models.*
import exceptions.*

fun menuNormal(gestor: GestorOci, userlogged: UserNormal) {
    var option: String?
    do {
        println("\n--- Panel de Usuario ---")
        println("1. Mostrar todos los elementos")
        println("2. Mostrar mis elementos")
        println("3. Mostrar categorias")
        println("4. Añadir elementos a la lista")
        println("5. Gestionar estados")
        println("0. Salir")
        print("Selecciona una opción: ")
        option = readlnOrNull()

        when (option) {
            "1" -> mostrarElementos(gestor)
            "2" -> mostrarMisElementos(gestor, userlogged)
            "3" -> mostrarCategorias(gestor)
            "4" -> añadirElemento(gestor, userlogged)
            "5" -> gestionarEstados(gestor, userlogged)
            "0" -> println("Saliendo del panel...")
            else -> println("Opción no válida.")
        }
    } while (option != "0")
}

fun gestionarEstados(gestor: GestorOci, userlogged: UserNormal) {
    var option: String?
    do {
        println("\n--- Gestionar Estados ---")
        println("1. Avanzar estado")
        println("2. Retroceder estado")
        println("0. Volver")
        print("Selecciona una opción: ")
        option = readlnOrNull()

        when (option) {
            "1" -> avanzarEstado(gestor, userlogged)
            "2" -> retrocederEstado(gestor, userlogged)
            "0" -> println("Volviendo al menú anterior...")
            else -> println("Opción no válida.")
        }
    } while (option != "0")
}

fun mostrarElementos(gestor: GestorOci){
    if (gestor.elements.isEmpty()) {
        println("No hay elementos registrados.")
        return
    }
    println("Elementos existentes: ${gestor.elements.joinToString { it.titulo }}")
}

fun mostrarMisElementos(gestor: GestorOci, userlogged: UserNormal) {
    if (userlogged.elementsUser.isEmpty()) {
        println("No tienes elementos en tu lista.")
        return
    }
    
    // Operación sobre colección (map y filterNotNull implícito con find)
    userlogged.elementsUser.forEach { elemento ->
        val elementoOriginal = gestor.elements.find { it.id == elemento.elementOciId }
        if (elementoOriginal != null) {
            println("[${elemento.elementOciId}] ${elementoOriginal.titulo} - Estado: ${elemento.estado.descripcion}")
        }
    }
}

fun mostrarCategorias(gestor: GestorOci) {
    if (gestor.categories.isEmpty()) {
        println("No hay categorías registradas.")
        return
    }
    println("Categorías existentes: ${gestor.categories.joinToString { it.nombre }}")
}

fun añadirElemento(gestor: GestorOci, userlogged: UserNormal){
    if (gestor.elements.isEmpty()) {
        println("No hay elementos disponibles para añadir.")
        return
    }
    
    println("Elementos disponibles:\n${gestor.elements.joinToString("\n") { "[${it.id}] ${it.titulo}" }}")

    println("\nEscribe qué elemento quieres guardar (introduce su ID):")
    val idElemento = readln()
    
    // Validamos que el elemento exista en GestorOci
    val elementoOriginal = gestor.elements.find { it.id == idElemento }
    
    try {
        if (elementoOriginal == null) {
            throw ElementNoTrobatException("No s'ha trobat cap element amb l'ID '$idElemento'.")
        }
        
        // Verificamos si ya lo tiene en su lista
        if (userlogged.elementsUser.any { it.elementOciId == idElemento }) {
            throw ElementDuplicatException("Aquest element ja està a la teva llista.")
        }

        val elemento = ElementUsuari(idElemento, estado = Estado.PENDIENTE)
        userlogged.crearElemento(elemento)
        println("Element '${elementoOriginal.titulo}' afegit correctament a la teva llista!")
        
    } catch (e: ElementNoTrobatException) {
        println("Error: ${e.message}")
    } catch (e: ElementDuplicatException) {
        println("Error: ${e.message}")
    } catch (e: Exception) {
        println("S'ha produït un error inesperat: ${e.message}")
    }
}

fun avanzarEstado(gestor: GestorOci, userlogged: UserNormal) {
    if (userlogged.elementsUser.isEmpty()) {
        println("No tienes elementos para modificar el estado.")
        return
    }
    
    println("Elementos disponibles:")
    userlogged.elementsUser.forEach { elemento ->
        val elementoOriginal = gestor.elements.find { it.id == elemento.elementOciId }
        if (elementoOriginal != null) {
            println("[${elemento.elementOciId}] ${elementoOriginal.titulo} - Estado: ${elemento.estado.descripcion}")
        }
    }

    println("Selecciona un elemento (introduce su ID):")
    val idElemento = readln()
    
    try {
        val elemento = userlogged.elementsUser.find { it.elementOciId == idElemento } 
            ?: throw ElementNoTrobatException("L'element amb ID '$idElemento' no es troba a la teva llista.")
            
        elemento.AvanzarEstado()
        println("Estado avanzado a: ${elemento.estado.descripcion}")
    } catch (e: ElementNoTrobatException) {
        println("Error: ${e.message}")
    }
}

fun retrocederEstado(gestor: GestorOci, userlogged: UserNormal) {
    if (userlogged.elementsUser.isEmpty()) {
        println("No tienes elementos para modificar el estado.")
        return
    }
    
    println("Elementos disponibles:")
    userlogged.elementsUser.forEach { elemento ->
        val elementoOriginal = gestor.elements.find { it.id == elemento.elementOciId }
        if (elementoOriginal != null) {
            println("[${elemento.elementOciId}] ${elementoOriginal.titulo} - Estado: ${elemento.estado.descripcion}")
        }
    }

    println("Selecciona un elemento (introduce su ID):")
    val idElemento = readln()
    
    try {
        val elemento = userlogged.elementsUser.find { it.elementOciId == idElemento }
            ?: throw ElementNoTrobatException("L'element amb ID '$idElemento' no es troba a la teva llista.")

        elemento.RetrocederEstado()
        println("Estado retrocedido a: ${elemento.estado.descripcion}")
    } catch (e: ElementNoTrobatException) {
        println("Error: ${e.message}")
    }
}
