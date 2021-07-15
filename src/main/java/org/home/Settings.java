package org.home;

import java.util.Scanner;

public class Settings {
    final int DefaultServerPort = 19000;
    final String DefaultServerAddress = "localhost";

    private int ServerPort = DefaultServerPort;
    private String ServerAddress = DefaultServerAddress;
    private String login = "";

    public int getServerPort() {
        if (ServerPort == 0) ServerPort = DefaultServerPort;
        return ServerPort;
    }

    public String getServerAddress() {
        if (ServerAddress.isEmpty()) ServerAddress = DefaultServerAddress;
        return ServerAddress;
    }

    public String getLogin() {
        if (login.isEmpty()) {
            System.out.println("Login:");
            Scanner scanner = new Scanner(System.in);
            String line = scanner.nextLine();
            if ("\\q".equals(line)) System.exit(0);
            login = line;
        };
        return login;
    }
}
