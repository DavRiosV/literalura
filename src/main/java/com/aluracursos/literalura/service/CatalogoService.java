package com.aluracursos.literalura.service;

import com.aluracursos.literalura.dto.*;
import com.aluracursos.literalura.model.Autor;
import com.aluracursos.literalura.model.Libro;
import com.aluracursos.literalura.repository.AutorRepository;
import com.aluracursos.literalura.repository.LibroRepository;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;


@Service
public class CatalogoService {

    private final ConsumoAPI consumoAPI = new ConsumoAPI();
    private final ConvierteDatos conversor = new ConvierteDatos();

    private final LibroRepository libroRepository;
    private final AutorRepository autorRepository;

    public CatalogoService(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    public void buscarLibroPorTitulo(String titulo) {
        String tituloNormalizado = quitarAcentos(titulo.trim().toLowerCase());
        String url = "https://gutendex.com/books/?search=" + tituloNormalizado.replace(" ", "%20");

        String json = consumoAPI.obtenerDatos(url);
        Datos datos = conversor.obtenerDatos(json, Datos.class);

        if (datos.getResults().isEmpty()) {
            System.out.println("⚠ No se encontraron libros con ese título.");
            return;
        }

        LibroResponse libroApi = datos.getResults().get(0);
        AutorResponse autorApi = libroApi.getAuthors().isEmpty() ? null : libroApi.getAuthors().get(0);

        // Verificamos si el autor ya existe en la base
        Autor autorEntidad = null;
        if (autorApi != null) {
            Optional<Autor> autorExistente = autorRepository.findByNombre(autorApi.getNombre());
            if (autorExistente.isPresent()) {
                autorEntidad = autorExistente.get();
            } else {
                autorEntidad = new Autor(
                        autorApi.getNombre(),
                        autorApi.getFechaNacimiento(),
                        autorApi.getFechaFallecimiento()
                );
                autorEntidad = autorRepository.save(autorEntidad);
            }
        }

        // Creamos el libro y lo guardamos
        Libro libroEntidad = new Libro(
                libroApi.getTitulo(),
                libroApi.getIdiomas(),
                libroApi.getNumeroDescargas(),
                autorEntidad
        );

        libroRepository.save(libroEntidad);

        System.out.println("Libro agregado a la base de datos: " + libroEntidad);
    }

    public void listarTodosLosLibros() {
        List<Libro> libros = libroRepository.findAll();
        if (libros.isEmpty()) {
            System.out.println("No hay libros en la base de datos.");
            return;
        }
        libros.forEach(System.out::println);
    }

    public void listarLibrosPorIdioma(String idioma) {
        List<Libro> filtrados = libroRepository.findByIdiomaIgnoreCase(idioma);
        if (filtrados.isEmpty()) {
            System.out.println("No se encontraron libros en ese idioma.");
            return;
        }
        filtrados.forEach(System.out::println);
    }

    public void listarAutores() {
        List<Autor> autores = autorRepository.findAll();
        if (autores.isEmpty()) {
            System.out.println("No hay autores en la base de datos.");
            return;
        }
        autores.forEach(System.out::println);
    }

    public void listarAutoresVivosEnAnio(int anio) {
        List<Autor> autoresVivos = autorRepository.findAutoresVivosEnAnio(anio);
        if (autoresVivos.isEmpty()) {
            System.out.println("No se encontraron autores vivos en el año " + anio);
            return;
        }
        autoresVivos.forEach(System.out::println);
    }

    private String quitarAcentos(String texto) {
        String normalizado = Normalizer.normalize(texto, Normalizer.Form.NFD);
        Pattern patron = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return patron.matcher(normalizado).replaceAll("");
    }

    public void contarLibrosPorIdioma(String idioma) {
        long cantidad = libroRepository.countByIdiomaIgnoreCase(idioma);
        if (cantidad == 0) {
            System.out.println("No se encontraron libros en el idioma '" + idioma + "'.");
        } else {
            System.out.println("Cantidad de libros en '" + idioma + "': " + cantidad);
        }
    }

    public void mostrarConteoPorIdiomas(List<String> idiomas) {
        idiomas.forEach(id -> {
            long c = libroRepository.countByIdiomaIgnoreCase(id);
            System.out.println("• " + id + ": " + c);
        });
    }

    public void mostrarConteoGlobalPorIdioma() {
        var filas = libroRepository.contarLibrosPorCadaIdioma();
        if (filas.isEmpty()) {
            System.out.println("No hay datos de idiomas.");
            return;
        }
        System.out.println("Libros por idioma:");
        for (Object[] row : filas) {
            String idioma = (String) row[0];
            Long count = (Long) row[1];
            System.out.println("• " + idioma + ": " + count);
        }
    }

}
