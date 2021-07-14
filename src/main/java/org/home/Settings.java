package org.home;

public class Settings {
    final int DefaultServerPort = 19000;
    final String DefaultServerAddress = "localhost";

    private int ServerPort;
    private String ServerAddress;

    public int getServerPort() {
        if (ServerPort == 0) ServerPort = DefaultServerPort;
        return ServerPort;
    }

    public String getServerAddress() {
        if (ServerAddress.isEmpty()) ServerAddress = DefaultServerAddress;
        return ServerAddress;
    }

}
