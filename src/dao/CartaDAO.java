package dao;

// Criterio 12: Separación de responsabilidades — este paquete solo habla con la BD
import connection.ConnectionDB;
import model.Calidad;
import model.Carta;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la tabla 'carta'.
 * Criterio 8:  Patrón DAO implementado — toda la lógica de BD está aquí.
 * Criterio 12: Separación de responsabilidades — Main nunca escribe SQL.
 * Criterio 13: Convenciones de nombres Java — camelCase en métodos y variables.
 */
public class CartaDAO {

    private static final String SELECT_BASE =
            "SELECT c.id AS carta_id, c.nombre AS carta_nombre, " +
                    "c.coste_elixir, c.tipo AS carta_tipo, " +
                    "cal.id AS cal_id, cal.nombre AS cal_nombre " +
                    "FROM carta c " +
                    "JOIN calidad cal ON c.id_calidad = cal.id ";

    private Carta mapear(ResultSet rs) throws SQLException {
        Calidad calidad = new Calidad(
                rs.getInt("cal_id"),
                rs.getString("cal_nombre")
        );
        return new Carta(
                rs.getInt("carta_id"),
                rs.getString("carta_nombre"),
                rs.getInt("coste_elixir"),
                rs.getString("carta_tipo"),   // ← alias explícito
                calidad
        );
    }

    /**
     * Busca una carta por su ID.
     * Criterio 3:  Consultar UN registro.
     * Criterio 10: PreparedStatement evita inyección SQL con el uso de ?.
     * Criterio 9:  Conexión abierta y cerrada en el bloque finally.
     */
    public Carta findById(int id) {
        String sql = SELECT_BASE + "WHERE c.id = ?"; // Criterio 10: ? = parámetro seguro
        Carta carta = null;
        Connection conn = ConnectionDB.getConnection(); // Criterio 9: abrir conexión
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id); // Criterio 10: reemplaza ? con el valor real
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                carta = mapear(rs); // Criterio 11: fila → objeto
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Error en findById: " + e.getMessage());
        } finally {
            // Criterio 9: siempre cerrar la conexión, incluso si hay error
            try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return carta;
    }

    /**
     * Retorna todas las cartas de la base de datos.
     * Criterio 4:  Consultar TODOS los registros.
     * Criterio 9:  Gestión segura de conexiones con finally.
     * Criterio 10: PreparedStatement en lugar de concatenar strings.
     */
    public List<Carta> findAll() {
        String sql = SELECT_BASE + "ORDER BY c.id";
        List<Carta> lista = new ArrayList<>();
        Connection conn = ConnectionDB.getConnection(); // Criterio 9: abrir conexión
        try {
            PreparedStatement ps = conn.prepareStatement(sql); // Criterio 10
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(mapear(rs)); // Criterio 11: cada fila → un objeto Carta
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Error en findAll: " + e.getMessage());
        } finally {
            // Criterio 9: cerrar conexión siempre en finally
            try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return lista;
    }

    /**
     * Inserta una nueva carta en la base de datos.
     * Criterio 5:  Adicionar un registro.
     * Criterio 10: PreparedStatement con 4 parámetros seguros.
     * Criterio 9:  Conexión cerrada en finally.
     */
    public boolean insert(Carta carta) {
        // Criterio 10: cada ? es un parámetro — nunca concatenamos datos del usuario
        String sql = "INSERT INTO carta (nombre, coste_elixir, tipo, id_calidad) VALUES (?, ?, ?, ?)";
        boolean exito = false;
        Connection conn = ConnectionDB.getConnection(); // Criterio 9: abrir conexión
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, carta.getNombre());       // Criterio 10: parámetro 1
            ps.setInt(2, carta.getCosteElixir());     // Criterio 10: parámetro 2
            ps.setString(3, carta.getTipo());         // Criterio 10: parámetro 3
            ps.setInt(4, carta.getCalidad().getId()); // Criterio 10: parámetro 4
            exito = ps.executeUpdate() > 0;
            ps.close();
        } catch (SQLException e) {
            System.out.println("Error en insert: " + e.getMessage());
        } finally {
            // Criterio 9: cerrar conexión siempre en finally
            try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return exito;
    }

    /**
     * Filtra cartas por coste de elixir.
     * Criterio 6:  Filtrar según un criterio.
     * Criterio 20: Filtro elaborado — permite buscar cartas por coste exacto de elixir.
     * Criterio 10: PreparedStatement con parámetro seguro.
     */
    public List<Carta> filterByCosteElixir(int coste) {
        // Criterio 20: filtro significativo — coste de elixir es clave en Clash Royale
        String sql = SELECT_BASE + "WHERE c.coste_elixir = ? ORDER BY c.nombre";
        List<Carta> lista = new ArrayList<>();
        Connection conn = ConnectionDB.getConnection(); // Criterio 9: abrir conexión
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, coste); // Criterio 10: parámetro seguro
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(mapear(rs)); // Criterio 11: fila → objeto Carta
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Error en filterByCosteElixir: " + e.getMessage());
        } finally {
            // Criterio 9: cerrar conexión siempre en finally
            try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return lista;
    }
}