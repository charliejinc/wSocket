package com.example.ws;

import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
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
    private SpringContextHolder springContextHolder;

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
                    Object bean = springContextHolder.getBean(className);
                    socketIONamespace.addListeners(bean);

                }));
        socketIOServer.start();
        logger.info("---------- NettySocket通知服务启动成功 ----------");

    }
}
