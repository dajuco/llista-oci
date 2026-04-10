package utils

import models.*


fun iniciarSesion(lista: List<User>): User? {
    println("Iniciar Sessió")
    println("Introdueix l'usuari")
    val userReq = readlnOrNull()?.trim() ?: ""

    println("Introdueix la contrasenya")
    val passwReq = readlnOrNull()?.trim() ?: ""

    if (userReq.isBlank() || passwReq.isBlank()) {
        println("L'usuari i la contrasenya no poden estar buits.")
        return null
    }

    return lista.find { it.username == userReq && it.password == passwReq }
}