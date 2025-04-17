package org.dash.service;

import org.dash.DiscordAPIUtils;
import org.dash.event.EventHandler;
import org.dash.event.outgoing.ResumeEvent;
import org.dash.model.EventPayload;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class GatewayClient extends WebSocketClient
{
    AuthService         authService;
    HeartbeatController heartbeatController;
    EventHandler        eventHandler;

    @Autowired
    public GatewayClient(@Autowired AuthService authService, @Autowired HeartbeatController heartbeatController,
                         @Autowired EventHandler eventHandler)
    {
        super(DiscordAPIUtils.getGatewayURI());

        this.heartbeatController = heartbeatController;
        this.authService = authService;
        this.eventHandler = eventHandler;
    }

    @Override
    public void onOpen(ServerHandshake handshake)
    {
        if (authService.authenticated())
        {
            ResumeEvent event = new ResumeEvent("",
                    authService.getSessionId(),
                    heartbeatController.getSequence());

            send(event.getPayload().toString());
        }

    }

    @Override
    public void onMessage(String message)
    {
        try {
            EventPayload payload = EventPayload.fromJson(message);

            heartbeatController.setSequence(payload.getSequence());
            eventHandler.process(payload);
        }
        catch(JSONException ex)
        {
            System.out.print("[Gateway Client]: Exception when processing the " +
                    "gateway message " + message + " as Event Payload " + ex.getMessage());
        }

    }

    @Override
    public void onClose(int code, String reason, boolean remote)
    {
        heartbeatController.cancelHeartbeat();

        if(code >= 4010 && code <= 4014)
        {
            System.out.println("[Gateway Client]: Connection closed with no-reconnect code " + code + " and reason " + reason);
            return;
        }

        // Needs to be called on as separate thread
        // to ensure proper cleanup
        new Thread(() ->
        {

            try
            {
                Thread.sleep(5000);
            }
            catch (InterruptedException e) { }

            this.uri = DiscordAPIUtils.getGatewayURI();

            if(authService.authenticated())
            {
                this.uri = authService.getResumeURI();
            }

            reconnect();
        }).start();
    }

    @Override
    public void onError(Exception ex)
    {
        authService.setAuthenticated(false);
        System.out.println("[Gateway Client]: On error called " + ex.getMessage() + " " + ex);
    }

}
