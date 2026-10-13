package com.marketplace.chat.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.time.Instant;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChatHandler extends TextWebSocketHandler {

    private final Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String username = (String) session.getAttributes().getOrDefault("username", "anonymous");
        String content = message.getPayload();
        if (content == null || content.isBlank()) return;
        if (content.length() > 2000) content = content.substring(0, 2000);

        Map<String, Object> payload = Map.of(
                "sender",    username,
                "content",   content,
                "timestamp", Instant.now().toString()
        );
        String json = mapper.writeValueAsString(payload);
        TextMessage out = new TextMessage(json);

        for (WebSocketSession s : sessions) {
            if (s.isOpen()) {
                try { s.sendMessage(out); } catch (Exception ignored) {}
            }
        }
    }
}
