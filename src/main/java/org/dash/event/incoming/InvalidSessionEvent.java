package org.dash.event.incoming;

import org.dash.model.EventPayload;
import org.dash.service.AuthService;
import org.dash.service.GatewayClientPipeline;
import org.springframework.stereotype.Component;

@Component
@OpcodeEvent(op = 9)
public class InvalidSessionEvent implements IncomingEvent
{
    AuthService authService;
    GatewayClientPipeline gatewayClientPipeline;

    public InvalidSessionEvent(AuthService authService, GatewayClientPipeline gatewayClientPipeline)
    {
        this.authService = authService;
        this.gatewayClientPipeline = gatewayClientPipeline;
    }

    @Override
    public void process(EventPayload payload)
    {
        System.out.println("[Invalid Session Event]: Received signal to reconnect to Gateway API");

        Boolean resumable = (Boolean) payload.getData();

        authService.setAuthenticated(resumable != null ? resumable : false);
        gatewayClientPipeline.sendCloseSignal();
    }

}
