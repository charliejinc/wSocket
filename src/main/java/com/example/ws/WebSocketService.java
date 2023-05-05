package com.example.ws;

import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import com.example.ws.core.SessionManager;
import com.example.ws.core.SpringContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

@Component
@Order(1)
public class WebSocketService implements ApplicationRunner {
    /**
     * logger
     */
    private static final Logger logger = LoggerFactory.getLogger(WebSocketService.class);
    /**
     * socketIOServer
     */
    @Autowired(required = false)
    private SocketIOServer socketIOServer;
    @Value("${socketIo.namespaces}")
    private String[] namespaces;

    @Autowired
    private SessionManager sessionManager;
    @Override
    public void run(ApplicationArguments args) {
        logger.info("---------- NettySocket通知服务开始启动 ----------");
        if (Objects.isNull(socketIOServer)) {
            return;
        }
        Optional.ofNullable(namespaces).ifPresent(nss ->
                Arrays.stream(nss).forEach(ns -> {
                    //获取命名空间

                    SocketIONamespace socketIONamespace =  socketIOServer.addNamespace(ns);;
                    //获取期待的类名
                    String className = ns.substring(1) + "MessageEventHandler";
                    Object bean = SpringContextHolder.getBean(className);
                    socketIONamespace.addListeners(bean);
                    socketIONamespace.addPingListener(client -> {
                        logger.info("客户端" + client.getSessionId() + "发送心跳");
                    });
                    socketIONamespace.addConnectListener(client -> {
                        String token = client.getHandshakeData().getSingleUrlParam("Authorization");
                        if (token == null) {
                            logger.info("客户端" + client.getSessionId() + "建立websocket连接失败，Authorization不能为null");
                            client.disconnect();
                            return;
                        }
                        String userId=token;
                        client.set("userId",userId);
                        if (userId != null) {
                            logger.info("客户端:{},建立websocket连接成功,userId:{}", client.getSessionId(), userId);
                            sessionManager.addSession( client);

                        } else {
                            System.err.println("客户端" + client.getSessionId() + "建立websocket连接失败");
                            client.disconnect();
                        }
                    });
                    socketIONamespace.addDisconnectListener(client -> {
                        logger.info("客户端" + client.getSessionId() + "断开websocket,userId:"+client.get("userId"));
                        sessionManager.removeSession(client);
                    });
                }));
        socketIOServer.start();
        logger.info("---------- NettySocket通知服务启动成功 ----------");

    }
}
