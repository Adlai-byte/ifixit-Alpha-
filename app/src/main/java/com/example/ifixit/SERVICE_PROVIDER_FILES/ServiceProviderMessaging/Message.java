package com.example.ifixit.SERVICE_PROVIDER_FILES.ServiceProviderMessaging;

import java.util.HashMap;
import java.util.Map;

public class Message {

    private String messageId;
    private String senderUid;
    private String receiverUid;
    private String messageText;
    private long timestamp;

    public Message() {}

    public Message(String messageId, String senderUid, String receiverUid, String messageText, long timestamp) {
        this.messageId = messageId;
        this.senderUid = senderUid;
        this.receiverUid = receiverUid;
        this.messageText = messageText;
        this.timestamp = timestamp;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getSenderUid() {
        return senderUid;
    }

    public void setSenderUid(String senderUid) {
        this.senderUid = senderUid;
    }

    public String getReceiverUid() {
        return receiverUid;
    }

    public void setReceiverUid(String receiverUid) {
        this.receiverUid = receiverUid;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("messageId", messageId);
        result.put("senderUid", senderUid);
        result.put("receiverUid", receiverUid);
        result.put("messageText", messageText);
        result.put("timestamp", timestamp);
        return result;
    }

}
