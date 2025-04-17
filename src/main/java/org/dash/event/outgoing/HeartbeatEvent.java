package org.dash.event.outgoing;

import org.dash.model.EventPayload;

import java.util.Optional;

public class HeartbeatEvent implements OutgoingEvent
{
    EventPayload payload;

    public HeartbeatEvent(int sequenceNumber)
    {
        payload = new EventPayload.EventPayloadBuilder(1).
                sequence(sequenceNumber).
                build();
    }

    @Override
    public EventPayload getPayload()
    {
        return payload;
    }

}
