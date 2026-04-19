package com.reservas.juegos;

import com.reservas.juegos.entities.Tarea;
import com.reservas.juegos.factory.TareaFactory;
import com.reservas.juegos.service.TareaService;

import java.io.File;
import java.util.List;
import java.util.Scanner;

public class MenuConsola {

    public static void main(String[] args) {

        // crea el servicio que maneja la lista de tareas en memoria
        TareaService service = new TareaService();

        // crea el scanner para leer lo que el usuario escribe en la terminal
        Scanner scanner = new Scanner(System.in);

        // -------------------------------------------------------
        // CARGA AUTOMATICA: si el archivo tareas.csv ya existe,
        // carga las tareas guardadas antes de mostrar el menu
        // -------------------------------------------------------
        File archivo = new File("tareas.csv");
        if (archivo.exists()) {
            try {
                service.cargarDesdeCSV("tareas.csv");
            } catch (Exception e) {
                System.out.println("No se pudo cargar el archivo: " + e.getMessage());
            }
        }

        // variable que guarda la opcion que elige el usuario
        int opcion = 0;

        // -------------------------------------------------------
        // MENU PRINCIPAL: se repite hasta que el usuario elija 5
        // -------------------------------------------------------
        while (opcion != 5) {
            System.out.println("\n===== GESTOR DE TAREAS =====");
            System.out.println("1. Agregar tarea");
            System.out.println("2. Listar tareas");
            System.out.println("3. Exportar tareas a CSV");
            System.out.println("4. Cargar tareas desde CSV");
            System.out.println("5. Salir");
            System.out.print("Seleccione una opcion: ");

            // lee el numero que escribe el usuario
            opcion = scanner.nextInt();
            scanner.nextLine(); // limpia el enter que queda en el buffer

            // -------------------------------------------------------
            // OPCION 1: agregar una nueva tarea
            // -------------------------------------------------------
            if (opcion == 1) {
                System.out.print("Tipo de tarea (normal / urgente): ");
                String tipo = scanner.nextLine();

                System.out.print("Nombre de la tarea: ");
                String nombre = scanner.nextLine();

                // usa la fabrica para crear la tarea del tipo correcto
                Tarea tarea = TareaFactory.crear(tipo, nombre);
                service.agregar(tarea);
                System.out.println("Tarea agregada: " + tarea);

            // -------------------------------------------------------
            // OPCION 2: mostrar todas las tareas en pantalla
            // -------------------------------------------------------
            } else if (opcion == 2) {
                List<Tarea> tareas = service.listar();

                if (tareas.isEmpty()) {
                    System.out.println("No hay tareas.");
                } else {
                    System.out.println("\n--- Lista de tareas ---");
                    for (int i = 0; i < tareas.size(); i++) {
                        System.out.println((i + 1) + ". " + tareas.get(i));
                    }
                }

            // -------------------------------------------------------
            // OPCION 3: guardar las tareas en un archivo CSV
            // -------------------------------------------------------
            } else if (opcion == 3) {
                System.out.print("Nombre del archivo CSV (ej: tareas.csv): ");
                String ruta = scanner.nextLine();
                try {
                    service.exportarACSV(ruta);
                } catch (Exception e) {
                    System.out.println("Error al exportar: " + e.getMessage());
                }

            // -------------------------------------------------------
            // OPCION 4: cargar tareas desde un archivo CSV existente
            // -------------------------------------------------------
            } else if (opcion == 4) {
                System.out.print("Nombre del archivo CSV a cargar: ");
                String ruta = scanner.nextLine();
                try {
                    service.cargarDesdeCSV(ruta);
                } catch (Exception e) {
                    System.out.println("Error al cargar: " + e.getMessage());
                }

            // -------------------------------------------------------
            // OPCION 5: guarda automaticamente y cierra el programa
            // -------------------------------------------------------
            } else if (opcion == 5) {
                try {
                    service.exportarACSV("tareas.csv");
                    System.out.println("Tareas guardadas. Hasta luego!");
                } catch (Exception e) {
                    System.out.println("Error al guardar: " + e.getMessage());
                }

            // -------------------------------------------------------
            // CUALQUIER OTRO NUMERO: avisa que la opcion no existe
            // -------------------------------------------------------
            } else {
                System.out.println("Opcion invalida.");
            }
        }

        // cierra el scanner al terminar
        scanner.close();
    }
}
