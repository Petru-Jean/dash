package org.dash.event.incoming;

import org.dash.event.annotations.GatewayEvent;
import org.dash.event.outgoing.IdentifyEvent;
import org.dash.model.EventPayload;
import org.dash.service.AuthService;
import org.dash.client.GatewayClientPipeline;
import org.dash.service.HeartbeatController;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@GatewayEvent(type = IncomingEvents.HELLO)
public class HelloEvent implements IncomingEvent
{
    HeartbeatController heartbeatController;
    AuthService authService;
    GatewayClientPipeline gatewayClientPipeline;

    @Autowired
    public HelloEvent(HeartbeatController heartbeatController, AuthService authService,
                      GatewayClientPipeline gatewayClientPipeline)
    {
        this.heartbeatController = heartbeatController;
        this.authService = authService;
        this.gatewayClientPipeline = gatewayClientPipeline;
    }

    @Override
    public void process(EventPayload payload)
    {
        int interval = 45000;

        try {
            JSONObject json = (JSONObject) payload.getData();
            interval = json.getInt("heartbeat_interval");
        }
        catch(JSONException ex)
        {
            System.out.println("[Event] Exception when processing HelloEvent payload, defaulting to heartbeat interval " + interval + " ms "
                    + ex.getMessage() + " " + ex);
        }

        heartbeatController.updateInterval(interval);
        heartbeatController.scheduleHeartbeat();

        if(authService.authenticated())
        {
            System.out.println("[Hello Event] The current connection does not require authentication");
            return;
        }

        String token = authService.getAuthToken();

        var event = new IdentifyEvent(token,
                new IdentifyEvent.ConnectionProperties("Placeholder OS", "Placeholder browser", "Placeholder device"),
                false,
                50,
                null,
                53608447);

        gatewayClientPipeline.sendEvent(event);

    }

}
