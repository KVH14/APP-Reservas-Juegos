package com.reservas.juegos;

import com.reservas.juegos.entities.Tarea;
import com.reservas.juegos.service.TareaService;
import com.reservas.juegos.strategy.*;

import java.util.*;

public class TestStrategy {

    public static void main(String[] args) {

        List<Tarea> tareas = new ArrayList<>();

        tareas.add(new Tarea("Reservar cancha", 2));
        tareas.add(new Tarea("Pagar reserva", 3));
        tareas.add(new Tarea("Confirmar horario", 1));

        TareaService service = new TareaService();
        Scanner sc = new Scanner(System.in);

        System.out.println("¿Cómo quieres ordenar las tareas?");
        System.out.println("1. Por prioridad");
        System.out.println("2. Por nombre");

        int opcion = sc.nextInt();

        if (opcion == 1) {
            service.setEstrategia(new OrdenPorPrioridad());
        } else {
            service.setEstrategia(new OrdenPorNombre());
        }

        service.ordenarTareas(tareas);

        System.out.println("\nTareas ordenadas:");
        for (Tarea t : tareas) {
            System.out.println(t);
        }
    }
}