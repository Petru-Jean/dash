package org.dash.event.incoming;

import org.dash.model.EventPayload;
import org.dash.service.HeartbeatController;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@OpcodeEvent(op = 11)
public class HeartbeatAckEvent implements IncomingEvent
{
    HeartbeatController heartbeatController;

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
