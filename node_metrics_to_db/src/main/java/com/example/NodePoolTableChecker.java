package com.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class NodePoolTableChecker {
    public static void createTable(Connection connection, String tableName) {
        try {
            PreparedStatement useSchemaStmt = connection.prepareStatement("USE node_metrics");
            useSchemaStmt.execute();
            PreparedStatement createTableStmt = connection.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS " + tableName + " ("
                            + "id INT NOT NULL AUTO_INCREMENT,"
                            + "node_name VARCHAR(255) NOT NULL,"
                            + "  cpu_usage DOUBLE NOT NULL,"
                            + "  memory_usage DOUBLE NOT NULL,"
                            + "  cpu_available DOUBLE NOT NULL,"
                            + "  memory_available DOUBLE NOT NULL,"
                            + "  timestamp VARCHAR(45) NOT NULL,"
                            + "  PRIMARY KEY (id)"
                            + ")");
            createTableStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
