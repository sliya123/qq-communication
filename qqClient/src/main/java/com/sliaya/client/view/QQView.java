package com.sliaya.client.view;

import com.sliaya.client.service.ManageClientConnectServerThread;
import com.sliaya.client.service.UserClientService;
import com.sliaya.client.util.Utility;
import com.sliaya.qqcommon.Message;
import com.sliaya.qqcommon.MessageType;

import java.util.List;

/**
 * 客户端的菜单界面
 */
public class QQView {

    private boolean loop = true;    // 控制是否显示菜单
    private String key;             // 接收用户得键盘输入
    private UserClientService userClientService = new UserClientService();  // 用于登陆服务器/注册用户等功能

    public static void main(String[] args) {
        QQView qqView = new QQView();
        qqView.mainMenu();
    }

    // 显示主菜单
    private void mainMenu(){

        while (loop) {
            // 一级菜单
            System.out.println("============欢迎登陆网络通信系统==============");
            System.out.println("\t\t 1 登陆系统");
            System.out.println("\t\t 9 退出系统");

            System.out.println("请输入你的选择: ");

            // 根据用户的输入，处理不同的逻辑
            key = Utility.readString(1);
            switch (key){
                case "1":
                    System.out.println("请输入用户号: ");
                    String userId = Utility.readString(50);
                    System.out.println("请输入密 码: ");
                    String pwd = Utility.readString(50);
                    if (userClientService.checkUser(userId,pwd)){
                        System.out.println("============欢迎 (用户 " + userId + ") 登陆成功==============");
                        while (loop){
                            System.out.println("\n============网络通信系统二级菜单 (用户 " + userId + ") ==============");
                            System.out.println("\t\t 1 显示在线用户列表");
                            System.out.println("\t\t 2 群发消息");
                            System.out.println("\t\t 3 私发消息");
                            System.out.println("\t\t 4 发送文件");
                            System.out.println("\t\t 9 退出系统");

                            key = Utility.readString(1);
                            switch (key){
                                case "1":
                                    // System.out.println("显示在线用户列表");
                                    userClientService.getOnlineFriendList();
                                    break;
                                case "2":
                                    System.out.println("群发消息");
                                    break;
                                case "3":
                                    System.out.println("私发消息");
                                    break;
                                case "4":
                                    System.out.println("发送文件");
                                    break;
                                case "9":
                                    // 给服务器发送退出消息，断开连接
                                    userClientService.loginOut();

                                    loop = false;
                                    break;

                            }
                        }
                    }else {
                        System.out.println("========登陆失败==========");
                    }
                    break;
                case "9":
                    loop = false;
                    break;
            }
        }
    }

}
