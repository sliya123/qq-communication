package com.sliaya.client.service;

import com.sliaya.qqcommon.Message;
import com.sliaya.qqcommon.MessageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Arrays;

public class ClientConnectServerThread extends Thread {

    // 该线程需要持有Socket对象才能进行通信
    private Socket socket;

    public ClientConnectServerThread(Socket socket) {
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }

    @Override
    public void run() {
        ObjectInputStream ois = null;
        while (true) {
            System.out.println("客户端线程, 等待读取从服务器发送的消息");
            try {
                // 使用的是 tcp 协议 ，不需要像 udp协议 调用 send 和 receive
                ois = new ObjectInputStream(socket.getInputStream()); // 因为需要读取服务器发送的数据，因此这里会阻塞线程
                Message message = (Message) ois.readObject();

                // 如果服务器端发送的是 MESSAGE_RET_ONLINE_NUM ，说明 返回用户列表信息
                if (message.getMesType().equals(MessageType.MESSAGE_RET_ONLINE_NUM.getMessageType())) {
                    // 取出在线列表信息，并显示出来
                    // 这里规定返回的信息以 , 分开
                    String[] onlineUsers = message.getContent().split(",");
                    System.out.println("==============当前在线用户列表==================");
                    // lambda表达式遍历输出
                    Arrays.asList(onlineUsers).forEach(onlineUser -> System.out.println("用户: " + onlineUser));
                } else {
                    System.out.println("不做处理....");
                }

            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }
}
