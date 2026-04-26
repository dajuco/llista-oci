import app.*
import models.*
import utils.*
import menus.*

fun main() {
    System.setOut(java.io.PrintStream(System.out, true, "UTF-8"))

    val gestor = GestorOci()

    var activo = true

    do {

        println("\n=== LLISTA-OCI ===")
        println("1. Iniciar sessió")
        println("0. Apagar sistema")
        print("Selecciona una opció: ")

        val option = readlnOrNull()

        when (option) {

            "1" -> {

                val userLogged = iniciarSessio()

                if (userLogged == null) {
                    println("accés denegat")
                }
                else {
                    println("Benvingut, ${userLogged.display}")
                    when (userLogged) {
                        is UserSuperAdmin -> {
                            println("Panell de Super Administrador")
                            menuSuperAdministrador(gestor)
                        }

                        is UserAdmin -> {
                            println("Panell de Administrador")
                            menuAdministrador(gestor)
                        }

                        is UserNormal -> {
                            println("Panell d'Usuari")
                            menuUsuari(gestor, userLogged)
                        }
                    }
                }
            }

            "0" -> activo = false

        }
    } while (activo)

}