package dao;

import connection.ConnectionDB;
import model.Calidad;
import org.checkerframework.checker.units.qual.C;
import org.postgresql.util.PSQLException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la tabla 'calidad'.
 * Contiene todas las operaciones de base de datos para Calidad.
 * Criterio 8: Patrón DAO. Criterio 10: PreparedStatement.
 */
public class CalidadDAO {

    /**
     * Busca una calidad por su ID.
     * Criterio 3: Consultar UN registro.
     */
    public Calidad findById(int id) {
        String sql = "SELECT id, nombre FROM calidad WHERE id = ?";
        Calidad calidad = null;

        Connection conn = ConnectionDB.getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // Mapeo objeto-relacional: fila → objeto Java (Criterio 11)
                calidad = new Calidad(
                        rs.getInt("id"),
                        rs.getString("nombre")
                );
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Error en findboy: " + e.getMessage());
        } finally {
            try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return calidad;
    }

    /**
     * Retorna todas las calidades registradas.
     * Criterio 4: Consultar TODOS los registros.
     */
    public List<Calidad> findAll() {
        String sql ="SELECT id, nombre FROM calidad ORDER BY id";
        List<Calidad> lista = new  ArrayList<>();

        Connection conn = ConnectionDB.getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                lista.add(new Calidad(
                        rs.getInt("id"),
                        rs.getString("nombre")
                ));
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Error en findAll: " + e.getMessage());
        } finally {
            try { if (conn != null) conn.close(); } catch (SQLException e) {e.printStackTrace(); }
        }
        return lista;
    }

    /**
     * Inserta una nueva calidad en la base de datos.
     * Criterio 5: Adicionar registro.
     */
    public boolean insert(Calidad calidad) {
        String sql = "INSERT INTO calidad (nombre) VALUES (?)";
        boolean exito = false;

        Connection conn = ConnectionDB.getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, calidad.getNombre());
            int filasAfectadas = ps.executeUpdate();
            exito = filasAfectadas > 0;  // true si se insertó al menos 1 fila
            ps.close();
        } catch (SQLException e) {
            System.out.println("Error en insert: " + e.getMessage());
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return exito;
    }
}
