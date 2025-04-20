package org.dash.event.incoming;

import org.dash.event.annotations.GatewayEvent;
import org.dash.model.EventPayload;
import org.springframework.stereotype.Component;

@Component
@GatewayEvent(type = IncomingEvents.PRESENCE_UPDATE)
public class PresenceUpdateEvent implements IncomingEvent
{

    @Override
    public void process(EventPayload payload)
    {

    }

}
