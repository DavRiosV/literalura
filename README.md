#  Literalura

Aplicación Java creada para deasio de aluraLatam usando **Spring Boot** para consultar información de libros desde la API de [Gutendex](https://gutendex.com/) y gestionarlos en una base de datos **PostgreSQL**.  
Permite buscar libros por título, almacenar autores y libros, filtrar por idioma y mostrar estadísticas.

---

## 🚀 Tecnologías utilizadas

- **Java 17**
- **Spring Boot 3**  
  - Spring Data JPA  
  - Spring Web  
- **PostgreSQL**
- **Jackson** (para manejo de JSON)
- **Maven**
- **Gutendex API** ([https://gutendex.com/](https://gutendex.com/))

---

## 📂 Estructura del proyecto

src
- main
- java
- com.aluracursos.literalura
- dto 
- model # Entidades JPA: Libro, Autor
- repository # Interfaces JpaRepository
- service # Lógica de negocio
- Principal.java
- LiteraturaApplication.java
- resources
- application.properties


## 🛠 Funcionalidades

Buscar libros por título en la API Gutendex y guardarlos en la base de datos
Mostrar catálogo completo de libros registrados
Filtrar libros por idioma
Contar cuántos libros hay por idioma (estadísticas)
Listar autores vivos en un año específico
