package com.example;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.fabric8.kubernetes.api.model.Node;
import io.fabric8.kubernetes.api.model.NodeList;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;

public class NodepoolDiscovery {
        public static List<String> getNodepoolsNames() {
        KubernetesClient client = new KubernetesClientBuilder().build();
        Set<String> nodePoolNames = new HashSet<>();
        try {
            NodeList nodes = client.nodes().list();
            for (Node node : nodes.getItems()) {
                if (node.getMetadata().getLabels().get("agentpool") != null) {
                    nodePoolNames.add(node.getMetadata().getLabels().get("agentpool"));
                }
            }
        } finally {
            client.close();
        }
        List<String> nodepoolsNames = new ArrayList<>(nodePoolNames); // convert the set to an arraylist
        return nodepoolsNames;
    }   
}
