package com.gupao.vip.bio;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * ClassName:BIOClient
 * Package:com.gupao.vip.bio
 * description
 *
 * @author:zhangbin
 * @date:2019/6/14
 * @Time:20:15
 * @Hello-Idea
 */
public class BIOClient {
    public static void main(String[] args) {

        // 直接发
        try {
            Socket socket = new Socket("localhost",8888);
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write("你好".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
