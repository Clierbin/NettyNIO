package com.gupao.vip.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * ClassName:NIOServer3
 * Package:com.gupao.vip.nio
 * description
 *
 * @author:zhangbin
 * @date:2019/6/14
 * @Time:21:23
 * @Hello-Idea
 */
public class NIOServer3 {
    // Selector ByteBuffer
    private int port;
    // 大堂经理
    private Selector selector;

    private ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

    public NIOServer3(int port) {
        this.port = port;
        // 开启一个大堂
        try {
            ServerSocketChannel server = ServerSocketChannel.open();
            server.bind(new InetSocketAddress(this.port));
            // NIO 默认的是阻塞的
            server.configureBlocking(false);
            selector = Selector.open();

            server.register(selector, SelectionKey.OP_ACCEPT);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        new NIOServer3(8888).listen();
    }

    private void listen() {
        System.out.println("服务器所监听的端口为: " + port);
        while (true) {
            try {
                selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    // 通道的状态
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    process(key);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void process(SelectionKey key) throws IOException {
        // 每个KEY代表一种状态
        if (key.isAcceptable()) {
            ServerSocketChannel server = (ServerSocketChannel) key.channel();
            SocketChannel socket = server.accept();
            socket.configureBlocking(false);
            socket.register(selector, SelectionKey.OP_READ);
        } else if (key.isReadable()) {
            SocketChannel socket = (SocketChannel) key.channel();
            int read = socket.read(byteBuffer);
            if (read > 0) {
                byteBuffer.flip();
                String msg = new String(byteBuffer.array(), 0, read);
                socket.register(selector, SelectionKey.OP_WRITE);
                key.attach(msg);
                System.out.println("服务端接受到的信息: " + msg);
            }
        } else if (key.isWritable()) {
            SocketChannel socket = (SocketChannel) key.channel();
            String attachment = (String) key.attachment();
            socket.write(ByteBuffer.wrap(attachment.getBytes()));
            socket.close();
        }
    }
}
