package com.sliaya.server.service;


import com.sliaya.qqcommon.Message;
import com.sliaya.qqcommon.MessageType;
import com.sliaya.qqcommon.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 这是服务器，在监听9999端口，等待客户端得连接，并保持通信
 */
public class QQServer {

    private ServerSocket serverSocket;

    // 使用 ConcurrentHashMap 处理并发下的问题
    private static ConcurrentHashMap<String, User> vaildUsers = new ConcurrentHashMap<>();

    // 初始化 vaildUsers
    static {
        vaildUsers.put("100", new User("100", "123456"));
        vaildUsers.put("200", new User("200", "123456"));
        vaildUsers.put("300", new User("300", "123456"));
    }

    /**
     * 根据 userid 和 password 来检验用户登陆
     *
     * @param userId
     * @param password
     * @return
     */
    public boolean checkUser(String userId, String password) {
        User user = vaildUsers.get(userId);
        if (user == null)   // 用户为空， 说明 userId 不存在
            return false;

        if (!user.getPassword().equals(password))
            return false;

        return true;

    }

    public QQServer() {
        System.out.println("服务端在9999端口监听...");
        try {
            serverSocket = new ServerSocket(9999);

            while (true) {
                // 当和某个客户端连接后，会继续监听
                Socket socket = serverSocket.accept();  // 如果没有客户端连接，这里会阻塞
                // 得到和socket关联得对象输入流
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                // 第一次连接都是用户登陆，所以读读取的是user对象
                User user = (User) ois.readObject();

                // 创建一个message对象，准备回复给客户端
                Message message = new Message();
                // 得到和socket关联得对象输出流
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

                if (checkUser(user.getUserId(), user.getPassword())) { //登陆通过
                    message.setMesType(MessageType.MESSAGE_LOGIN_SUCCEED.getMessageType());
                    // 将message对象回复给客户端
                    oos.writeObject(message); // 对象不需要调用shutdown方法结束
                    // 创建一个线程。和客户端保持通信，该线程需要持有socket对象
                    ServerConnectCLientThread serverConnectCLientThread =
                            new ServerConnectCLientThread(socket, user.getUserId());
                    serverConnectCLientThread.start(); // 启动线程
                    // 将该线程放入一个集合类中，集中管理
                    ManageServerConnectCLientThread.addServerConnectCLientThread(user.getUserId(), serverConnectCLientThread);
                } else { // 登陆失败
                    System.out.println("用户 id = " + user.getUserId() + "，登陆失败！");
                    message.setMesType(MessageType.MESSAGE_LOGIN_FAIL.getMessageType());
                    oos.writeObject(message);
                    // 关闭socket
                    socket.close();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 如果服务器推出了 while，说明服务器不在监听，因此关闭 serverSocket
            try {
                if (serverSocket != null) {
                    serverSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
