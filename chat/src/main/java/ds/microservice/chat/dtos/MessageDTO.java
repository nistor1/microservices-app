package ds.microservice.chat.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;


public class MessageDTO {

    private UUID messageId;

    private UUID senderId;

    private List<UUID> receiverIds;

    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")  // Handles date with timezone
    private OffsetDateTime timestamp;

    private boolean seen;

    public MessageDTO() {
    }

    public MessageDTO(UUID messageId, UUID senderId, List<UUID> receiverIds, String content, OffsetDateTime timestamp, boolean seen) {
        this.messageId = messageId;
        this.senderId = senderId;
        this.receiverIds = receiverIds;
        this.content = content;
        this.timestamp = timestamp;
        this.seen = seen;
    }

    public UUID getMessageId() {
        return messageId;
    }

    public void setMessageId(UUID messageId) {
        this.messageId = messageId;
    }

    public UUID getSenderId() {
        return senderId;
    }

    public void setSenderId(UUID senderId) {
        this.senderId = senderId;
    }

    public List<UUID> getReceiverIds() {
        return receiverIds;
    }

    public void setReceiverIds(List<UUID> receiverIds) {
        this.receiverIds = receiverIds;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public OffsetDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(OffsetDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }
}
