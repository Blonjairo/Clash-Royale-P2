# Clash Royale - Catalogo

Proecto del segundo parcial de Programcion Orientada a Objetos.
Gestion de un catalogo de cartas de Clash Royale con persistencia en PostgreSQL (Neon).

---

## Descripcion

Aplicacion de consola en Java que permite gestionar un catalogo de cartas de Clash Royale, implementando
el patron DAO para la persistencia de datos en una base de datos remota en Neon.


---

## Estructura del Proyecto

```
src/
├── connection/
│   └── ConnectionDB.java     # Gestión de conexión a PostgreSQL
├── dao/
│   ├── CalidadDAO.java       # Operaciones BD para Calidad
│   └── CartaDAO.java         # Operaciones BD para Carta
├── model/
│   ├── Calidad.java          # Modelo de Calidad
│   └── Carta.java            # Modelo de Carta
└── main/
    └── Main.java             # Menú infinito y punto de entrada
```
---

## Modelo de Base de Datos

```sql
CREATE TABLE calidad (
    id     SERIAL PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE carta (
    id           SERIAL PRIMARY KEY,
    nombre       VARCHAR(100) NOT NULL,
    coste_elixir INT NOT NULL CHECK (coste_elixir BETWEEN 1 AND 9),
    tipo         VARCHAR(50) NOT NULL,
    id_calidad   INT NOT NULL,
    CONSTRAINT fk_carta_calidad FOREIGN KEY (id_calidad) REFERENCES calidad(id)
);
```
---
## Configuracion

### Requisitos
- Java JDK 17+
- Driver JDBC PostgreSQL 42.7.3
- Cuenta en [Neon](https://neon.tech)

### Configurar Credenciales

Crear archivo "db.properties" en la raiz del proyecto (NO se sube al repositorio):

```properties
DB_URL=jdbc:postgresql://HOST/neondb?sslmode=require
DB_USER=tu_usuario
DB_PASSWORD=tu_contraseña
```
Este archivo esta en `.gitignore` para proteger las credenciales.

---
## Funcionalidades 
| Opción | Función | Criterio |
|--------|---------|----------|
| 1 | Ver todas las calidades | Consultar TODOS |
| 2 | Buscar calidad por ID | Consultar UNO |
| 3 | Agregar calidad | Adicionar registro |
| 4 | Ver todas las cartas | Consultar TODOS |
| 5 | Buscar carta por ID | Consultar UNO |
| 6 | Agregar carta | Adicionar registro |
| 7 | Filtrar cartas por coste de elixir | Filtro elaborado |
| 0 | Salir | — |

---

## Arrquitectura - Patron DAO

```
Main.java
   │
   ├── CalidadDAO ──→ ConnectionDB ──→ Neon PostgreSQL
   └── CartaDAO   ──→ ConnectionDB ──→ Neon PostgreSQL
```

- **model/** → Clases espejo de las tablas (POJO)
- **dao/** → Toda la lógica de base de datos
- **connection/** → Una sola clase gestiona la conexión
- **main/** → Solo el menú, nunca escribe SQL

---

## Autor
**Juan Andrade (Blonjairo)**
Programacion Orientada a Objetos - Segundo Parcial