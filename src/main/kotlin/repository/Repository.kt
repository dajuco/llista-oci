package repository

interface Repository<T> {
    fun save(item: T)
    fun findAll(): List<T>
    fun delete(id: String)
    fun update(item: T): Boolean

}