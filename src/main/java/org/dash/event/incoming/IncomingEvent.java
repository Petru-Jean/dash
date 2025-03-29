package org.dash.event.incoming;

import org.dash.model.EventPayload;
import org.dash.service.ClientResponseBus;

public interface IncomingEvent
{
    public void process(EventPayload payload, ClientResponseBus bus);

}
