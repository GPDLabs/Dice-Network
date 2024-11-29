package org.gpd.dicenetwork.messageManage;

public class MessageEntity<T> {
    private MessageHeaderEntity header;
    private T body;

    public MessageEntity() {}

    public MessageEntity(MessageHeaderEntity header, T body) {
        this.header = header;
        this.body = body;
    }

    public MessageHeaderEntity getHeader() {
        return header;
    }

    public void setHeader(MessageHeaderEntity header) {
        this.header = header;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }
}
