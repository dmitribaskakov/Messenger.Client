package org.home;

import org.home.nio.MessengerClientNio;
import org.home.socket.SimpleSocketClient;

public class MessengerClient {

    public static void main(String... args) throws Exception {
        System.out.println("MessengerClient: started");
        final int PORT = 19000;
        final String ADDRESS = "localhost";

        //SimpleSocketClient.test();

        MessengerClientNio client = new MessengerClientNio();
        client.start(ADDRESS, PORT);

        System.out.println("MessengerClient: finished");
    }
}
