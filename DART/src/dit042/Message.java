package dit042;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Message model object
 *
 * <p>
 * Album object attributes, getters and setters
 * Implements Comparator< Album > to sort with attribute Artist
 *
 * @param "albumArtist" is unique to this object
 *
 * @author Altansukh Tumenjargal
 * @version 0.3
 */
public class Message implements Comparable< Message >{
    private String msgId;
    private String senderId;
    private String recipientId;
    private LocalDateTime msgDate;
    private String msgSubject;
    private String msgBody;
    private String msgItemId;
    private String msgType;
    private boolean isRead;

    private final static String EOL = System.getProperty("line.separator");

    public Message(String senderId, String recipientId, LocalDateTime msgDate, String msgSubject, String msgBody, String msgItemId, String msgType) {
        this.msgId = String.valueOf(UUID.randomUUID());
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.msgDate = msgDate;
        this.msgSubject = msgSubject;
        this.msgBody = msgBody;
        this.msgItemId = msgItemId;
        this.msgType = msgType;
        this.isRead = false;
    }

    // Getters
    public String getMsgId() { return this.msgId; }
    public String getSenderId() { return this.senderId; }
    public String getRecipientId() { return this.recipientId; }
    public LocalDateTime getMsgDate() { return this.msgDate; }
    public String getMsgSubject() { return this.msgSubject; }
    public String getMsgBody() { return this.msgBody; }
    public String getMsgItemId() { return this.msgItemId; }
    public String getMsgType() { return this.msgType; }
    public Boolean getIsRead() { return this.isRead; }

    // Setters
    public void setMsgId(String msgId) { this.msgId = msgId; }
    public void setSenderId(String senderId) { this.senderId = senderId; }
    public void setRecipientId(String recipientId) { this.recipientId = recipientId; }
    public void setMsgDate(LocalDateTime msgDate) { this.msgDate = msgDate; }
    public void setMsgSubject(String msgSubject) { this.msgSubject = msgSubject; }
    public void setMsgBody(String msgBody) { this.msgBody = msgBody; }
    public void setMsgItemId(String msgItemId) { this.msgItemId = msgItemId; }
    public void setMsgType(String msgType) { this.msgType = msgType; }
    public void setIsRead(Boolean isRead) { this.isRead = isRead; }

    private String indicateIsRead() {
        String readIndicator = null;
        if(!isRead) {
            readIndicator = " * ";
        } else {
            readIndicator = "   ";
        }
        return readIndicator;
    }

    @Override
    public String toString() {
        return EOL + "|" + this.indicateIsRead() + "| Sender: " + this.getSenderId() + " | Subject: " + this.getMsgSubject() + " | Date: " + this.getMsgDate() + " itemId: " + this.getMsgItemId() + "Type: " + getMsgType();
    }

    @Override
    public int compareTo(Message msg) {
        return this.getMsgDate().compareTo(msg.getMsgDate());
    }
}