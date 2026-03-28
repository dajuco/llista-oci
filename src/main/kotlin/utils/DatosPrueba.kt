package utils

import app.GestorOci
import models.Categoria
import models.ElementOci
import models.ElementUsuari
import models.Estado
import models.UserNormal

fun cargarDatosDePrueba(gestor: GestorOci) {
    println("🔧 Cargando datos de prueba...")

    // 1. Crear Categorías
    val Peliculas = Categoria("Películas")
    val Juegos = Categoria("Videojuegos")
    
    gestor.crearCategoria(Peliculas)
    gestor.crearCategoria(Juegos)

    // 2. Crear Elementos
    val elemento1 = ElementOci(
        id = "P-001",
        titulo = "El Señor de los Anillos",
        descripcion = "Fantasía épica",
        categoria = Peliculas
    )
    val elemento2 = ElementOci(
        id = "J-001",
        titulo = "The Witcher 3",
        descripcion = "RPG de mundo abierto",
        categoria = Juegos
    )
    
    gestor.crearElemento(elemento1)
    gestor.crearElemento(elemento2)

    // 3. Crear Usuarios (gestor.crearUser ya los añade a la lista internamente)
    // Parámetros: username, password, display, admin (boolean)
    gestor.crearUser("admin", "1234", "Administrador Principal", true)
    gestor.crearUser("user", "1234", "Usuario Normal", false)

    // 4. Añadir el elemento P-001 al usuario normal
    // Buscamos al usuario que acabamos de crear (sabemos que es UserNormal y tiene username "user")
    val usuarioNormal = gestor.users.find { it.username == "user" } as? UserNormal
    
    if (usuarioNormal != null) {
        val fichaPrueba = ElementUsuari(
            elementOciId = "P-001",
            estado = Estado.PENDIENTE
        )
        usuarioNormal.crearElemento(fichaPrueba)
    }

    println("✅ Datos de prueba cargados correctamente.\n")
}