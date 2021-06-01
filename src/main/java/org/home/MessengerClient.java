package org.home;

public class MessengerClient {

    public static void main(String... args) throws Exception {
        System.out.println("MessengerClient: started");

        SimpleSocketClient.test();

        System.out.println("MessengerClient: finished");
    }
}
