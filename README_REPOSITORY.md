# 🚀 Bienvenido a llista-oci con Repository Pattern

## 📚 Las Guías Están en la Carpeta `docs/`

Hemos creado una carpeta `docs/` con **6 guías completas** sobre el patrón Repository.

```
docs/
├── 00_INDICE.md                      ← EMPIEZA AQUÍ
├── 01_REPOSITORY_BASICO.md           ← Conceptos
├── 02_IMPLEMENTACION_PASO_A_PASO.md  ← Cómo implementar
├── 03_MULTIPLES_REPOSITORIES.md      ← Tu caso: un JSON por entidad
├── 04_LISTAS_ANIDADAS.md             ← Usuarios con sus elementos propios
├── 05_SAVE_vs_UPDATE.md              ← Cuándo usar cada uno
└── 06_TROUBLESHOOTING.md             ← Solucionar problemas
```

---

## ⚡ Quick Start

1. **Lee** [`docs/00_INDICE.md`](./docs/00_INDICE.md)
2. **Lee** [`docs/02_IMPLEMENTACION_PASO_A_PASO.md`](./docs/02_IMPLEMENTACION_PASO_A_PASO.md)
3. **Implementa** los archivos `Repository.kt` y `JsonRepository.kt`
4. **Lee** [`docs/03_MULTIPLES_REPOSITORIES.md`](./docs/03_MULTIPLES_REPOSITORIES.md) (tu caso específico)
5. **Lee** [`docs/04_LISTAS_ANIDADAS.md`](./docs/04_LISTAS_ANIDADAS.md) (si quieres usuarios con sus elementos)

---

## 🎯 Tu Estructura Deseada

✅ Un JSON por **cada clase**:
- `data/elementos.json` → Lista de ElementOci
- `data/categorias.json` → Lista de Categoria
- `data/usuarios.json` → Lista de User

✅ Cada usuario puede tener su **propia lista de elementos** anidada.

---

## 📁 Carpeta de Datos

Crea una carpeta `data/` en la raíz si no existe:

```bash
mkdir -p data
echo "[]" > data/elementos.json
echo "[]" > data/categorias.json
echo "[]" > data/usuarios.json
```

---

## 💡 Ruta de Aprendizaje

```
Concepto Básico (5 min)
        ↓
Implementación (20 min)
        ↓
Múltiples JSON (10 min)  ← TÚ ESTÁS AQUÍ
        ↓
Listas Anidadas (15 min) ← Y AQUÍ
        ↓
SAVE vs UPDATE (10 min)
        ↓
Troubleshooting (según necesites)
```

---

## 🗂️ Estructura Final de tu Proyecto

```
llista-oci/
├── docs/                              ← 📚 GUÍAS
│   ├── 00_INDICE.md
│   ├── 01_REPOSITORY_BASICO.md
│   ├── 02_IMPLEMENTACION_PASO_A_PASO.md
│   ├── 03_MULTIPLES_REPOSITORIES.md
│   ├── 04_LISTAS_ANIDADAS.md
│   ├── 05_SAVE_vs_UPDATE.md
│   └── 06_TROUBLESHOOTING.md
│
├── data/                              ← 📄 JSONs
│   ├── elementos.json
│   ├── categorias.json
│   └── usuarios.json
│
├── src/main/kotlin/
│   ├── repository/                    ← ⚙️ PATRÓN
│   │   ├── Repository.kt              (interfaz genérica)
│   │   └── JsonRepository.kt          (implementación)
│   │
│   ├── models/                        ← 📦 TUS MODELOS
│   │   ├── ElementOci.kt
│   │   ├── Categoria.kt
│   │   ├── User.kt
│   │   ├── UserAdmin.kt
│   │   ├── UserNormal.kt
│   │   ├── UserSuperAdmin.kt
│   │   └── ...
│   │
│   ├── app/
│   │   └── GestorOci.kt               ← USA LOS REPOSITORIES
│   │
│   ├── menus/
│   ├── utils/
│   ├── exceptions/
│   └── Main.kt
│
├── build.gradle.kts
├── README.md
└── ...
```

---

## 🔑 Conceptos Clave

### Repository Pattern
Una **capa de abstracción** entre tu app y los datos.

```
APP → Repository → JSON
APP → Repository → SQLite (en el futuro)
APP → Repository → API REST (en el futuro)
```

### Un JSON por Entidad
- `elementoRepository` → `data/elementos.json`
- `categoriaRepository` → `data/categorias.json`
- `usuarioRepository` → `data/usuarios.json`

### Listas Anidadas
Cada usuario tiene su propia lista de elementos dentro del JSON:
```json
{
  "id": "user_123",
  "username": "juan",
  "elementos": [
    { "id": "elem_1", "titulo": "Libro1" },
    { "id": "elem_2", "titulo": "Libro2" }
  ]
}
```

---

## ✅ Checklist

- [ ] Leí el [`docs/00_INDICE.md`](./docs/00_INDICE.md)
- [ ] Leí el [`docs/02_IMPLEMENTACION_PASO_A_PASO.md`](./docs/02_IMPLEMENTACION_PASO_A_PASO.md)
- [ ] Creé `src/main/kotlin/repository/Repository.kt`
- [ ] Creé `src/main/kotlin/repository/JsonRepository.kt`
- [ ] Añadí `@Serializable` a mis modelos
- [ ] Creé la carpeta `data/`
- [ ] Creé los archivos JSON vacíos
- [ ] Instancié los repositories en `GestorOci`
- [ ] Probé que funciona

---

## 📞 ¿Problemas?

👉 Lee [`docs/06_TROUBLESHOOTING.md`](./docs/06_TROUBLESHOOTING.md)

Contiene soluciones a 14+ problemas comunes.

---

## 🚀 ¡Adelante!

Abre [`docs/00_INDICE.md`](./docs/00_INDICE.md) y empieza a aprender.

¡El patrón Repository va a hacer tu código mucho más limpio! 💪

