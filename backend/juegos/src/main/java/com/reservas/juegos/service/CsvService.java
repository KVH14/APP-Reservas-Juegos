package com.reservas.juegos.service;

import com.reservas.juegos.entities.Tarea;
import com.reservas.juegos.factory.TareaFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.File;

public class CsvService {

    // -------------------------------------------------------
    // METODO EXPORTAR: guarda todas las tareas en un archivo CSV
    // -------------------------------------------------------
    public static void exportar(List<Tarea> tareas, String ruta) throws IOException {

        // abre (o crea) el archivo en la ruta indicada
        FileWriter writer = new FileWriter(ruta);

        // escribe la primera linea con los nombres de las columnas
        writer.write("tipo,nombre,prioridad\n");

        // recorre cada tarea y escribe una linea por tarea
        for (Tarea t : tareas) {

            // decide el tipo segun la prioridad
            String tipo;
            if (t.getPrioridad() >= 10) {
                tipo = "urgente";
            } else {
                tipo = "normal";
            }

            // escribe la linea: tipo,nombre,prioridad
            writer.write(tipo + "," + t.getNombre() + "," + t.getPrioridad() + "\n");
        }

        // cierra el archivo para guardar los cambios
        writer.close();
        System.out.println("Tareas guardadas en: " + ruta);
    }

    // -------------------------------------------------------
    // METODO CARGAR: lee las tareas desde un archivo CSV
    // -------------------------------------------------------
    public static List<Tarea> cargar(String ruta) throws IOException {

        // lista vacia donde se guardaran las tareas leidas
        List<Tarea> tareas = new ArrayList<>();

        // abre el archivo con Scanner (igual que leer del teclado pero desde un archivo)
        Scanner lector = new Scanner(new File(ruta));

        // salta la primera linea porque es la cabecera (tipo,nombre,prioridad)
        if (lector.hasNextLine()) {
            lector.nextLine();
        }

        // lee linea por linea hasta que no haya mas
        while (lector.hasNextLine()) {
            String linea = lector.nextLine();

            // separa la linea por comas y guarda cada parte en un arreglo
            // ejemplo: "urgente,Pagar reserva,10" -> ["urgente", "Pagar reserva", "10"]
            String[] partes = linea.split(",");

            // verifica que la linea tenga al menos tipo y nombre
            if (partes.length >= 2) {
                String tipo = partes[0];
                String nombre = partes[1];

                // usa la fabrica para crear la tarea correcta (normal o urgente)
                Tarea tarea = TareaFactory.crear(tipo, nombre);
                tareas.add(tarea);
            }
        }

        // cierra el archivo
        lector.close();
        System.out.println("Se cargaron " + tareas.size() + " tareas desde: " + ruta);
        return tareas;
    }
}
