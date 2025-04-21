package org.dash.service;

import org.dash.DiscordAPIUtils;
import org.dash.client.GatewayClientPipeline;
import org.dash.client.WebSocketSubscriber;
import org.dash.event.outgoing.IdentifyEvent;
import org.dash.event.outgoing.ResumeEvent;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;

@Service
public class AuthService extends WebSocketSubscriber
{
    @Value("${AUTH_TOKEN}")
    private String authToken;

    private boolean authenticated;

    private URI    resumeURI;
    private String sessionId;

    GatewayClientPipeline pipeline;
    HeartbeatService heartbeatService;

    @Autowired
    public AuthService(GatewayClientPipeline pipeline, HeartbeatService heartbeatService)
    {
        this.resumeURI = DiscordAPIUtils.getGatewayURI();
        this.pipeline = pipeline;
        this.heartbeatService = heartbeatService;
    }

    @Override
    public void onError(Exception ex)
    {
        setAuthenticated(false);
        System.out.println("[On Error] " + ex + " " + ex.getMessage());
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake)
    {
        if(authenticated)
        {
            var event = new ResumeEvent(
                    authToken,
                    sessionId,
                    heartbeatService.getSequence());

            pipeline.sendEvent(event);
            return;
        }

        var event = new IdentifyEvent(authToken,
                new IdentifyEvent.ConnectionProperties("Placeholder OS", "Placeholder browser", "Placeholder device"),
                false,
                50,
                null,
                53608447);

        pipeline.sendEvent(event);
    }

    public String getSessionId()
    {
        return sessionId;
    }

    public void setSessionId(String sessionId)
    {
        this.sessionId = sessionId;
    }

    public boolean authenticated()
    {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated)
    {
        this.authenticated = authenticated;
    }

    public URI getResumeURI()
    {
        return resumeURI;
    }

    public void setResumeURI(URI resumeURI)
    {
        this.resumeURI = resumeURI;
    }

    public String getAuthToken()
    {
        return this.authToken;
    }


}
