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

        int opcion;
        do {
            System.out.println("\n===== 📚 MENÚ LITERALURA =====");
            System.out.println("1️⃣  Buscar libro por título");
            System.out.println("2️⃣  Mostrar catálogo de libros");
            System.out.println("0️⃣  Salir");
            System.out.print("Seleccione una opción: ");

            while (!teclado.hasNextInt()) { // Evita que el usuario ponga texto en vez de número
                System.out.print("❌ Opción inválida. Ingrese un número: ");
                teclado.next();
            }
            opcion = teclado.nextInt();
            teclado.nextLine(); // limpiar buffer

            switch (opcion) {
                case 1:
                    System.out.print("🔎 Ingrese el título del libro: ");
                    String titulo = teclado.nextLine();
                    literaturaService.buscarLibrosPorTitulo(titulo);
                    break;
                case 2:
                    literaturaService.mostrarCatalogo();
                    break;
                case 0:
                    System.out.println("Saliendo del programa. ¡Hasta luego!");
                    break;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
            }
        } while (opcion != 0);
    }
}
