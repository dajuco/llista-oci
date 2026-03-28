package menus

import app.GestorOci

fun menuSuperAdmin(gestor: GestorOci) {
    var option: String?
    do {
        println("\n--- Panel de Super Administrador ---")
        println("1. Crear usuario")
        println("0. Salir")
        print("Selecciona una opción: ")
        option = readlnOrNull()

        when (option) {
            "1" -> {
                crearUser(gestor)
            }
            "0" -> println("Saliendo del panel...")
            else -> println("Opción no válida.")
        }
    } while (option != "0")
}

fun crearUser(gestor: GestorOci) {

    println("--- Crear Nuevo Usuario ---")
    print("Introduce el nombre de usuario: ")
    val username = readln()

    print("Introduce la contraseña: ")
    val password = readln()

    print("Introduce el nombre a mostrar (display): ")
    val display = readln()

    print("¿Es administrador? (s/n): ")
    val isAdmin = readln().lowercase() == "s"


    gestor.crearUser(username, password, display, isAdmin)
    println("Usuario creado correctamente.")
}