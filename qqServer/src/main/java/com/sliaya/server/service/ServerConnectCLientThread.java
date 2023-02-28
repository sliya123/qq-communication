package com.sliaya.server.service;

import com.sliaya.qqcommon.Message;
import com.sliaya.qqcommon.MessageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * 该类的一个对象和某个客户端保持通信
 */
public class ServerConnectCLientThread extends Thread {

    private Socket socket;
    private String userId;  // 连接到服务端的用户id


    public ServerConnectCLientThread(Socket socket, String userId) {
        this.socket = socket;
        this.userId = userId;
    }

    @Override
    public void run() {     // 这里线程处于 run 状态， 可以发送/接收消息
        // 获取与当前用户通信的线程
        ServerConnectCLientThread serverConnectCLientThread =
                ManageServerConnectCLientThread.getServerConnectCLientThread(userId);
        if (serverConnectCLientThread == null)
            return;
        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;
        while (true) {        // 判断当前的用户是否退出了线程
            try {
                System.out.println("服务端 " + userId + " 和客户端保持通信，读取消息");
                ois = new ObjectInputStream(socket.getInputStream());
                Message message = (Message) ois.readObject();

                // 根据客户端发送的信息，做不同的处理
                if (message.getMesType().equals(MessageType.MESSAGE_GET_ONLINE_NUM.getMessageType())) {  // 获取用户列表请求
                    // 调用查看用户列表方法用户列表
                    System.out.println(message.getSender() + " 要在线用户列表");
                    String onlineUser = ManageServerConnectCLientThread.getOnlineUser();
                    Message msgRet = new Message();
                    // 设置消息头
                    msgRet.setMesType(MessageType.MESSAGE_RET_ONLINE_NUM.getMessageType());
                    // 在线用户列表内容
                    msgRet.setContent(onlineUser);
                    // 接收者（即传送过来的 Sender ）
                    msgRet.setGetter(message.getSender());
                    // 将 megRet 对象 传送给客户端
                    oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(msgRet);
                } else if (message.getMesType().equals(MessageType.MESSAGE_CLIENT_EXIT.getMessageType())) { // 用户退出登陆
                    System.out.println(message.getSender() + " 退出登陆");
                    // 获取与当前用户通信的线程
                    // 在管理的线程中，将与当前通信的 socket关闭 并 退出当前线程
                    ManageServerConnectCLientThread.removeServerConnectCLientThread(message.getSender());
                    // 释放资源
                    ois.close();
                    socket.close();
                    // serverConnectCLientThread.interrupt(); // 关闭线程
                    // 退出与当前客户通信的线程
                    break;
                }else{
                    System.out.println("其他message，暂不处理");
                }
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }


    }
}
