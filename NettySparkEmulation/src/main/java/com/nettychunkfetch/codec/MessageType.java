package com.nettychunkfetch.codec;

public enum MessageType {
    REQUEST(1),
    RESPONSE(2);

    private final int value;

    MessageType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    // You can add a method to get enum from int value if necessary
    public static MessageType fromInt(int i) {
        for (MessageType type : MessageType.values()) {
            if (type.getValue() == i) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid message type value: " + i);
    }
}

