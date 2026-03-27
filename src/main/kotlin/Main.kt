import models.*

fun menu() {
    println("LLISTA D'OCI")
    println("1. Crear element")
    println("2. Gestionar estats")
    println("3. Crear categories")
    println("4. Filtrar per estat")
    println("5. Filtrar per categoria")
    println("0. Sortir")
    print("Selecciona una opció: ")
}

fun CrearElement(){

    /*
    val id: String,
    val titulo: String,
    var descripcion: String? = null,
    var estado: Int,
    val categoria: Categoria
    */


    println("Id")
    val id = readlnOrNull() ?: ""
    println("Titulo")
    val titulo = readlnOrNull() ?: ""
    println("Descripcion")
    val descripcion = readlnOrNull() ?: ""
    println("categoria")
    val categoria = readlnOrNull() ?: ""

    
}

fun main(args: Array<String>) {
    var opcion: Int
    val elements = mutableListOf<ElementOci>()
    val categories = mutableListOf<Categoria>()
    
    println("Llista-oci Application")
    
    do {
        menu()

        opcion = readlnOrNull()?.toIntOrNull() ?: 0
        
        when (opcion) {
            1 -> {
                println("Opció 'Crear element' seleccionada (pendent d'implementació)")
            }
            2 -> {
                println("Opció 'Gestionar estats' seleccionada (pendent d'implementació)")
            }
            3 -> {
                println("Opció 'Crear categories' seleccionada (pendent d'implementació)")
            }
            4 -> {
                println("Opció 'Filtrar per estat' seleccionada (pendent d'implementació)")
            }
            5 -> {
                println("Opció 'Filtrar per categoria' seleccionada (pendent d'implementació)")
            }
            0 -> println("Adéu!")
            else -> println("Opció no vàlida.")
        }
        println()
    } while (opcion != 0)
}
