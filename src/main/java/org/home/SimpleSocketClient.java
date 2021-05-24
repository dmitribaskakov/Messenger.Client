package org.home;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class SimpleSocketClient {

    public static final int PORT = 19000;
    public static final String HOST = "localhost";

    public static void test() {

        Socket socket = null;
        try {
            socket = new Socket(HOST, PORT);

            try (InputStream in = socket.getInputStream();
                 OutputStream out = socket.getOutputStream()) {

                String line = "Hello from Client!";
                out.write(line.getBytes());
                out.flush();

                byte[] data = new byte[32 * 1024];
                int readBytes = in.read(data);
                line = new String(data, 0, readBytes);
                System.out.print("Server> ");
                System.out.println(line);

            }

        } catch (IOException e) {
            e.printStackTrace();
            // exit, failed to open socket
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (Exception e) {
                    // tsss!
                }
            }
        }

    }

}
