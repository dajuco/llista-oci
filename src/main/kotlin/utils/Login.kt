package utils

import models.*


fun iniciarSesion(lista: List<User>): User? {
    println("Iniciar Sesion")
    println("Introduce el usuario")
    val userReq = readlnOrNull()?.trim() ?: ""

    println("Introduce la contraseña")
    val passwReq = readlnOrNull()?.trim() ?: ""

    return lista.find { it.username == userReq && it.password == passwReq }
}