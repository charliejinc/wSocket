package com.example.ws;
import com.corundumstudio.socketio.SocketIOServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class WebSocketService implements ApplicationRunner {
      /**
     * logger
     */
    private static final Logger logger = LoggerFactory.getLogger(WebSocketService.class);
    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info("---------- NettySocket通知服务开始启动 ----------");
        socketIOServer.start();
        logger.info("---------- NettySocket通知服务启动成功 ----------");
        
    }

    /**
     * socketIOServer
     */
    private final SocketIOServer socketIOServer;

    @Autowired
    public WebSocketService(SocketIOServer socketIOServer) {
        this.socketIOServer = socketIOServer;
    }

   
    
}
