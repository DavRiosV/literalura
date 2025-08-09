package com.aluracursos.literalura.service;

import com.aluracursos.literalura.model.Datos;
import com.aluracursos.literalura.model.Libro;
import java.util.ArrayList;
import java.util.List;

public class LiteraturaService {
    private final ConsumoAPI consumoAPI;
    private final ConvierteDatos conversor;
    private final List<Libro> catalogo;

    public LiteraturaService() {
        this.consumoAPI = new ConsumoAPI();
        this.conversor = new ConvierteDatos();
        this.catalogo = new ArrayList<>();
    }

    public List<Libro> buscarLibrosPorTitulo(String titulo) {
        String url = "https://gutendex.com/books/?search=" + titulo.replace(" ", "%20");
        String json = consumoAPI.obtenerDatos(url);

        Datos datos = conversor.obtenerDatos(json, Datos.class);
        List<Libro> resultados = datos.getResults();

        if (!resultados.isEmpty()) {
            Libro libro = resultados.get(0); // Nos quedamos con el primero
            catalogo.add(libro);
            System.out.println("Libro agregado al catálogo: " + libro);
        } else {
            System.out.println("No se encontraron libros con ese título.");
        }
        return resultados;
    }

    public void mostrarCatalogo() {
        if (catalogo.isEmpty()) {
            System.out.println("El catálogo está vacío.");
        } else {
            System.out.println("Catálogo de libros:");
            catalogo.forEach(System.out::println);
        }
    }
}
