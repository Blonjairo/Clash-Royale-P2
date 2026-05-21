package main;

import connection.ConnectionDB;
import java.sql.Connection;

public class Main {
    public static void  main(String[] args) {

        System.out.println("Conectando a Neon...");
        Connection conn = ConnectionDB.getConnection();

        if (conn != null) {
            System.out.println("✓ Conexión exitosa");
            try {
                conn.close();
                System.out.println("✓ Conexión cerrada");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("✗ Falló la conexión");
        }
    }
}