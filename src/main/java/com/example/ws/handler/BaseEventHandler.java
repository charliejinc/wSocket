package com.example.ws.handler;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.example.ws.core.SessionManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public  class BaseEventHandler implements IEventHandler{
    @Autowired
    private SessionManager sessionManager;


    @Override
    public void onConnect(SocketIOClient client) {
        String token = client.getHandshakeData().getSingleUrlParam("Authorization");
        if (token == null) {
            log.info("客户端" + client.getSessionId() + "建立websocket连接失败，Authorization不能为null");
            client.disconnect();
            return;
        }
        String userId=token;
        client.set("userId",userId);
        if (userId != null) {
           log.info("客户端:{},建立websocket连接成功,userId:{}", client.getSessionId(), userId);
            sessionManager.addSession( client);

        } else {
            System.err.println("客户端" + client.getSessionId() + "建立websocket连接失败");
            client.disconnect();
        }

    }

    @Override
    public void onDisConnect(SocketIOClient client) {
        log.info("客户端" + client.getSessionId() + "断开websocket连接成功");
        sessionManager.removeSession(client);
    }
}
