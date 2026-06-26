package classes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.DatabaseMetaData;

public class DBconnection {
    private static Connection connection;

    /**
     * Establishes a connection to the database.
     * @return A Connection object representing the database connection.
     * @throws SQLException If a database access error occurs.
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                // Load the Derby JDBC driver
                Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
                System.out.println("Derby JDBC driver loaded successfully");
                
                // Create the connection
                connection = DriverManager.getConnection("jdbc:derby:db/CCIT;create=true", "", "");
                System.out.println("Database connection established successfully");
            } catch (ClassNotFoundException e) {
                System.err.println("Derby JDBC driver not found.");
                e.printStackTrace();
                throw new SQLException("Derby JDBC driver not found.", e);
            } catch (SQLException e) {
                System.err.println("Failed to connect to the database.");
                e.printStackTrace();
                throw e;
            }
        }
        return connection;
    }

    /**
     * Closes the database connection.
     * @throws SQLException If a database access error occurs.
     */
    public static void closeConnection() throws SQLException {
        try {
            if (connection != null && !connection.isClosed()) {
                // In Derby, we need to shut down the database properly
                try {
                    DriverManager.getConnection("jdbc:derby:;shutdown=true");
                } catch (SQLException se) {
                    // Derby throws an exception on successful shutdown, so this is expected
                    if (se.getSQLState().equals("XJ015")) {
                        System.out.println("Derby database shut down normally.");
                    } else {
                        throw se;
                    }
                }
                
                connection.close();
                connection = null;
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection:");
            e.printStackTrace();
        }
    }

    /**
     * Main method to test the database connection
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        System.out.println("Testing database connection...");
        
        try {
            // Get a connection to the database
            Connection conn = getConnection();
            System.out.println("Connection successful!");
            
            // Test if the connection works by executing a simple query
            try (Statement stmt = conn.createStatement()) {
                // Get database metadata
                DatabaseMetaData metaData = conn.getMetaData();
                System.out.println("Connected to: " + metaData.getDatabaseProductName() + " " + metaData.getDatabaseProductVersion());
                
                // List existing tables
                System.out.println("\nExisting tables:");
                ResultSet tables = metaData.getTables(null, null, "%", new String[]{"TABLE"});
                boolean tablesExist = false;
                
                while (tables.next()) {
                    tablesExist = true;
                    System.out.println("- " + tables.getString("TABLE_NAME"));
                }
                
                if (!tablesExist) {
                    System.out.println("No tables found. Database is empty.");
                }
            }
            
            // Close the connection when done
            System.out.println("\nClosing connection...");
            closeConnection();
            System.out.println("Connection test completed successfully.");
            
        } catch (SQLException e) {
            System.err.println("Database connection test failed:");
            e.printStackTrace();
        }
    }
}