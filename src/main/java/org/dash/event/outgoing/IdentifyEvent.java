package org.dash.event.outgoing;

import org.dash.model.EventPayload;
import org.json.JSONObject;

import java.util.Optional;

public class IdentifyEvent implements OutgoingEvent
{
    private EventPayload payload;

    private record ConnectionProperties(String os, String browser, String device) {}

    private record UpdatePresence() {}

    public IdentifyEvent(String token, ConnectionProperties connectionProperties, boolean compress, int large_threshold, int[] shard, Optional<UpdatePresence> updatePresence, int intents)
    {
        int opcode = 2;

        var data = new JSONObject();
        data.put("d", new JSONObject()
                .put("token",      token)
                .put("properties", connectionProperties)
                .put("compress",   compress))
                .put("large_threshold", large_threshold)
                .put("shard", shard)
                .put("presence", updatePresence)
                .put("intents", intents);

        payload = new EventPayload(opcode, Optional.of(data), Optional.ofNullable(null), Optional.ofNullable(null));

    }

    @Override
    public EventPayload getPayload()
    {
        return payload;
    }

}
