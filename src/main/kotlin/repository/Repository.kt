package repository

interface Repositorio<T> {
    fun desar(element: T)
    fun trobarTots(): List<T>
    fun eliminar(id: String)
    fun actualitzar(element: T): Boolean
    fun trobarPerId(id: String): T?
    fun trobarPerTitol(titol: String): T?
    fun trobarPerUsuari(usuari: String): T?

}