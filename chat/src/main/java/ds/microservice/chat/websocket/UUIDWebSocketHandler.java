package ds.microservice.chat.websocket;


import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import ds.microservice.chat.dtos.MessageDTO;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;

public class UUIDWebSocketHandler extends TextWebSocketHandler {

    private Map<String, WebSocketSession> sessions = new HashMap<>();
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String query = session.getUri().getQuery();
        Map<String, String> queryParams = new HashMap<>();
        if (query != null) {
            for (String param : query.split("&")) {
                String[] keyValue = param.split("=");
                queryParams.put(keyValue[0], keyValue[1]);
            }
        }

        String uuidSender = queryParams.get("uuidSender");
        if (uuidSender != null) {
            if (sessions.containsKey(uuidSender)) {
                System.out.println("WebSocket already connected for: " + uuidSender);
            }
            else {
                sessions.put(uuidSender, session);
                System.out.println("WebSocket connection established: " + uuidSender);
            }
        } else {
            System.out.println("Error: uuidSender not found in query parameters.");
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        objectMapper.registerModule(new JavaTimeModule());
        MessageDTO messageModel = objectMapper.readValue(message.getPayload(), MessageDTO.class);

        UUID uuidSender = messageModel.getSenderId();
        List<UUID> uuidReceivers = messageModel.getReceiverIds();
        String messageContent = messageModel.getContent();

        if (messageContent.equals("User is typing...")) {
            for (UUID uuidReceiver : uuidReceivers) {
                sendTypingStatusToReceiver(uuidReceiver, uuidSender, true);
            }
        } else if (messageContent.equals("--------------")) {
            for (UUID uuidReceiver : uuidReceivers) {
                sendTypingStatusToReceiver(uuidReceiver, uuidSender, false);
            }
        } else {
            for (UUID uuidReceiver : uuidReceivers) {
                WebSocketSession receiverSession = sessions.get(uuidReceiver.toString());
                if (receiverSession != null && receiverSession.isOpen()) {
                    String messageJson = objectMapper.writeValueAsString(messageModel);
                    TextMessage textMessage = new TextMessage(messageJson);
                    receiverSession.sendMessage(textMessage);
                }
            }
        }
    }

    private void sendTypingStatusToReceiver(UUID uuidReceiver, UUID uuidSender, boolean isTyping) throws IOException {
        WebSocketSession receiverSession = sessions.get(uuidReceiver.toString());
        if (receiverSession != null && receiverSession.isOpen()) {
            MessageDTO typingMessage = new MessageDTO();
            typingMessage.setSenderId(uuidSender);
            typingMessage.setReceiverIds(Arrays.asList(uuidReceiver));
            typingMessage.setContent(isTyping ? "User is typing..." : "--------------");

            String typingJson = objectMapper.writeValueAsString(typingMessage);
            TextMessage textMessage = new TextMessage(typingJson);
            receiverSession.sendMessage(textMessage);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        String query = session.getUri().getQuery();
        Map<String, String> queryParams = new HashMap<>();
        if (query != null) {
            for (String param : query.split("&")) {
                String[] keyValue = param.split("=");
                queryParams.put(keyValue[0], keyValue[1]);
            }
        }

        String uuidSender = queryParams.get("uuidSender");
        if (uuidSender != null) {
            sessions.remove(uuidSender);
            System.out.println("WebSocket connection closed: " + uuidSender);
        } else {
            System.out.println("Error: uuidSender not found in query parameters.");
        }
    }
}