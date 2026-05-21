package connection;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Gestiona la conexión a la base de datos PostgreSQL en Neon.
 * Lee credenciales desde db.properties (Criterio 9 y 19).
 */
public class ConnectionDB {

    private static final String PROPERTIES_FILE = "db.properties";

    public static Connection getConnection() {
        Properties props = new Properties();
        try {
            FileInputStream fis = new FileInputStream(PROPERTIES_FILE);
            props.load(fis);
            fis.close();

            String url = props.getProperty("DB_URL");
            String user = props.getProperty("DB_USER");
            String password = props.getProperty("DB_PASSWORD");

            return DriverManager.getConnection(url, user, password);

        } catch (IOException e) {
            System.out.println("Error: no se encontro db.properrties");
            e.printStackTrace();
            return null;
        } catch (SQLException e) {
            System.out.println("Error al conectar con la base de datos");
            e.printStackTrace();
            return null;
        }
    }
}
