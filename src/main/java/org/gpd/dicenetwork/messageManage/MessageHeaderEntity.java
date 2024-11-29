package org.gpd.dicenetwork.messageManage;

public class MessageHeaderEntity {
    private int messageLength;
    private MessageType messageType;
    private MessageName messageName;
    private String version;
    private String checksum;

    public MessageHeaderEntity() {

    }

    public MessageHeaderEntity(int messageLength, MessageType messageType, MessageName messageName, String version, String checksum) {
        this.messageLength = messageLength;
        this.messageType = messageType;
        this.messageName = messageName;
        this.version = version;
        this.checksum = checksum;
    }

    public int getMessageLength() {
        return messageLength;
    }

    public void setMessageLength(int messageLength) {
        this.messageLength = messageLength;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public MessageName getMessageName() {
        return messageName;
    }

    public void setMessageName(MessageName messageName) {
        this.messageName = messageName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }
}
