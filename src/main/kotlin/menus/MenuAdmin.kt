package menus
import app.GestorOci
import models.*


fun menuAdmin(gestor: GestorOci) {
    var option: String?
    do {
        println("\n--- Panel de Administrador ---")
        println("1. Crear elemento")
        println("2. Crear categoria")
        println("0. Salir")
        println("Selecciona una opción: ")
        option = readlnOrNull()

        when (option) {
            "1" -> crearElemento(gestor)
            "2" -> crearCategoria(gestor)
            "0" -> println("Saliendo del panel...")
            else -> println("Opción no válida.")
        }
    } while (option != "0")
}


fun crearElemento(gestor: GestorOci) {

    if (gestor.categories.isNotEmpty()) {

        println("--- Crear Nuevo Elemento ---")
        println("Introduce la id: ")
        val id = readln()
        println("Introduce el titulo: ")
        val titulo = readln()
        println("Introduce la descripcion: ")
        val descripcion = readln()
        println("Categorias existentes:")

        println("Categorías existentes: ${gestor.categories.joinToString { it.nombre }}")

        println("Introduce la categoria: ")
        val nombreCategoria = readln()
        val categoria = gestor.categories.find { it.nombre == nombreCategoria }

        if (categoria != null) {
            val elemento = ElementOci(id, titulo, descripcion, categoria)
            gestor.crearElemento(elemento)
            println("Elemento creado correctamente.")
        } else {
            println("La categoria no existe.")
        }
    }
    else {
        println("No hay categorias creadas.")
    }

}

fun crearCategoria(gestor: GestorOci) {
    println("--- Crear Nueva Categoria ---")
    print("Introduce el nombre de la categoria: ")
    val nombre = readln()
    val categoria = Categoria(nombre)
    gestor.crearCategoria(categoria)
    println("Categoria creada correctamente.")
}



