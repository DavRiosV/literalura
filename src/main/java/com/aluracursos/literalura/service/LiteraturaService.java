package com.aluracursos.literalura.service;

import com.aluracursos.literalura.model.Datos;
import com.aluracursos.literalura.model.Libro;
import java.util.ArrayList;
import java.util.List;
import java.text.Normalizer;
import java.util.regex.Pattern;

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
        // 1️⃣ Normalizamos texto
        String tituloNormalizado = quitarAcentos(titulo.trim().toLowerCase());

        // 2️⃣ Construimos URL
        String url = "https://gutendex.com/books/?search=" + tituloNormalizado.replace(" ", "%20");

        // 3️⃣ Consumimos API
        String json = consumoAPI.obtenerDatos(url);

        // 4️⃣ Convertimos a objeto Java
        Datos datos = conversor.obtenerDatos(json, Datos.class);
        List<Libro> resultados = datos.getResults();

        // 5️⃣ Mostramos resultados y guardamos
        if (!resultados.isEmpty()) {
            Libro libro = resultados.get(0); // Nos quedamos con el primero
            catalogo.add(libro);
            System.out.println("✅ Libro agregado al catálogo: " + libro);
        } else {
            System.out.println("⚠ No se encontraron libros con ese título.");
        }
        return resultados;
    }

    // Método auxiliar para quitar acentos
    private String quitarAcentos(String texto) {
        String normalizado = Normalizer.normalize(texto, Normalizer.Form.NFD);
        Pattern patron = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return patron.matcher(normalizado).replaceAll("");
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
