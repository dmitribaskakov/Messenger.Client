package org.home;

import org.home.env.Settings;
import org.home.env.SettingsManager;
import org.home.nio.MessengerClientNio;

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

//        Setting settings = new Setting();
//        settings.serverAddress = "127.0.0.1";
//        settings.serverPort = 100;
//        settings.login = "nix";
//
//        String jsonString = JSON.toJSONString(settings);
//        System.out.println("json " + jsonString);
//        try {
//            Files.write(Paths.get("Settings.json"), jsonString.getBytes());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        Setting loadsettings;
//        try (Stream<String> lines = Files.lines(Paths.get("Settings.json"))) {
//            String content = lines.collect(Collectors.joining());
//            loadsettings = JSON.parseObject(content, Setting.class);
//        } catch (IOException e) {
//            //java.nio.file.NoSuchFileException
//            e.printStackTrace();
//        }


    }
}
//
//
//class Setting {
////    public String getServerAddress() {
////        return serverAddress;
////    }
////
////    public void setServerAddress(String serverAddress) {
////        this.serverAddress = serverAddress;
////    }
////
////    public int getServerPort() {
////        return serverPort;
////    }
////
////    public void setServerPort(int serverPort) {
////        this.serverPort = serverPort;
////    }
////
////    public String getLogin() {
////        return login;
////    }
////
////    public void setLogin(String login) {
////        this.login = login;
////    }
//
//    @JSONField(name = "ServerAddress")
//    public String serverAddress = "local";
//
//    @JSONField(name = "ServerPort")
//    public int serverPort = 1900;
//
//    @JSONField(name = "Login")
//    public String login = "";
//}