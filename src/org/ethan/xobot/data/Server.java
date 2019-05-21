package org.ethan.xobot.data;


public enum Server {
    ALORA("Alora", "2.7a3f69b490212133ea87ab9c7f62fc1ef"),
    IKOV("Ikov", "C229B7ABEA4A2F6B4741980A74E72DEC"),
    DAWNTAINED("Dawntained", "f830d1590b1ac9810cd4a112f8254d9c"),
    SOULPLAY("Soulplay", "");

    private String name;
    private String serverHash;

    Server(String name, String serverHash) {
        this.name = name;
        this.serverHash = serverHash;
    }

    public String getName() {
        return name;
    }

    public String getServerHash() {
        return serverHash;
    }
}
