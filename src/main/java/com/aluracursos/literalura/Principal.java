package com.aluracursos.literalura;

import com.aluracursos.literalura.model.Datos;
import com.aluracursos.literalura.service.ConsumoAPI;
import com.aluracursos.literalura.service.ConvierteDatos;
import com.aluracursos.literalura.service.LiteraturaService;

import java.util.Scanner;

public class Principal {
    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);
        LiteraturaService literaturaService = new LiteraturaService();

        System.out.println("🔎 Búsqueda de libros");
        System.out.print("Ingrese el título del libro: ");
        String titulo = teclado.nextLine();

        literaturaService.buscarLibrosPorTitulo(titulo);

        System.out.println("\n📋 Mostrando catálogo actual:");
        literaturaService.mostrarCatalogo();
    }
}
