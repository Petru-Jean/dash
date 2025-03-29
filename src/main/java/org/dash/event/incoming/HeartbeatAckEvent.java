package org.dash.event.incoming;

import org.dash.event.outgoing.HeartbeatEvent;
import org.dash.model.EventPayload;
import org.dash.service.ClientResponseBus;

public class HeartbeatAckEvent implements IncomingEvent
{

    @Override
    public void process(EventPayload payload, ClientResponseBus bus)
    {
        //bus.send(new HeartbeatEvent(0));
    }

}
