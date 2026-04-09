import app.*
import models.*
import utils.*
import menus.*

val MODO_DESARROLLO = true

fun main() {
    System.setOut(java.io.PrintStream(System.out, true, "UTF-8"))

    val gestor = GestorOci()
    
    if (MODO_DESARROLLO) {
        cargarDatosDePrueba(gestor)
    }

    var activo = true

    do {

        println("\n=== LLISTA-OCI ===")
        println("1. Iniciar sessió")
        println("0. Apagar sistema")
        print("Selecciona una opció: ")

        val option = readlnOrNull()

        when (option) {

            "1" -> {

                val userLogged = iniciarSesion(gestor.users)

                if (userLogged == null) {
                    println("accés denegat")
                }
                else {
                    println("Benvingut, ${userLogged.display}")
                    when (userLogged) {
                        is UserSuperAdmin -> {
                            println("Panell de Super Administrador")
                            menuSuperAdmin(gestor)
                        }

                        is UserAdmin -> {
                            println("Panell de Administrador")
                            menuAdmin(gestor)
                        }

                        is UserNormal -> {
                            println("Panell d'Usuari")
                            menuNormal(gestor, userLogged)
                        }
                    }
                }
            }

            "0" -> activo = false

        }
    } while (activo)

}