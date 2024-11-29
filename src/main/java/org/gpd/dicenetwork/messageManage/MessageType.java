package org.gpd.dicenetwork.messageManage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum MessageType {
    push,
    request,
    response,
    unknown;

    @JsonCreator
    public static MessageType fromString(String value) {
        if (value == null) {
            return unknown;
        }
        try {
            return valueOf(value.toLowerCase());
        } catch (IllegalArgumentException e) {
            return unknown;
        }
    }

    @JsonValue
    public String getValue() {
        return this.name();
    }
}
