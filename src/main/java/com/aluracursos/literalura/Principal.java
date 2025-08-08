package com.aluracursos.literalura;

import com.aluracursos.literalura.model.Datos;
import com.aluracursos.literalura.service.ConsumoAPI;
import com.aluracursos.literalura.service.ConvierteDatos;

public class Principal {
    public static void main(String[] args) {
        ConsumoAPI consumoAPI = new ConsumoAPI();
        ConvierteDatos conversor = new ConvierteDatos();

        String json = consumoAPI.obtenerDatos("https://gutendex.com/books/?search=dracula");

        Datos datos = conversor.obtenerDatos(json, Datos.class);

        datos.getResults().forEach(System.out::println);
    }
}
