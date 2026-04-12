package repository

interface Repositorio<T> {
    fun guardar(elemento: T)
    fun encontrarTodos(): List<T>
    fun eliminar(id: String)
    fun actualizar(elemento: T): Boolean
    fun encontrarPorId(id: String): T?
    fun encontrarPorTitulo(titulo: String): T?
    fun encontrarPorUser(usuario: String): T?

}