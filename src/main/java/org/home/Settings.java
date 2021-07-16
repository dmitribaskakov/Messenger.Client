package org.home;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Scanner;

public class Settings {
    final private int DefaultServerPort = 1900;
    final private String DefaultServerAddress = "localhostxxxx";

    @JSONField(name = "ServerAddress")
    private String serverAddress;

    @JSONField(name = "ServerPort")
    private int serverPort;

    @JSONField(name = "Login")
    private String login;

    public String getServerAddress() {
        if ((serverAddress==null) | ((serverAddress!=null) & (serverAddress.isEmpty()))) {
            serverAddress = DefaultServerAddress;
        };
        return serverAddress;
    }

    public int getServerPort() {
        if (serverPort == 0) {
            serverPort = DefaultServerPort;
        }
        return serverPort;
    }

    public String getLogin() {
        if ((login==null) | ((login!=null) & (login.isEmpty()))) {
            System.out.println("Login:");
            Scanner scanner = new Scanner(System.in);
            String line = scanner.nextLine();
            if ("\\q".equals(line)) System.exit(0);
            login = line;
        };
        return login;
    }
}
