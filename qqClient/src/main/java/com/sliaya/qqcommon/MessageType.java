package com.sliaya.qqcommon;

public enum MessageType {

    MESSAGE_LOGIN_SUCCEED("login_succeed"),                     // 登陆成功
    MESSAGE_LOGIN_FAIL("login_fail"),                           // 登陆失败
    MESSAGE_COMM_MSG("comm_msg"),                               // 普通消息
    MESSAGE_GET_ONLINE_NUM("get_online_num"),                   // 要求返回在线用户列表
    MESSAGE_RET_ONLINE_NUM("ret_online_num"),                   // 返回在线用户列表
    MESSAGE_CLIENT_EXIT("client_exit");                         // 客户端请求退出

    private String messageType;

    MessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public static void main(String[] args) {
        System.out.println();
    }
}
