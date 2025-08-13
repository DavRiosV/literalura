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
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();
    private List<Libro> catalogo = new ArrayList<>();


    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private AutorRepository autorRepository;

    public void buscarLibroPorTitulo(String titulo) {
        String tituloNormalizado = quitarAcentos(titulo.trim().toLowerCase());
        String url = "https://gutendex.com/books/?search=" + tituloNormalizado.replace(" ", "%20");

        String json = consumoAPI.obtenerDatos(url);
        Datos datos = conversor.obtenerDatos(json, Datos.class);

        if (datos.getResults().isEmpty()) {
            System.out.println("⚠ No se encontraron libros con ese título.");
            return;
        }

        Libro libroApi = datos.getResults().get(0);
        Autor autor = libroApi.getAutor().get(0);

        // Guardar autor si no existe
        Autor autorGuardado = autorRepository.save(autor);

        // Guardar libro
        Libro libro = new Libro(libroApi.getTitulo(), libroApi.getIdiomas().get(0), libroApi.getNumeroDescargas(), autorGuardado);
        libroRepository.save(libro);

        System.out.println("✅ Libro agregado a la base de datos: " + libro);
    }

    public void listarTodosLosLibros() {
        if (catalogo.isEmpty()) {
            System.out.println("⚠ No hay libros en el catálogo.");
            return;
        }
        catalogo.forEach(System.out::println);
    }

    public void listarLibrosPorIdioma(String idioma) {
        List<Libro> filtrados = catalogo.stream()
                .filter(l -> !l.getIdiomas().isEmpty() &&
                        l.getIdiomas().get(0).equalsIgnoreCase(idioma))
                .collect(Collectors.toList());

        if (filtrados.isEmpty()) {
            System.out.println("⚠ No se encontraron libros en ese idioma.");
            return;
        }
        filtrados.forEach(System.out::println);
    }

    private String quitarAcentos(String texto) {
        String normalizado = Normalizer.normalize(texto, Normalizer.Form.NFD);
        Pattern patron = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return patron.matcher(normalizado).replaceAll("");
    }

    public void listarAutores() {
        if (catalogo.isEmpty()) {
            System.out.println("⚠ No hay libros en el catálogo.");
            return;
        }

        Set<Autor> autoresUnicos = catalogo.stream()
                .flatMap(libro -> libro.get().stream())
                .collect(Collectors.toSet());

        autoresUnicos.forEach(System.out::println);
    }

    public void listarAutoresVivosEnAnio(int anio) {
        if (catalogo.isEmpty()) {
            System.out.println("⚠ No hay libros en el catálogo.");
            return;
        }

        Set<Autor> autoresVivos = catalogo.stream()
                .flatMap(libro -> libro.getAuthors().stream())
                .filter(autor -> autor.getFechaNacimiento() != null && autor.getFechaNacimiento() <= anio &&
                        (autor.getFechaFallecimiento() == null || autor.getFechaFallecimiento() >= anio))
                .collect(Collectors.toSet());

        if (autoresVivos.isEmpty()) {
            System.out.println("⚠ No se encontraron autores vivos en el año " + anio);
            return;
        }

        autoresVivos.forEach(System.out::println);
    }

}
