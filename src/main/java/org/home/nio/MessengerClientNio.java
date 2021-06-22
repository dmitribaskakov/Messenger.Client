package org.home.nio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static java.nio.ByteBuffer.allocate;
import static java.nio.channels.SelectionKey.*;
import static java.nio.channels.SelectionKey.OP_READ;

public class MessengerClientNio {
    private InetSocketAddress serverAddress;
    private Selector selector;
    private ByteBuffer readBuffer;

    static Logger log = LoggerFactory.getLogger(MessengerClientNio.class);

    public void start(String ServerAddress, int ServerPort) throws IOException {
        serverAddress = new InetSocketAddress(ServerAddress, ServerPort);
        readBuffer = allocate(8192);
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        selector = Selector.open();
        socketChannel.register(selector, OP_CONNECT);
        socketChannel.connect(serverAddress);
        BlockingQueue<String> queue = new ArrayBlockingQueue<>(2);

        // создаем отдельный поток на чтение ввода с клавиатуры
        new Thread(() -> {
            System.out.println("Press you message or \\q for exit:");
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String line = scanner.nextLine();
                if ("\\q".equals(line)) {
                    System.exit(0);
                }
                try {
                    queue.put(line);
                } catch (InterruptedException e) {
                    //e.printStackTrace();
                    System.out.println("MessengerClientNio.run: queue be notified");
                }
                SelectionKey key = socketChannel.keyFor(selector);
                key.interestOps(OP_WRITE);
                selector.wakeup();
            }
        }).start();

        // в текущем потоке обмен сообщениями с сервером
        while (true) {
            selector.select();
            for (SelectionKey selectionKey : selector.selectedKeys()) {
                if (selectionKey.isConnectable()) {
                    socketChannel.finishConnect();
                    selectionKey.interestOps(OP_WRITE);
                    System.out.println("[Connected to server]");
                } else if (selectionKey.isReadable()) {
                    readBuffer.clear();
                    socketChannel.read(readBuffer);
                    System.out.println("[Received = '" + new String(readBuffer.array())+"']");
                } else if (selectionKey.isWritable()) {
                    String line = queue.poll();
                    if (line != null) {
                        socketChannel.write(ByteBuffer.wrap(line.getBytes()));
                    }
                    selectionKey.interestOps(OP_READ);
                }
            }
        }
    }
}
