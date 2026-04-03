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
        println("1. Iniciar sesión")
        println("0. Apagar sistema")
        print("Selecciona una opción: ")

        val option = readlnOrNull()

        when (option) {

            "1" -> {

                val userLogged = iniciarSesion(gestor.users)

                if (userLogged == null) {
                    println("acceso denegado")
                }
                else {
                    println("Bienvenido, ${userLogged.display}")
                    when (userLogged) {
                        is UserSuperAdmin -> {
                            println("Panel de Super Administrador")
                            menuSuperAdmin(gestor)
                        }

                        is UserAdmin -> {
                            println("Panel de Administrador")
                            menuAdmin(gestor)
                        }

                        is UserNormal -> {
                            println("Panel de Usuario")
                            menuNormal(gestor, userLogged)
                        }
                    }
                }
            }

            "0" -> activo = false

        }
    } while (activo)

}