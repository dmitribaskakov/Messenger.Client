package org.home;

import org.home.env.Settings;
import org.home.env.SettingsManager;
import org.home.nio.Message;
import org.home.nio.MessengerClientNio;

import java.nio.channels.SelectionKey;
import java.util.Scanner;

import static java.nio.channels.SelectionKey.OP_WRITE;

public class MessengerClient {

    //static Logger log = LoggerFactory.getLogger(MessengerClient.class);

    public static void main(String... args) throws Exception {

        SettingsManager settingsManager = new SettingsManager();
        Settings settings = settingsManager.load();
        System.out.println("MessengerClient: started");

        //SimpleSocketClient.test();

        MessengerClientNio client = new MessengerClientNio();
        client.start(settings);

        System.out.println("MessengerClient: finished");

    }
}
