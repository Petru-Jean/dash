package org.dash.event.incoming;

import org.dash.event.annotations.GatewayEvent;
import org.dash.model.EventPayload;
import org.dash.service.HeartbeatController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@GatewayEvent(type = IncomingEvents.HEARTBEAT_ACK)
public class HeartbeatAckEvent implements IncomingEvent
{
    HeartbeatController heartbeatController;

    @Autowired
    public HeartbeatAckEvent(HeartbeatController heartbeatController)
    {
        this.heartbeatController = heartbeatController;
    }

    @Override
    public void process(EventPayload payload)
    {
        heartbeatController.scheduleHeartbeat();
    }

}
