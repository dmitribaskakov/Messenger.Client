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

import static java.nio.ByteBuffer.allocate;
import static java.nio.channels.SelectionKey.OP_ACCEPT;
import static java.nio.channels.SelectionKey.OP_CONNECT;

public class MessengerClientNio {
    private InetSocketAddress serverAddress;
    private Selector selector;
    private ByteBuffer readBuffer;

    static Logger log = LoggerFactory.getLogger(MessengerClientNio.class);

    public void start(String ServerAddress, int ServerPort) throws IOException {
        serverAddress = new InetSocketAddress(ServerAddress, ServerPort);
        readBuffer = allocate(8192);
        selector = Selector.open();
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, OP_CONNECT);
        socketChannel.connect(serverAddress);

    }
}
