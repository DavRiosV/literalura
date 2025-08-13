package com.aluracursos.literalura;

import com.aluracursos.literalura.service.CatalogoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
public class LiteraluraApplication implements CommandLineRunner {

	@Autowired
	private CatalogoService catalogoService;

	private final Scanner scanner = new Scanner(System.in);

	public static void main(String[] args) {
		SpringApplication.run(LiteraluraApplication.class, args);
	}

	@Override
	public void run(String... args) {
		boolean salir = false;

		while (!salir) {
			mostrarMenu();
			int opcion = leerEntero();

			switch (opcion) {
				case 1 -> {
					System.out.print("Ingrese el título del libro: ");
					String titulo = scanner.nextLine();
					catalogoService.buscarLibroPorTitulo(titulo);
				}
				case 2 -> catalogoService.listarTodosLosLibros();
				case 3 -> {
					System.out.print("Ingrese el idioma (ej: en, es, fr): ");
					String idioma = scanner.nextLine();
					catalogoService.listarLibrosPorIdioma(idioma);
				}
				case 4 -> catalogoService.listarAutores();
				case 5 -> {
					System.out.print("Ingrese el año: ");
					int anio = leerEntero();
					if (anio > 0) {
						catalogoService.listarAutoresVivosEnAnio(anio);
					} else {
						System.out.println("Año inválido.");
					}
				}
				case 0 -> {
					System.out.println("Saliendo de la aplicación...");
					salir = true;
				}
				default -> System.out.println("Opción inválida. Intente nuevamente.");
			}
		}
	}

	private void mostrarMenu() {
		System.out.println("""
                \n=== MENÚ PRINCIPAL ===
                1 - Buscar libro por título
                2 - Listar libros registrados
                3 - Listar libros por idioma
                4 - Listar autores registrados
                5 - Listar autores vivos en un año específico
                0 - Salir
                """);
		System.out.print("Seleccione una opción: ");
	}

	private int leerEntero() {
		try {
			return Integer.parseInt(scanner.nextLine().trim());
		} catch (NumberFormatException e) {
			return -1; // Para que caiga en "Opción inválida"
		}
	}
}
