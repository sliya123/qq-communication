package com.sliaya.server.service;

import java.net.Socket;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ManageServerConnectCLientThread {

    private static Map<String, ServerConnectCLientThread> map = new ConcurrentHashMap<>();

    // 根据 userid 获取对应的线程
    public static ServerConnectCLientThread getServerConnectCLientThread(String userId){
        return map.get(userId);
    }

    // 添加线程对象到 map 集合中
    public static void addServerConnectCLientThread(String userId,ServerConnectCLientThread serverConnectCLientThread){
        map.put(userId,serverConnectCLientThread);
    }

    public static void removeServerConnectCLientThread(String userId){
        map.remove(userId);
    }

    // 查看当前已经建立连接的用户
    public static String getOnlineUser(){

        // 通过迭代器获取map中所有的key值，根据key值遍历，获取所有的用户
        Iterator<String> iterator = map.keySet().iterator();
        StringBuilder sb = new StringBuilder();
        while (iterator.hasNext()){
            sb.append(iterator.next()).append(",");
        }

        return sb.toString();


    }

}
