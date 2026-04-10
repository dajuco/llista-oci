package menus

import app.GestorOci
import models.*
import exceptions.*

fun menuSuperAdmin(gestor: GestorOci) {
    var option: String?
     do {
         println("\n--- Panell de Super Administrador ---")
         println("1. Crear usuari")
         println("2. Mostrar usuaris")
         println("0. Sortir")
         print("Selecciona una opció: ")
         option = readlnOrNull()

         when (option) {
             "1" -> crearUser(gestor)
             "2" -> mostrarUsuaris(gestor)
             "0" -> println("Sortint del menú...")
             else -> println("Opció no vàlida.")
         }
     } while (option != "0")
}

fun crearUser(gestor: GestorOci) {

    try {
        println("--- Crear Nou Usuari ---")
        print("Introdueix el nom d'usuari: ")
        val username = readln().trim()

            if (username.isBlank())
                throw TextBuitException("El nom d'usuari no pot estar buit.")
            if (gestor.users.any { it.username.equals(username, ignoreCase = true) })
                throw ElementDuplicatException("El nom d'usuari '$username' ja existeix.")

        print("Introdueix la contrasenya: ")
        val password = readln().trim()

            if (password.isBlank())
                throw TextBuitException("La contrasenya no pot estar buida.")

        print("Introdueix el nom a mostrar (display): ")
        val display = readln().trim()

            if (display.isBlank())
                throw TextBuitException("El nom a mostrar (display) no pot estar buit.")


        print("¿És administrador? (s/n): ")
        val isAdmin = readln().trim().lowercase() == "s"


        gestor.crearUser(username, password, display, isAdmin)
    } catch (e: TextBuitException) {
        println(e.message)
    } catch (e: ElementDuplicatException) {
        println(e.message)
    } catch (e: Exception) {
        println(e.message)
    }
}

fun mostrarUsuaris(gestor: GestorOci) {
    gestor.mostrarUsuariosFormateados()
}