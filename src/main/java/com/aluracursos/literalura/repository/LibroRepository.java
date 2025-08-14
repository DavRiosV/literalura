package com.aluracursos.literalura.repository;

import com.aluracursos.literalura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LibroRepository extends JpaRepository<Libro, Long> {
    boolean existsByTituloIgnoreCase(String titulo);
    List<Libro> findByIdiomasContainingIgnoreCase(String idioma);
}
