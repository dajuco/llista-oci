package utils

import models.*
import repository.*


fun iniciarSessio(): User? {
    println("Inicia la sessió")
    println("Introdueix l'usuari")
    val usuariRequerit = readlnOrNull()?.trim() ?: ""

    println("Introdueix la contrasenya")
    val contrasenyaRequerida = readlnOrNull()?.trim() ?: ""

    if (usuariRequerit.isBlank() || contrasenyaRequerida.isBlank()) {
        println("L'usuari i la contrasenya no poden estar buits.")
        return null
    }

    return GestorRepositorio.repositorioUsuario
        .trobarTots()
        .find { it.username == usuariRequerit && it.password == contrasenyaRequerida }

}