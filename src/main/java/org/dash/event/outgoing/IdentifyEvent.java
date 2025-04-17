package org.dash.event.outgoing;

import org.dash.model.EventPayload;
import org.hibernate.sql.Update;
import org.json.JSONObject;

import java.util.Optional;

public class IdentifyEvent implements OutgoingEvent
{
    private EventPayload payload;

    public record ConnectionProperties(String os, String browser, String device) {}

    public IdentifyEvent(String token, ConnectionProperties connectionProperties, boolean compress, int large_threshold, Integer shard, int intents)
    {
        int opcode = 2;

        var json = new JSONObject();

        var data = new JSONObject();
        data.put("token",      token);
        data.put("compress",   compress);
        data.put("large_threshold", large_threshold);
        data.put("intents", intents);

        var props = new JSONObject();
        props.put("os", connectionProperties.os);
        props.put("browser", connectionProperties.browser);
        props.put("device", connectionProperties.device);

        data.put("properties", props);

        if(shard != null) data.put("shard", shard);

        json.put("d", data);

        payload = new EventPayload.EventPayloadBuilder(opcode).
                data(data).
                build();
    }

    @Override
    public EventPayload getPayload()
    {
        return payload;
    }

}
