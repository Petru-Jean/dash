package org.dash.event.outgoing;

import org.dash.model.EventPayload;

public interface OutgoingEvent
{
    public EventPayload getPayload();
}
