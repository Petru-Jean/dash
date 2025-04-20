package org.dash.event.incoming;

import org.dash.event.annotations.GatewayEvent;
import org.dash.model.EventPayload;
import org.dash.client.GatewayClientPipeline;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@GatewayEvent(type = IncomingEvents.RECONNECT)
public class ReconnectEvent implements  IncomingEvent
{
    GatewayClientPipeline gatewayClientPipeline;

    @Autowired
    public ReconnectEvent(GatewayClientPipeline gatewayClientPipeline)
    {
        this.gatewayClientPipeline = gatewayClientPipeline;
    }

    @Override
    public void process(EventPayload payload)
    {
        System.out.println("[Reconnect Event]: Received signal to reconnect to Gateway API");
        gatewayClientPipeline.sendCloseSignal();
    }

}
