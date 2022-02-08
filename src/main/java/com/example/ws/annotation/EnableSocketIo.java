package com.example.ws.annotation;

import com.example.ws.config.SocketIoConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;


/**
 * <p>socketIo服务器开关</>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(SocketIoConfig.class)
public @interface EnableSocketIo {
}

