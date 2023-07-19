
package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.fabric8.kubernetes.api.model.Node;
import io.fabric8.kubernetes.api.model.metrics.v1beta1.NodeMetrics;
import io.fabric8.kubernetes.api.model.metrics.v1beta1.NodeMetricsList;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;

// public class App {
// public static String username = "3d316df1-3861-47a3-854b-3649a3c801a0";
// public static String password = " kqA8Q~8Xo2-Iwislb91i73MlrDNRETCHLJxRdat7";
// public static String tenant = "dbb04fc7-056d-47a4-ae19-11d02acf6ec7";

// public static void main(String[] args) throws IOException, InterruptedException {
//     Connection connection = connectToDb();
//     KubernetesClient client = new DefaultKubernetesClient();
//     String date = new Date().toString();
//     NodeMetricsList nodeMetricsList = client.top().nodes().metrics();
//     for (NodeMetrics nodeMetrics : nodeMetricsList.getItems()) {
//         String nodeName = nodeMetrics.getMetadata().getName();
//         Node node = client.nodes().withName(nodeName).get(); // get current node
//         String cpuUsage = nodeMetrics.getUsage().get("cpu").toString().replaceAll("[^0-9\\.]+", ""); // remove the
//                                                                                                      // metric unit
//         Double cpuUsageValue = Double.parseDouble(cpuUsage) / 1000000; // millicores = nanocores / 1000000
//         String cpuAvailable = node.getStatus().getCapacity().get("cpu").toString().replaceAll("[^0-9\\.]+", "");
//         Double cpuAvailableValue = Double.parseDouble(cpuAvailable) * 1000; // core = millicores * 1000
//         String memoryAvailable = node.getStatus().getCapacity().get("memory").toString().replaceAll("[^0-9\\.]+",
//                 "");
//         Double memoryAvailableValue = Double.parseDouble(memoryAvailable) / 1024; // mebibyte = kibibyte / 1024
//         String nodePool = nodeMetrics.getMetadata().getLabels().get("agentpool");
//         Double memoryUsage = getMemoryUsage(nodeName, username, password, tenant);
//         createTable(connection, nodePool);
//         boolean success = false;
//         while (!success) { // retry in case of failure
//             try {
//                 PreparedStatement useDatabaseStmt = connection.prepareStatement("USE node_metrics");
//                 useDatabaseStmt.execute();
//                 String insertQuery = String.format(
//                         "INSERT INTO %s (node_name, cpu_usage, memory_usage, timestamp, cpu_available, memory_available ) VALUES (?, ?, ?,?,?,?)",
//                         nodePool);
//                 PreparedStatement nodeMetricsStmt;
//                 nodeMetricsStmt = connection.prepareStatement(insertQuery);
//                 nodeMetricsStmt.setString(1, nodeName);
//                 nodeMetricsStmt.setDouble(2, cpuUsageValue);
//                 nodeMetricsStmt.setDouble(3, memoryUsage);
//                 nodeMetricsStmt.setString(4, date);
//                 nodeMetricsStmt.setDouble(5, cpuAvailableValue);
//                 nodeMetricsStmt.setDouble(6, memoryAvailableValue);
//                 nodeMetricsStmt.executeUpdate();
//                 success = true;
//             } catch (SQLException e) {
//                 Thread.sleep(5000);
//                 e.printStackTrace();
//             }
//         }
//     }
// }

// private static Connection connectToDb() {
//     Connection conn = null;
//     Statement stmt = null;
//     String jdbcUrl = "jdbc:mysql://mysql-service.proactive-autoscaler.svc.cluster.local:3306/";
//     String username = "root";
//     String password = "admin";

//     try {
//         conn = DriverManager.getConnection(jdbcUrl, username, password);
//         stmt = conn.createStatement();

//         ResultSet rs = stmt
//                 .executeQuery(
//                         "SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = 'node_metrics'");
//         if (!rs.next()) {
//             stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS node_metrics");
//             System.out.println("Created database node_metrics");
//         } else {
//             System.out.println("Database 'node_metrics' already exists");
//         }

//     } catch (SQLException e) {
//         System.out.println("Error connecting to database: " + e.getMessage());
//     }
//     return conn;
// }

