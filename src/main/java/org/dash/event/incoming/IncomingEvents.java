package org.dash.event.incoming;

import java.util.Optional;

public enum IncomingEvents
{
    GUILD_CREATE("GUILD_CREATE"),

    MESSAGE_CREATE("MESSAGE_CREATE"),

    READY("READY"),

    VOICE_STATE_UPDATE("VOICE_STATE_UPDATE"),

    PRESENCE_UPDATE("PRESENCE_UPDATE"),

    RECONNECT(7),

    INVALID_SESSION(9),

    HELLO(10),

    HEARTBEAT_ACK(11);

    public final int opcode;
    public final Optional<String> name;

    private IncomingEvents(int opcode) {
        this.opcode = opcode;
        this.name = Optional.empty();
    }

    private IncomingEvents(String name) {
        this.opcode = 0;
        this.name = Optional.of(name);
    }
}
