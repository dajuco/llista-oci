# 📚 1. Repository Básico

## ¿QUÉ ES EL PATRÓN REPOSITORY?

El **Repository** es una capa de abstracción que **separa la lógica de tu aplicación de cómo se guardan y recuperan los datos**.

### Analogía del Mundo Real
Imagina que tu aplicación es un **restaurante**:
- **Sin Repository**: El camarero (aplicación) va directamente a la cocina, abre el frigorífico, coge la comida y la prepara. Muy caótico.
- **Con Repository**: El camarero pide al jefe de cocina "quiero 5 kilos de pollo". El jefe de cocina es responsable de dónde sacarlo (frigorífico, congelador, proveedor...).

---

## ARQUITECTURA DEL PATRÓN

### Flujo de Datos
```
┌─────────────────────────────────────────────────────────────┐
│                    TU APLICACIÓN                             │
│              (Main, GestorOci, Menus)                        │
└──────────────────────┬──────────────────────────────────────┘
                       │
                  pide datos
                       │
                       ▼
┌─────────────────────────────────────────────────────────────┐
│              INTERFAZ REPOSITORY<T>                          │
│  (contrato de operaciones: save, findAll, delete, update)  │
└──────────────────────┬──────────────────────────────────────┘
                       │
                  implementa
                       │
                       ▼
┌─────────────────────────────────────────────────────────────┐
│           JSONREPOSITORY<T> (Implementación)                │
│        (lee/escribe en archivos JSON)                       │
└──────────────────────┬──────────────────────────────────────┘
                       │
                   persiste
                       │
                       ▼
┌─────────────────────────────────────────────────────────────┐
│              ARCHIVOS JSON                                   │
│     (elementos.json, categorias.json, usuarios.json)        │
└─────────────────────────────────────────────────────────────┘
```

---

## VENTAJAS

✅ **Separación de responsabilidades**: La aplicación no conoce cómo se guardan los datos
✅ **Fácil de cambiar**: Puedes cambiar JSON por SQLite sin modificar la aplicación
✅ **Testeable**: Puedes crear un mock repository para tests
✅ **Reutilizable**: El mismo repository funciona para cualquier tipo de objeto

---

## COMPONENTES DEL PATRÓN

### 1. EL MODELO (Tu Entidad)
```kotlin
@Serializable
data class ElementOci(
    val id: String,
    val titulo: String,
    val descripcion: String,
    val categoria: Categoria
)
```

**Necesita:**
- Anotación `@Serializable` para convertirse a JSON
- Propiedad `id` única para identificar

---

### 2. LA INTERFAZ REPOSITORY<T>
```kotlin
interface Repository<T> {
    fun save(item: T): Boolean          // Guarda un elemento nuevo
    fun findAll(): List<T>              // Obtiene todos
    fun findById(id: String): T?        // Busca uno por id
    fun delete(id: String): Boolean     // Elimina por id
    fun update(item: T): Boolean        // Modifica uno existente
}
```

**Es el CONTRATO**: Define qué operaciones se pueden hacer, pero NO cómo hacerlas.

---

### 3. LA IMPLEMENTACIÓN: JSONREPOSITORY<T>
```kotlin
class JsonRepository<T>(
    private val fileName: String,           // Nombre del archivo JSON
    private val serializer: KSerializer<T>  // Cómo serializar T
) : Repository<T> {
    // Los métodos que implementan save(), findAll(), delete()...
}
```

**Responsabilidades:**
- Leer datos del archivo JSON
- Escribir datos al archivo JSON
- Gestionar la serialización/deserialización
- Implementar la lógica de cada operación

---

## CÓMO FUNCIONA INTERNAMENTE

### Operación: SAVE (Crear)
```
1. readFromFile()        ← Lee todos los elementos actuales
2. items.add(item)       ← Añade el nuevo elemento
3. writeToFile(items)    ← Escribe toda la lista al JSON
```

**Resultado:** El JSON tiene un elemento más.

---

### Operación: FINDALL (Listar todos)
```
1. readFromFile()        ← Lee el JSON
2. Deserializa           ← Convierte JSON a objetos Kotlin
3. Retorna List<T>       ← La aplicación recibe la lista
```

---

### Operación: UPDATE (Modificar)
```
1. readFromFile()        ← Lee todos los elementos
2. Busca por id          ← Encuentra el que coincide
3. Reemplaza en posición ← Actualiza ese elemento
4. writeToFile(items)    ← Escribe la lista modificada
```

**Diferencia con SAVE:** No añade, modifica.

---

### Operación: DELETE (Eliminar)
```
1. readFromFile()        ← Lee todos los elementos
2. Filtra por id         ← Mantiene solo los que NO coinciden
3. writeToFile(items)    ← Escribe sin el eliminado
```

---

## VENTAJA FUTURA

Si en el futuro cambias a SQLite, **solo cambias JsonRepository**:

```
APLICACIÓN → REPOSITORY → JsonRepository → JSON
                         ↓
                    (cambiar esto)
                         ↓
                         SQLiteRepository → SQLite

(La aplicación ni se entera)
```

---

## PARA EMPEZAR

👉 Lee la siguiente guía: [`02_IMPLEMENTACION_PASO_A_PASO.md`](./02_IMPLEMENTACION_PASO_A_PASO.md)

