package com.example.ws.handler;

import com.corundumstudio.socketio.SocketIOClient;
import lombok.extern.slf4j.Slf4j;


public interface IEventHandler {
    void onConnect(SocketIOClient client);
    void onDisConnect(SocketIOClient client);

}
