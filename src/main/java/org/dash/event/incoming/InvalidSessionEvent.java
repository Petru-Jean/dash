package org.dash.event.incoming;

import org.dash.event.annotations.GatewayEvent;
import org.dash.model.EventPayload;
import org.dash.service.AuthService;
import org.dash.client.GatewayClientPipeline;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@GatewayEvent(type = IncomingEvents.INVALID_SESSION)
public class InvalidSessionEvent implements IncomingEvent
{
    AuthService authService;
    GatewayClientPipeline gatewayClientPipeline;

    @Autowired
    public InvalidSessionEvent(AuthService authService, GatewayClientPipeline gatewayClientPipeline)
    {
        this.authService = authService;
        this.gatewayClientPipeline = gatewayClientPipeline;
    }

    @Override
    public void process(EventPayload payload)
    {
        System.out.println("[Invalid Session Event]: Received signal to reconnect to Gateway API");

        var resume = (Boolean) payload.getData();

        authService.setAuthenticated(resume != null ? resume : false);

        gatewayClientPipeline.sendCloseSignal();

        // Need to make sure that any user that leaves the channel
        // between this point and the reconnection
        // has their voice state cache updated

        // Also guild create is not send when not resuming
    }

}
