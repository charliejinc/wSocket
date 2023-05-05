package com.example.ws.handler;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.annotation.OnEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component("testMessageEventHandler")
public class TestMessageEventHandler extends BaseEventHandler{

    @OnEvent(value = "message")
    public void onEvent(SocketIOClient client, AckRequest request, String data) {
        log.info("客户端" + client.getSessionId() + "发送消息:" + data);
        client.sendEvent("message", "服务端回复消息");
    }

}
