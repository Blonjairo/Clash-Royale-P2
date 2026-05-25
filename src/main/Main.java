package main;

import dao.CalidadDAO;
import dao.CartaDAO;
import model.Calidad;
import java.util.List;
import java.util.Scanner;
import model.Carta;

public class Main
{
    static CalidadDAO calidadDAO = new CalidadDAO();
    static CartaDAO cartaDAO = new CartaDAO();
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args)
    {
        System.out.println("=== Bienvenido al Catalogo de Clash Royale ===");

        //Criterio 7: el while(true) hace el menú infinito
        while (true)
        {
            mostrarMenu();
            int opcion = leerOpcion();

            switch (opcion)
            {
                case 1 -> consultarTodasCalidades();
                case 2 -> consultarCalidadPorId();
                case 3 -> agregaCalidad();
                case 4 -> consultarTodasCartas();
                case 5 -> consultarCartaPorId();
                case 6 -> agregarCarta();
                case 7 -> filtrarCartasPorElixir(); // ¡Opción 7 agregada para que funcione el filtro!
                case 0 ->
                {
                    System.out.println("Hasta luego");
                    return;
                }
                default -> System.out.println("Opcion no validad. Intenta de nuevo.");
            }
        }
    }

    //MENU
    static void mostrarMenu()
    {
        System.out.println("╔══════════════════════════════╗");
        System.out.println("║     CATÁLOGO CLASH ROYALE    ║");
        System.out.println("╠══════════════════════════════╣");
        System.out.println("║  CALIDADES                   ║");
        System.out.println("║  1. Ver todas las calidades  ║");
        System.out.println("║  2. Buscar calidad por ID    ║");
        System.out.println("║  3. Agregar calidad          ║");
        System.out.println("╠══════════════════════════════╣");
        System.out.println("║  CARTAS                      ║");
        System.out.println("║  4. Ver todas las cartas     ║");
        System.out.println("║  5. Buscar carta por ID      ║");
        System.out.println("║  6. Agregar carta            ║");
        System.out.println("║  7. Filtrar por elixir       ║");
        System.out.println("╠══════════════════════════════╣");
        System.out.println("║  0. Salir                    ║");
        System.out.println("╚══════════════════════════════╝");
        System.out.print("Elige una opción: ");
    }

    static int leerOpcion()
    {
        try
        {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e)
        {
            return -1;
        }
    }

    //CALIDADES
    static void consultarTodasCalidades()
    {
        System.out.println("--- Todas las Calidades ---");
        List<Calidad> lista = calidadDAO.findAll();
        if (lista.isEmpty())
        {
            System.out.println("No hay calidades registradas.");
        } else
        {
            lista.forEach(System.out::println);
        }
    }

    //Criterio 3
    static void consultarCalidadPorId()
    {
        System.out.print("Ingresa el ID de la calidad: ");
        try
        {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Calidad c = calidadDAO.findById(id);
            if (c != null)
            {
                System.out.println("Encontrada: " + c);
            } else
            {
                System.out.println("No existe una caliidad con ID " + id);
            }
        } catch (NumberFormatException e)
        {
            System.out.println("ID invalido.");
        }
    }

    //Criterio 5
    static void agregaCalidad()
    {
        System.out.print("Nombre de la nueva Calidad: ");
        String nombre = scanner.nextLine().trim();
        if (nombre.isEmpty())
        {
            System.out.println("el nombre no puede estar vacio.");
            return;
        }
        Calidad nueva = new Calidad();
        nueva.setNombre(nombre);
        boolean ok = calidadDAO.insert(nueva);
        System.out.println(ok ? "✓ Calidad agregada exitosamente." : "✗ No se pudo agregar.");
    }

    //CARTAS
    //Criterio 4
    static void consultarTodasCartas()
    {
        System.out.println("--- Todas las Cartas ---");
        List<Carta> lista = cartaDAO.findAll();
        if (lista.isEmpty())
        {
            System.out.println("No hay cartas registradas.");
        } else
        {
            lista.forEach(System.out::println);
        }
    }

    //Criterio 3
    static void consultarCartaPorId() {
        System.out.print("Ingresa el ID de la carta: ");
        try
        {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Carta c = cartaDAO.findById(id);
            if (c != null)
            {
                System.out.println("Encontrada: " + c);
            } else
            {
                System.out.println("No existe una carta con ID " + id);
            }
        } catch (NumberFormatException e)
        {
            System.out.println("ID invalido.");
        }
    }

    //Criterio 5
    static void agregarCarta()
    {
        System.out.print("Nombre de la carta: ");
        String nombre = scanner.nextLine().trim();

        System.out.print("Coste de elixir (1-9): ");
        int coste;
        try
        {
            coste = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Coste invalido.");
            return;
        }

        System.out.print("Tipo (Tropa/Hechizo/Estructura): ");
        String tipo = scanner.nextLine().trim();

        System.out.println("Calidades disponibles: ");
        calidadDAO.findAll().forEach(c ->
                System.out.println(" " + c.getId() + ". " + c.getNombre())
        );

        System.out.print("Ingresa el ID de la calidad: ");
        int idCalidad;
        try
        {
            idCalidad = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e)
        {
            System.out.println("ID de calidad invalido.");
            return;
        }
        Calidad calidad = calidadDAO.findById(idCalidad);
        if(calidad == null)
        {
            System.out.println("No existe esa calidad.");
            return;
        }

        Carta nueva = new Carta();
        nueva.setNombre(nombre);
        nueva.setCosteElixir(coste);
        nueva.setTipo(tipo);
        nueva.setCalidad(calidad);

        boolean ok = cartaDAO.insert(nueva);
        System.out.println(ok ? "✓ Carta agregada exitosamente." : "✗ No se pudo agregar.");
    }

    //Criterio 6 y 20
    static void filtrarCartasPorElixir() {
        System.out.print("Ingresa el costo de elixir a filtrar (1-9): ");
        try
        {
            int coste = Integer.parseInt(scanner.nextLine().trim());
            List<Carta> lista = cartaDAO.filterByCosteElixir(coste);
            if (lista.isEmpty())
            {
                System.out.println("No hay cartas con coste " + coste + ".");
            } else
            {
                System.out.println("--- Cartas con coste " + coste + " ---");
                lista.forEach(System.out::println);
            }
        } catch (NumberFormatException e)
        {
            System.out.println("Coste invalido.");
        }
    }
}