// private static void createTable(Connection connection, String tableName) {
//     try {
//         PreparedStatement useSchemaStmt = connection.prepareStatement("USE node_metrics");
//         useSchemaStmt.execute();
//         PreparedStatement createTableStmt = connection.prepareStatement(
//                 "CREATE TABLE IF NOT EXISTS " + tableName + " ("
//                         + "id INT NOT NULL AUTO_INCREMENT,"
//                         + "node_name VARCHAR(255) NOT NULL,"
//                         + "  cpu_usage DOUBLE NOT NULL,"
//                         + "  memory_usage DOUBLE NOT NULL,"
//                         + "  cpu_available DOUBLE NOT NULL,"
//                         + "  memory_available DOUBLE NOT NULL,"
//                         + "  timestamp VARCHAR(45) NOT NULL,"
//                         + "  PRIMARY KEY (id)"
//                         + ")");
//         createTableStmt.executeUpdate();
//     } catch (SQLException e) {
//         e.printStackTrace();
//     }
// }

// public static Double getMemoryUsage(String nodeName, String username, String password, String tenant)
//         throws IOException, InterruptedException {
//     // azure login
//     String[] azLoginCommand = { "az", "login", "--service-principal", "-u", username, "-p",
//             password, "--tenant", tenant };
//     String resp1 = runTerminalCommands(azLoginCommand);
//     System.out.println(resp1);
//     // switch context
//     String[] switchContextCommand = { "/bin/bash", "-c",
//             "az aks get-credentials --name PAKCluster --resource-group PAKResourceGroup" };
//     String resp2 = runTerminalCommands(switchContextCommand);
//     System.out.println(resp2);
//     // calculate memory usage without cache and buffer
//     String[] kubectlDebugCommand = { "kubectl", "debug", "node/" + nodeName, "-it",
//             "--image=mcr.microsoft.com/dotnet/runtime-deps:6.0", "--", "awk",
//             "/^MemTotal:/{t=$2}/^MemFree:/{f=$2}/^Cached:/{c=$2}/^Buffers:/{b=$2}END{print t-f-c-b}",
//             "/proc/meminfo" };
//     String memoryUsage = runTerminalCommands(kubectlDebugCommand);
//     // System.out.println("*****************************");
//     // System.out.println(memoryUsage);
//     // System.out.println("*****************************");
//     String[] parts = memoryUsage.split("\n");
//     memoryUsage = parts[parts.length - 1];// last index will always contain the numerical value of memory usage
//     Double memoryUsageValue = Double.parseDouble(memoryUsage) / 1.024 / 1024;// convert from Kilobytes to Mibibytes
//     // delete debugging pods
//     String[] deleteDebugPodsCommand = { "/bin/bash", "-c",
//             "kubectl get pods --all-namespaces --no-headers | awk '{if ($2 ~ /^node-debugger/) print \"kubectl delete pod --namespace=\" $1 \" \" $2}' | sh" };
//     runTerminalCommands(deleteDebugPodsCommand);
//     return memoryUsageValue;
// }

// private static String runTerminalCommands(String[] args) throws IOException, InterruptedException {
//     ProcessBuilder processBuilder = new ProcessBuilder(args);
//     // System.out.println("##############################");
//     // System.out.println(String.join(" ", processBuilder.command().toArray(new
//     // String[0])));
//     // System.out.println("###############################");
//     processBuilder.redirectErrorStream(true);
//     Process process = processBuilder.start();

//     BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//     StringBuilder response = new StringBuilder();
//     String line;
//     while ((line = reader.readLine()) != null) {
//         response.append(line).append("\n");
//     }

//     process.waitFor();
//     return response.toString();
// }
// }
public class App {
    public static void main(String[] args) throws SQLException {
        Connection connection = NodeMetricsDBManager.connectToDb();
        List<String> nodepoolNames = NodepoolDiscovery.getNodepoolsNames();
        List<Nodepool> nodepools = new ArrayList<Nodepool>();
        for (String nodepoolName : nodepoolNames) {
            NodePoolTableChecker.createTable(connection, nodepoolName);
            Nodepool temp = new Nodepool(nodepoolName);
            nodepools.add(temp);
        }
        String date = new Date().toString();
        for (Nodepool nodepool : nodepools) {
            List<CustomNode> nodes = NodepoolNodeMetricsFetcher.getNodes(nodepool);
            nodepool.setNodes(nodes);
            for (CustomNode node : nodes) {
                NodeMetricsDBManager.writeToDb(connection, nodepool, node, date);
            }
        }
    }
}
