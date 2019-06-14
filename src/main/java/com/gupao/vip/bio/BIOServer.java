package com.gupao.vip.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * ClassName:BIOServer
 * Package:com.gupao.vip.bio
 * description
 *
 * @author:zhangbin
 * @date:2019/6/14
 * @Time:20:08
 * @Hello-Idea
 */
public class BIOServer {
    int port;

    // ServerSocket
    ServerSocket serverSocket;
    public BIOServer(int port) {
        this.port=port;
        try {
            serverSocket=new ServerSocket();

            serverSocket.bind(new InetSocketAddress(8080));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new BIOServer(8080).listen();
    }

    private void listen() {
        try {
            while (true){


            Socket socket = serverSocket.accept();
            System.out.println("发送数据的端口为: " + socket.getPort());

            InputStream inputStream = socket.getInputStream();
            byte[] bytes = new byte[1024];
            int read = inputStream.read(bytes);
            if (read > 0) {
                System.out.println("接受到的信息是: " +new String(bytes,0,read));
            }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
