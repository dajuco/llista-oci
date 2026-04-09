package utils

import models.*


fun iniciarSesion(lista: List<User>): User? {
    println("Iniciar Sessió")
    println("Introdueix l'usuari")
    val userReq = readlnOrNull()?.trim() ?: ""

    println("Introdueix la contrasenya")
    val passwReq = readlnOrNull()?.trim() ?: ""

    return lista.find { it.username == userReq && it.password == passwReq }
}