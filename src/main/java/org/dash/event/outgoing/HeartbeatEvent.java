package org.dash.event.outgoing;

import org.dash.model.EventPayload;
import org.json.JSONObject;

import java.util.Optional;

public class HeartbeatEvent implements OutgoingEvent
{
    EventPayload payload;

    public HeartbeatEvent(int sequenceNumber)
    {
        payload = new EventPayload(1,
                Optional.of(sequenceNumber),
                Optional.empty(),
                Optional.empty());
    }

    @Override
    public EventPayload getPayload()
    {
        return payload;
    }

}
