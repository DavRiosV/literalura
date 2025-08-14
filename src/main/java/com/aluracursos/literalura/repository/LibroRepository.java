package com.aluracursos.literalura.repository;

import com.aluracursos.literalura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LibroRepository extends JpaRepository<Libro, Long> {
    List<Libro> findByIdiomasContainingIgnoreCase(String idioma);

    // Listar por idioma (case-insensitive)
    @Query("""
           SELECT l FROM Libro l
           JOIN l.idiomas i
           WHERE LOWER(i) = LOWER(:idioma)
           """)
    List<Libro> findByIdiomaIgnoreCase(String idioma);

    // Contar por idioma (case-insensitive)
    @Query("""
           SELECT COUNT(l) FROM Libro l
           JOIN l.idiomas i
           WHERE LOWER(i) = LOWER(:idioma)
           """)
    long countByIdiomaIgnoreCase(String idioma);

    // (Opcional) Conteo global por cada idioma
    @Query("""
           SELECT i, COUNT(l) FROM Libro l
           JOIN l.idiomas i
           GROUP BY i
           """)
    List<Object[]> contarLibrosPorCadaIdioma();
}
