package ds.microservice.measurement.service;

import ds.microservice.measurement.consumer.RabbitMQSimulatorConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class WebSocketMessageSender extends TextWebSocketHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketMessageSender.class);

    private final Map<WebSocketSession, String> sessionDeviceMap = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String deviceId = getDeviceIdFromUri(session);
        if (deviceId != null) {
            sessionDeviceMap.put(session, deviceId);
            LOGGER.info("WebSocket connection established for deviceId: {}", deviceId);
        } else {
            session.close();
            LOGGER.warn("WebSocket connection closed: Missing deviceId in URI");
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) throws Exception {
        sessionDeviceMap.remove(session);
        LOGGER.info("WebSocket connection closed: {}", session);
    }

    public void sendMessageToClients(String deviceId, String message) throws IOException {
        for (Map.Entry<WebSocketSession, String> entry : sessionDeviceMap.entrySet()) {
            if (entry.getValue().equals(deviceId) && entry.getKey().isOpen()) {
                try {
                    entry.getKey().sendMessage(new TextMessage(message));
                    LOGGER.info("Sent message to deviceId {}: {}", deviceId, message);
                } catch (IOException e) {
                    LOGGER.error("Error sending message to WebSocket session", e);
                }
            } else {
                sessionDeviceMap.remove(entry);
            }
        }
    }

    private String getDeviceIdFromUri(WebSocketSession session) {
        String path = session.getUri().getPath();
        return path.substring(path.lastIndexOf("/") + 1);
    }
}