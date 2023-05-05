package com.example.ws.handler;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component("chatMessageEventHandler")
public class ChatMessageEventHandler extends BaseEventHandler{

    @OnEvent(value = "messageevent")
    public void onEvent(SocketIOClient client, AckRequest request, String data) {
        log.info("客户端" + client.getSessionId() + "发送消息:" + data);
        client.sendEvent("messageevent", "服务端回复消息");
    }
    @OnConnect
    @Override
    public void onConnect(SocketIOClient client) {
        super.onConnect(client);
    }


    @OnDisconnect
    @Override
    public void onDisConnect(SocketIOClient client) {
        super.onDisConnect(client);
    }
}
