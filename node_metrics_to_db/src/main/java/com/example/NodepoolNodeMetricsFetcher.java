package com.example;

import java.util.ArrayList;
import java.util.List;

import io.fabric8.kubernetes.api.model.Node;
import io.fabric8.kubernetes.api.model.metrics.v1beta1.NodeMetrics;
import io.fabric8.kubernetes.api.model.metrics.v1beta1.NodeMetricsList;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;

public class NodepoolNodeMetricsFetcher {
    public static List<CustomNode> getNodes(Nodepool nodepool) {
        KubernetesClient client = new DefaultKubernetesClient();
        NodeMetricsList nodeMetricsList = client.top().nodes().metrics();
        List<CustomNode> nodes = new ArrayList<CustomNode>();
        for (NodeMetrics nodeMetrics : nodeMetricsList.getItems()) {
            String nodeName = nodeMetrics.getMetadata().getName();
            String currentNodePoolName = nodeMetrics.getMetadata().getLabels().get("agentpool");
            if (!(nodepool.getName().equals(currentNodePoolName))) { // node doesn't belong to this nodepool
                continue;
            }
            Node node = client.nodes().withName(nodeName).get(); // get current node

            String cpuUsage = nodeMetrics.getUsage().get("cpu").toString().replaceAll("[^0-9\\.]+", ""); // remove the
                                                                                                         // metric unit
            Double cpuUsageValue = Double.parseDouble(cpuUsage) / 1000000; // millicores = nanocores / 1000000

            String memoryUsage = nodeMetrics.getUsage().get("memory").toString().replaceAll("[^0-9\\.]+", ""); // remove
                                                                                                               // the
                                                                                                               // metric
                                                                                                               // unit
            Double memoryUsageValue = Double.parseDouble(memoryUsage) / 1024;

            String cpuAvailable = node.getStatus().getCapacity().get("cpu").toString().replaceAll("[^0-9\\.]+", "");
            Double cpuAvailableValue = Double.parseDouble(cpuAvailable) * 1000; // core = millicores * 1000

            String memoryAvailable = node.getStatus().getCapacity().get("memory").toString().replaceAll("[^0-9\\.]+",
                    "");
            Double memoryAvailableValue = Double.parseDouble(memoryAvailable) / 1024; // mebibyte = kibibyte / 1024

            CustomNode tempNode = new CustomNode(nodeName, cpuAvailableValue, memoryAvailableValue, cpuUsageValue,
                    memoryUsageValue);
            nodes.add(tempNode);
        }
        return nodes;
    }
}
