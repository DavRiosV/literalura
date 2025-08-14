package com.aluracursos.literalura.service;

import com.aluracursos.literalura.model.*;
import com.aluracursos.literalura.repository.AutorRepository;
import com.aluracursos.literalura.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class CatalogoService {

    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private AutorRepository autorRepository;

    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();

    public void buscarLibroPorTitulo(String titulo) {
        String tituloNormalizado = quitarAcentos(titulo.trim().toLowerCase());
        String url = "https://gutendex.com/books/?search=" + tituloNormalizado.replace(" ", "%20");

        String json = consumoAPI.obtenerDatos(url);
        Datos datos = conversor.obtenerDatos(json, Datos.class);

        if (datos.getResults().isEmpty()) {
            System.out.println("⚠ No se encontraron libros con ese título.");
            return;
        }

        Libro libroApi = datos.getResults().get(0); // Solo tomamos el primero
        Autor autorApi = libroApi.getAuthor().isEmpty() ? null : libroApi.getAuthors().get(0);

        if (autorApi == null) {
            System.out.println("⚠ El libro no tiene autor registrado, no se puede guardar.");
            return;
        }

        // Buscar si el autor ya existe en la BD
        Autor autorExistente = autorRepository.findByNombreIgnoreCase(autorApi.getNombre())
                .orElseGet(() -> {
                    Autor nuevoAutor = new Autor(
                            autorApi.getNombre(),
                            autorApi.getFechaNacimiento(),
                            autorApi.getFechaFallecimiento()
                    );
                    return autorRepository.save(nuevoAutor);
                });

        // Verificar si el libro ya está guardado
        boolean libroYaExiste = libroRepository.existsByTituloIgnoreCase(libroApi.getTitulo());
        if (libroYaExiste) {
            System.out.println("⚠ El libro ya está en la base de datos.");
            return;
        }

        // Crear y guardar el nuevo libro
        Libro nuevoLibro = new Libro(
                libroApi.getTitulo(),
                libroApi.getIdiomas(),
                libroApi.getNumeroDescargas(),
                autorExistente
        );
        libroRepository.save(nuevoLibro);

        System.out.println("✅ Libro agregado al catálogo: " + nuevoLibro);
    }

    public void listarTodosLosLibros() {
        List<Libro> libros = libroRepository.findAll();
        if (libros.isEmpty()) {
            System.out.println("⚠ No hay libros en el catálogo.");
            return;
        }
        libros.forEach(System.out::println);
    }

    public void listarLibrosPorIdioma(String idioma) {
        List<Libro> filtrados = libroRepository.findByIdiomasContainingIgnoreCase(idioma);

        if (filtrados.isEmpty()) {
            System.out.println("⚠ No se encontraron libros en ese idioma.");
            return;
        }
        filtrados.forEach(System.out::println);
    }

    public void listarAutores() {
        List<Autor> autores = autorRepository.findAll();
        if (autores.isEmpty()) {
            System.out.println("⚠ No hay autores en la base de datos.");
            return;
        }
        autores.forEach(System.out::println);
    }

    public void listarAutoresVivosEnAnio(int anio) {
        List<Autor> autoresVivos = autorRepository.findAutoresVivosEnAnio(anio);
        if (autoresVivos.isEmpty()) {
            System.out.println("⚠ No se encontraron autores vivos en el año " + anio);
            return;
        }
        autoresVivos.forEach(System.out::println);
    }

    // Elimina acentos y normaliza texto
    private String quitarAcentos(String texto) {
        String normalizado = Normalizer.normalize(texto, Normalizer.Form.NFD);
        Pattern patron = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return patron.matcher(normalizado).replaceAll("");
    }
}
