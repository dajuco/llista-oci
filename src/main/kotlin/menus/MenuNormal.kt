package menus
import app.GestorOci
import models.*

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

    println("Elementos existentes: ${gestor.elements.joinToString { it.titulo }}")
}

fun mostrarMisElementos(gestor: GestorOci, userlogged: UserNormal) {
    for (elemento in userlogged.elementsUser) {
        val elementoOriginal = gestor.elements.find { it.id == elemento.elementOciId }
        if (elementoOriginal != null) {
            println("[${elemento.elementOciId}] ${elementoOriginal.titulo} - Estado: ${elemento.estado.descripcion}")
        }
    }
}

fun mostrarCategorias(gestor: GestorOci) {
    println("Categorías existentes: ${gestor.categories.joinToString { it.nombre }}")
}

fun añadirElemento(gestor: GestorOci, userlogged: UserNormal){
    println("Elementos disponibles:\n${gestor.elements.joinToString("\n") { "[${it.id}] ${it.titulo}" }}")

    println("\nEscribe qué elemento quieres guardar (introduce su ID):")
    val idElemento = readln()

    val elemento = ElementUsuari(idElemento, estado = Estado.PENDIENTE)

    userlogged.crearElemento(elemento)
}

fun avanzarEstado(gestor: GestorOci, userlogged: UserNormal) {
    println("Elementos disponibles")
    for (elemento in userlogged.elementsUser) {
        val elementoOriginal = gestor.elements.find { it.id == elemento.elementOciId }
        if (elementoOriginal != null) {
            println("[${elemento.elementOciId}] ${elementoOriginal.titulo} - Estado: ${elemento.estado.descripcion}")
        }
    }

    println("Selecciona un elemento")
    val idElemento = readln()
    val elemento = userlogged.elementsUser.find { it.elementOciId == idElemento }

    if (elemento != null) elemento.AvanzarEstado()
}

fun retrocederEstado(gestor: GestorOci, userlogged: UserNormal) {
    println("Elementos disponibles")
    for (elemento in userlogged.elementsUser) {
        val elementoOriginal = gestor.elements.find { it.id == elemento.elementOciId }
        if (elementoOriginal != null) {
            println("[${elemento.elementOciId}] ${elementoOriginal.titulo} - Estado: ${elemento.estado.descripcion}")
        }
    }

    println("Selecciona un elemento")
    val idElemento = readln()
    val elemento = userlogged.elementsUser.find { it.elementOciId == idElemento }

    if (elemento != null) elemento.RetrocederEstado()
}