package org.dash.event.incoming;

import org.dash.event.annotations.GatewayEvent;
import org.dash.event.outgoing.IdentifyEvent;
import org.dash.model.EventPayload;
import org.dash.service.AuthService;
import org.dash.client.GatewayClientPipeline;
import org.dash.service.HeartbeatService;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@GatewayEvent(type = IncomingEvents.HELLO)
public class HelloEvent implements IncomingEvent
{
    HeartbeatService heartbeatService;
    AuthService authService;
    GatewayClientPipeline gatewayClientPipeline;

    @Autowired
    public HelloEvent(HeartbeatService heartbeatService, AuthService authService,
                      GatewayClientPipeline gatewayClientPipeline)
    {
        this.heartbeatService = heartbeatService;
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

        heartbeatService.updateInterval(interval);
        heartbeatService.scheduleHeartbeat();

    }

}
