package com.aluracursos.literalura.dto;


import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LibroResponse {


    @JsonAlias("title")
    private String titulo;

    @JsonAlias("languages")
    private List<String> idiomas;

    @JsonAlias("download_count")
    private int numeroDescargas;

    @JsonAlias("authors")
    private List<AutorResponse> authors;

    public String getTitulo() { return titulo; }
    public List<String> getIdiomas() { return idiomas; }
    public int getNumeroDescargas() { return numeroDescargas; }
    public List<AutorResponse> getAuthors() { return authors; }
}
