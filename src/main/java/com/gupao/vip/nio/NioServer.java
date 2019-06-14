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
 * ClassName:NioServer
 * Package:com.gupao.vip.nio
 * description
 *
 * @author:zhangbin
 * @date:2019/6/14
 * @Time:20:23
 * @Hello-Idea
 */
public class NioServer {

    // 两个重要的因素
    private int port;

    private Selector selector;

    private ByteBuffer byteBuffer=ByteBuffer.allocate(1024);

    public NioServer(int port) {
        this.port=port;
        try {
            // 开启大厅
            ServerSocketChannel server=ServerSocketChannel.open();

            server.bind(new InetSocketAddress(this.port));
            server.configureBlocking(false);
            // 初始化大堂经理
            selector=Selector.open();
            server.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void listen() {
        System.out.println("监听的端口为: " + port);
        try {
            while (true) {
                // 开始轮训
                selector.select();

                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey key =  iterator.next();
                    iterator.remove();
                    process(key);
                }

            }
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void process(SelectionKey key) throws IOException {
        // 对key进行处理

        if(key.isAcceptable()){
            ServerSocketChannel server = (ServerSocketChannel) key.channel();
            // 接受连接
            SocketChannel socket = server.accept();
            socket.configureBlocking(false);
            socket.register(selector,SelectionKey.OP_READ);
        }else if (key.isReadable()){
            SocketChannel socket = (SocketChannel) key.channel();
            int read = socket.read(byteBuffer);
            if (read > 0 ) {
                byteBuffer.flip();
                String msg=new String(byteBuffer.array(),0,read);
                socket.register(selector,SelectionKey.OP_WRITE);
                key.attach(msg);
                System.out.println("接受到的信息是: " + msg);
            }

        }else if(key.isWritable()) {
            SocketChannel socket = (SocketChannel) key.channel();
            String attachment = (String) key.attachment();
            socket.write(ByteBuffer.wrap(attachment.getBytes()));
            // 关闭通道
            socket.close();
        }

    }

    public static void main(String[] args) {
        new NioServer(8888).listen();
    }
}
