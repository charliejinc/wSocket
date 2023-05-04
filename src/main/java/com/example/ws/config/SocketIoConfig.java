package com.example.ws.config;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

//@Configuration
//@ConfigurationProperties(prefix = "socketio")
@Data
public class SocketIoConfig {
    /**
     * logger
     */
    private static final Logger logger = LoggerFactory.getLogger(SocketIoConfig.class);
    @Value("${socketIo.port}")
    private Integer port;

    @Value("${socketIo.workCount}")
    private Integer workCount;

    @Value("${socketIo.allowCustomRequests}")
    private Boolean allowCustomRequests;

    @Value("${socketIo.upgradeTimeout}")
    private Integer upgradeTimeout;

    @Value("${socketIo.pingTimeout}")
    private Integer pingTimeout;

    @Value("${socketIo.pingInterval}")
    private Integer pingInterval;

    @Value("${socketIo.maxFramePayloadLength}")
    private Integer maxFramePayloadLength;

    @Value("${socketIo.maxHttpContentLength}")
    private Integer maxHttpContentLength;
    /**
     * SocketIOServer配置
     *
     * @return com.corundumstudio.socketio.SocketIOServer
     * @date 2019/4/17 11:41
     */
    @Bean("socketIOServer")
    public SocketIOServer socketIOServer() {
        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        config.setHostname("localhost");
        // 配置端口
        config.setPort(port);
        // 开启Socket端口复用
        com.corundumstudio.socketio.SocketConfig socketConfig = new com.corundumstudio.socketio.SocketConfig();
        socketConfig.setReuseAddress(true);
        config.setSocketConfig(socketConfig);
        // 连接数大小
        config.setWorkerThreads(workCount);
        // 允许客户请求
        config.setAllowCustomRequests(allowCustomRequests);
        // 协议升级超时时间(毫秒)，默认10秒，HTTP握手升级为ws协议超时时间
        config.setUpgradeTimeout(upgradeTimeout);
        // Ping消息超时时间(毫秒)，默认60秒，这个时间间隔内没有接收到心跳消息就会发送超时事件
        config.setPingTimeout(pingTimeout);
        // Ping消息间隔(毫秒)，默认25秒。客户端向服务器发送一条心跳消息间隔
        config.setPingInterval(pingInterval);
        // 设置HTTP交互最大内容长度
        config.setMaxHttpContentLength(maxHttpContentLength);
        // 设置最大每帧处理数据的长度，防止他人利用大数据来攻击服务器
        config.setMaxFramePayloadLength(maxFramePayloadLength);
        return new SocketIOServer(config);
    }

    /**
     * 开启SocketIOServer注解支持
     *
     * @param socketServer
     * @return com.corundumstudio.socketio.annotation.SpringAnnotationScanner
     */
    @Bean
    public SpringAnnotationScanner springAnnotationScanner(SocketIOServer socketServer) {
        return new SpringAnnotationScanner(socketServer);
    }
}
