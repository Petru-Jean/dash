package org.dash.event.incoming;

import org.dash.model.EventPayload;
import org.dash.service.GatewayClientPipeline;
import org.springframework.stereotype.Component;

@Component
@OpcodeEvent(op = 7)
public class ReconnectEvent implements  IncomingEvent
{
    GatewayClientPipeline gatewayClientPipeline;

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
