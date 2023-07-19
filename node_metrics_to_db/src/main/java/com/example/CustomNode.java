package com.example;

public class CustomNode {
    String name;
    double cpuAvailable;
    double memoryAvailable;
    double cpuUsage;
    double memoryUsage;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCpuAvailable() {
        return cpuAvailable;
    }

    public void setCpuAvailable(double cpuAvailable) {
        this.cpuAvailable = cpuAvailable;
    }

    public double getMemoryAvailable() {
        return memoryAvailable;
    }

    public void setMemoryAvailable(double memoryAvailable) {
        this.memoryAvailable = memoryAvailable;
    }

    public double getCpuUsage() {
        return cpuUsage;
    }

    public void setCpuUsage(double cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public double getMemoryUsage() {
        return memoryUsage;
    }

    public void setMemoryUsage(double memoryUsage) {
        this.memoryUsage = memoryUsage;
    }

    public CustomNode(String name, double cpuAvailable, double memoryAvailable, double cpuUsage, double memoryUsage) {
        this.name = name;
        this.cpuAvailable = cpuAvailable;
        this.memoryAvailable = memoryAvailable;
        this.cpuUsage = cpuUsage;
        this.memoryUsage = memoryUsage;
    }
    
}
