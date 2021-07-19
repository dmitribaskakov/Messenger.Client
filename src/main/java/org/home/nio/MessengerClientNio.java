package org.home.nio;

import org.home.env.Settings;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static java.nio.ByteBuffer.allocate;
import static java.nio.channels.SelectionKey.*;
import static java.nio.channels.SelectionKey.OP_READ;

public class MessengerClientNio {
    private Selector selector;
    private ByteBuffer readBuffer;
    private BlockingQueue<String> queue;
    private String login;

    //static Logger log = LoggerFactory.getLogger(MessengerClientNio.class);

    public void start(Settings settings) throws IOException {
        readBuffer = allocate(8192);
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        selector = Selector.open();
        socketChannel.register(selector, OP_CONNECT);
        socketChannel.connect(new InetSocketAddress(settings.getServerAddress(), settings.getServerPort()));
        queue = new ArrayBlockingQueue<>(4);

        login = settings.getLogin();

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
                    queue.put(parseToMessage(line).ToString());
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
        //noinspection InfiniteLoopStatement
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
        System.out.println("[Connected to server]");

        Message message = new Message();
        message.operation = Message.Operation_Authentication;
        message.from = login;
        try {
            queue.put(message.ToString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        key.interestOps(OP_WRITE);
        System.out.println("[Authentication on server]");
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
        if (line != null) socketChannel.write(ByteBuffer.wrap(line.getBytes()));
        key.interestOps(OP_READ);
    }

    private Message parseToMessage(String line) {
        Message message = new Message();
        if (line != null) {
            message.operation = Message.Operation_Message;
            message.from = login;
            if (line.startsWith("@")) {
                int pos = line.indexOf(" ");
                if (pos > 0) {
                    message.to = line.substring(1, pos);
                    line = line.substring(pos + 1);
                }
            }
            message.body = line;
        }
        return message;
    }

}
