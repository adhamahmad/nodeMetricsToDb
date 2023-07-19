package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class NodeMetricsDBManager {
    public static Connection connectToDb() {
        Connection conn = null;
        Statement stmt = null;
        String jdbcUrl = "jdbc:mysql://mysql-service.proactive-autoscaler.svc.cluster.local:3306/";
        String username = "root";
        String password = "admin";

        try {
            conn = DriverManager.getConnection(jdbcUrl, username, password);
            stmt = conn.createStatement();

            ResultSet rs = stmt
                    .executeQuery(
                            "SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = 'node_metrics'");
            if (!rs.next()) {
                stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS node_metrics");
                System.out.println("Created database node_metrics");
            } else {
                System.out.println("Database 'node_metrics' already exists");
            }

        } catch (SQLException e) {
            System.out.println("Error connecting to database: " + e.getMessage());
        }
        return conn;
    }

    public static void writeToDb(Connection connection, Nodepool nodepool,CustomNode node ,String date) throws SQLException {
        PreparedStatement useSchemaStmt = connection.prepareStatement("USE node_metrics");
        useSchemaStmt.execute();

        String nodepoolName = nodepool.getName();
        String nodeName = node.getName();
        double cpuUsageValue = node.getCpuUsage();
        double memoryUsage = node.getMemoryUsage();
        double memoryAvailableValue= node.getMemoryUsage();
        double cpuAvailableValue = node.getCpuAvailable();

        String insertQuery = String.format(
                "INSERT INTO %s (node_name, cpu_usage, memory_usage, timestamp, cpu_available, memory_available ) VALUES (?, ?, ?,?,?,?)",
                nodepoolName);

        PreparedStatement nodeMetricsStmt;
        nodeMetricsStmt = connection.prepareStatement(insertQuery);
        nodeMetricsStmt.setString(1, nodeName);
        nodeMetricsStmt.setDouble(2, cpuUsageValue);
        nodeMetricsStmt.setDouble(3, memoryUsage);
        nodeMetricsStmt.setString(4, date);
        nodeMetricsStmt.setDouble(5, cpuAvailableValue);
        nodeMetricsStmt.setDouble(6, memoryAvailableValue);
        nodeMetricsStmt.executeUpdate();

    }

}
