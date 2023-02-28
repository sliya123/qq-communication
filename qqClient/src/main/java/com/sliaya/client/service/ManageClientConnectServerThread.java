package com.sliaya.client.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ManageClientConnectServerThread {

    // 多线程环境下，为了线程安全，因该使用 ConcurrentHashMap
    private static Map<String, ClientConnectServerThread> map = new ConcurrentHashMap<>();

    // 将某个线程加入到集合中
    public static void addClientConnectServerThread(String userId, ClientConnectServerThread clientConnectServerThread) {
        map.put(userId, clientConnectServerThread);
    }

    // 通过userId 获取对应得线程
    public static ClientConnectServerThread getClientConnectServerThread(String userId){
        return map.get(userId);
    }

}
