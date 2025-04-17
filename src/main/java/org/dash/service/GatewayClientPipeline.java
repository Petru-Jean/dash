package org.dash.service;

import org.dash.event.outgoing.OutgoingEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class GatewayClientPipeline
{
    GatewayClient gatewayClient;

    public GatewayClientPipeline(@Autowired @Lazy GatewayClient gatewayClient)
    {
        this.gatewayClient = gatewayClient;
    }

    public void sendEvent(OutgoingEvent event)
    {
        gatewayClient.send(event.getPayload().toString());
    }

    public void sendCloseSignal()
    {
        gatewayClient.close();
    }

}
