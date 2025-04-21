package org.dash.event.incoming;

import org.dash.event.annotations.GatewayEvent;
import org.dash.model.EventPayload;
import org.dash.service.HeartbeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@GatewayEvent(type = IncomingEvents.HEARTBEAT_ACK)
public class HeartbeatAckEvent implements IncomingEvent
{
    HeartbeatService heartbeatService;

    @Autowired
    public HeartbeatAckEvent(HeartbeatService heartbeatService)
    {
        this.heartbeatService = heartbeatService;
    }

    @Override
    public void process(EventPayload payload)
    {
        heartbeatService.scheduleHeartbeat();
    }

}
