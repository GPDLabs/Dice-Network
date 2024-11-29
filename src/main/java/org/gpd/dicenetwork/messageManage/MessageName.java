package org.gpd.dicenetwork.messageManage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum MessageName {
    lotteryStart,
    lotteryResult,
    luckyWallet,
    loginVqr,
    registerWallet,
    getLotteryTime,
    unknown;

    @JsonCreator
    public static MessageName fromString(String value) {
        if (value == null) {
            return unknown;
        }
        try {
            return valueOf(value);
        } catch (IllegalArgumentException e) {
            return unknown;
        }
    }

    @JsonValue
    public String getValue() {
        return this.name();
    }
}

