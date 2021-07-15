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
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static java.nio.ByteBuffer.allocate;
import static java.nio.channels.SelectionKey.*;
import static java.nio.channels.SelectionKey.OP_READ;

public class MessengerClientNio {
    private Selector selector;
    private ByteBuffer readBuffer;
    BlockingQueue<String> queue;

    //static Logger log = LoggerFactory.getLogger(MessengerClientNio.class);

    public void start(String ServerAddress, int ServerPort, String login) throws IOException {
        readBuffer = allocate(8192);
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        selector = Selector.open();
        socketChannel.register(selector, OP_CONNECT);
        socketChannel.connect(new InetSocketAddress(ServerAddress, ServerPort));
        queue = new ArrayBlockingQueue<>(2);

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
                    e.printStackTrace();
                    //System.out.println("MessengerClientNio.run: queue be notified");
                }
                SelectionKey key = socketChannel.keyFor(selector);
                key.interestOps(OP_WRITE);
                selector.wakeup();
            }
        }).start();

        // в текущем потоке обмен сообщениями с сервером
        while (true) {
            // ждем событий в канале
            selector.select();
            Iterator<SelectionKey> selectedKeys = selector.selectedKeys().iterator();
            while (selectedKeys.hasNext()) {
                SelectionKey key = selectedKeys.next();
                selectedKeys.remove();
                if (!key.isValid()) {
                    continue;
                }
                if (key.isConnectable()) {
                    connect(key);
                } else if (key.isReadable()) {
                    read(key);
                } else if (key.isWritable()) {
                    write(key);
                }
            }
        }
    }

    private void connect(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        socketChannel.finishConnect();
        socketChannel.register(selector, OP_READ);
        System.out.println("[Connected to server]");
    }

    private void read(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        readBuffer.clear();
        int numRead = socketChannel.read(readBuffer);
        readBuffer.flip();
        byte[] dataCopy = new byte[numRead];
        readBuffer.get(dataCopy);
        System.out.println("[Received = '" + new String(dataCopy) +"']");
    }

    private void write(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        String line = queue.poll();
        if (line != null) {
            socketChannel.write(ByteBuffer.wrap(line.getBytes()));
        }
        key.interestOps(OP_READ);
    }

}
