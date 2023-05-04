package com.example.ws.core;

import com.corundumstudio.socketio.HandshakeData;
import com.corundumstudio.socketio.SocketIOClient;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SessionManager {
    // sessionId => [sessionId, conn]，同一个session，有多条连接，同一个浏览器，打开多个
    private Map<String, Set<SocketIOClient>> sessionConnections = new ConcurrentHashMap<>();
    // userId -> [userId, sessionId] 同一用户多端登录
    private Map<Integer, Set<String>> userSessions = new ConcurrentHashMap<>();

    public void addSession( SocketIOClient client) {
        // 客户端连接的时候假如到缓存里面，以便后续推送消息，key是用户sessionId，value 是用户连接
        String sessionId = getSessionId(client);
        if (sessionId != null) {
            Set<SocketIOClient> connections = sessionConnections.computeIfAbsent(sessionId, k -> new HashSet<>());
            connections.add(client);
            userLogin(sessionId);
        }
    }
    public void removeSession(SocketIOClient client) {
        String sessionId = getSessionId(client);
        if (sessionId != null) {
            Set<SocketIOClient> connections = sessionConnections.get(sessionId);
            if (connections != null) {
                connections.remove(client);
            }
        }
    }


    public Set<SocketIOClient> getUserSessions(Integer userId) {
        Set<String> sessions = userSessions.get(userId);
        if (sessions == null || sessions.size() == 0) {
            return null;
        }
        Set<SocketIOClient> clients = new HashSet<>();
        sessions.forEach(session -> {
            Set<SocketIOClient> connections = sessionConnections.get(session);
            if (connections != null) {
                clients.addAll(connections);
            }
        });
        return clients;
    }

    private Map<String, String> getCookies(SocketIOClient client) {
        HandshakeData handshakeData = client.getHandshakeData();
        String cookieString = handshakeData.getHttpHeaders().get("Cookie");
        return parseCookie(cookieString);
    }

    private Map<String, String> parseCookie(String cookie) {
        Map<String, String> map = new HashMap<>();
        if (cookie != null) {
            String[] cookies = cookie.split("; ");
            for (String ck : cookies) {
                String[] kv = ck.split("=");
                if (kv.length == 2) {
                    map.put(kv[0], kv[1]);
                }
            }
        }
        return map;
    }

    private void userLogin(String sessionId) {
        // 用户登录，新增用户session
        int userId = 0;
        if (sessionId != null && sessionId.trim().length() > 0) {
            try {
                userId = Integer.parseInt(sessionId.split("\\|")[1]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (userId > 0) {
            Set<String> sessions = userSessions.computeIfAbsent(userId, k -> new HashSet<>());
            sessions.add(sessionId);
        }
    }
    private String getSessionId ( SocketIOClient client) {
        String userId=client.get("userId");
       // Map<String, String> cookie = getCookies(client);
        String sid = client.getSessionId().toString() + "|" + userId;
        return sid;
    }



}
