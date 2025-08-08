package com.aluracursos.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Libro {
    @JsonAlias("title")
    private String titulo;

    @JsonAlias("languages")
    private List<String> idiomas;

    @JsonAlias("download_count")
    private int numeroDescargas;

    private List<Autor> authors;

    // Getters y setters
    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public List<String> getIdiomas() {
        return idiomas;
    }
    public void setIdiomas(List<String> idiomas) {
        this.idiomas = idiomas;
    }

    public int getNumeroDescargas() {
        return numeroDescargas;
    }
    public void setNumeroDescargas(int numeroDescargas) {
        this.numeroDescargas = numeroDescargas;
    }

    public List<Autor> getAuthors() {
        return authors;
    }
    public void setAuthors(List<Autor> authors) {
        this.authors = authors;
    }

    @Override
    public String toString() {
        return "📚 " + titulo + " | Idioma: " + (idiomas != null && !idiomas.isEmpty() ? idiomas.get(0) : "N/A")
                + " | Descargas: " + numeroDescargas
                + " | Autor: " + (authors != null && !authors.isEmpty() ? authors.get(0).getNombre() : "Desconocido");
    }
}
