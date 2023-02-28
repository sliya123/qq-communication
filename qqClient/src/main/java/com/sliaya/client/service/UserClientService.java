package com.sliaya.client.service;

import com.sliaya.qqcommon.Message;
import com.sliaya.qqcommon.MessageType;
import com.sliaya.qqcommon.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * 完成用户登陆验证和用户注册等功能
 */
public class UserClientService {

    // 其他地方可能也会使用user信息，因此设为成员属性
    private User user = new User();

    // 同上
    private Socket socket;

    // 根据UserId 和 pwd 到服务器验证该用户=是否合法
    public boolean checkUser(String userId, String pwd) {
        boolean check = false;
        user.setUserId(userId);
        user.setPassword(pwd);

        try {
            // 连接服务器
            socket = new Socket(InetAddress.getByName("127.0.0.1"), 9999);
            // 得到ObjectOutputStream对象
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(user); // 将user对象传输到服务器进行验证

            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Message msg = (Message) ois.readObject();
            // 接收服务器传输的消息，判断登陆是否成功
            if (msg.getMesType().equals(MessageType.MESSAGE_LOGIN_SUCCEED.getMessageType())) {
                // 创建一个和服务器端保持通信的线程 -> 创建一个类 ClientConnectServerThread 管理
                ClientConnectServerThread clientThread = new ClientConnectServerThread(socket);
                clientThread.start(); // 开启线程
                // 为了方便管理线程以及后面客户端的扩展，将该线程放入容器中进行管理
                ManageClientConnectServerThread.addClientConnectServerThread(userId, clientThread);
                check = true; // 登陆成功
            } else {
                // 如果登陆失败，就不能启动和服务器通信得线程，需要关闭socket
                socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return check;
    }

    /**
     * 向服务器发送请求 -> 获取当前在线的所有用户列表
     */
    public void getOnlineFriendList() {
        try {
            // 发送一个 message ，类型是 MESSAGE_GET_ONLINE_NUM
            Message message = new Message();
            message.setMesType(MessageType.MESSAGE_GET_ONLINE_NUM.getMessageType());
            message.setSender(user.getUserId());

            // 发送给服务器，应该是使用当前线程的 socket 对应的 ObjectOutputStream
            // 根据当前用户的userId，去 ManageClientConnectServerThread 去出对应的 ClientConnectServerThread，再从中取出socket
            ClientConnectServerThread clientConnectServerThread =
                    ManageClientConnectServerThread.getClientConnectServerThread(user.getUserId());

            ObjectOutputStream oos =
                    new ObjectOutputStream(clientConnectServerThread.getSocket().getOutputStream());
            oos.writeObject(message);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * 向服务器发送请求 -> 表示退出登陆，断开 socket 连接
     */
    public synchronized void loginOut() {
        try {
            // 发送一个 message ，类型是 MESSAGE_CLIENT_EXIT ,表示要向服务器退出
            Message message = new Message();
            message.setSender(user.getUserId());
            message.setMesType(MessageType.MESSAGE_CLIENT_EXIT.getMessageType());

            // 发送给服务器，应该是使用当前线程的 socket 对应的 ObjectOutputStream
            // 根据当前用户的userId，去 ManageClientConnectServerThread 去出对应的 ClientConnectServerThread，再从中取出socket
            ClientConnectServerThread clientConnectServerThread =
                    ManageClientConnectServerThread.getClientConnectServerThread(user.getUserId());

            Socket socket = clientConnectServerThread.getSocket();
            ObjectOutputStream oos =
                    new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(message);

            clientConnectServerThread.interrupt();  // 关闭当前线程

            // 释放资源
             oos.close();
             socket.close(); // 关闭 socket通信

            System.out.println(user.getUserId() + " 退出系统");
            // System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
