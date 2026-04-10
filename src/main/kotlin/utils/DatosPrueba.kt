package utils

import app.GestorOci
import models.Categoria
import models.ElementOci
import models.ElementUsuari
import models.Estado
import models.UserNormal

fun cargarDatosDePrueba(gestor: GestorOci) {
    println("🔧 Cargando datos de prueba...")

    // 1. Crear Categories
    val Peliculas = Categoria("Pel·lícules")
    val Juegos = Categoria("Videojocs")

    gestor.crearCategoria(Peliculas)
    gestor.crearCategoria(Juegos)

    // 2. Crear Elements
    val elemento1 = ElementOci(
        id = "P-001",
        titulo = "El Senyor dels Anells",
        descripcion = "Fantasia èpica",
        categoria = Peliculas
    )
    val elemento2 = ElementOci(
        id = "J-001",
        titulo = "The Witcher 3",
        descripcion = "RPG de món obert",
        categoria = Juegos
    )
    
    gestor.crearElemento(elemento1)
    gestor.crearElemento(elemento2)

    // 3. Crear Usuaris (gestor.crearUser ja els afegeix a la llista internament)
    // Paràmetres: username, password, display, admin (boolean)
    gestor.crearUser("admin", "1234", "Administrador Principal", true)
    gestor.crearUser("user", "1234", "Usuari Normal", false)

    // 4. Afegir l'element P-001 a l'usuari normal
    // Busquem a l'usuari que acabem de crear (sabem que és UserNormal i té username "user")
    val usuarioNormal = gestor.users.find { it.username == "user" } as? UserNormal
    
    if (usuarioNormal != null) {
        val fichaPrueba = ElementUsuari(
            elementOciId = "P-001",
            estado = Estado.PENDENT
        )
        usuarioNormal.crearElemento(fichaPrueba)
    }

    println("✅ Dades de prova carregades correctament.\n")
}