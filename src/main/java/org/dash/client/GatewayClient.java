package org.dash.client;

import org.dash.DiscordAPIUtils;
import org.dash.event.outgoing.ResumeEvent;
import org.dash.model.EventPayload;
import org.dash.service.AuthService;
import org.dash.service.EventHandler;
import org.dash.service.HeartbeatController;
import org.dash.service.VoiceSessionService;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GatewayClient extends WebSocketClient
{
    AuthService authService;
    VoiceSessionService voiceService;

    HeartbeatController heartbeatController;
    EventHandler eventHandler;

    public enum WebSocketClientEvents
    {
        ON_CLOSE,
        ON_OPEN,
        ON_ERROR
    };

    @Autowired
    public GatewayClient(@Autowired AuthService authService, @Autowired HeartbeatController heartbeatController,
                         @Autowired EventHandler eventHandler, @Autowired VoiceSessionService voiceService)
    {
        super(DiscordAPIUtils.getGatewayURI());

        this.heartbeatController = heartbeatController;
        this.authService = authService;
        this.eventHandler = eventHandler;
        this.voiceService = voiceService;

        this.connect();
    }

    @Override
    public void onOpen(ServerHandshake handshake)
    {
        if(!authService.authenticated()) return;

        ResumeEvent event = new ResumeEvent(
                authService.getAuthToken(),
                authService.getSessionId(),
                heartbeatController.getSequence());

        String payload = event.getPayload().toString();
        System.out.println("[Gateway Client]: Outgoing payload: " + payload);
        send(payload);
    }

    @Override
    public void onMessage(String message)
    {
        try {
            EventPayload payload = EventPayload.fromJson(message);

            if(payload.getSequence() != null) {
                heartbeatController.setSequence(payload.getSequence());
            }

            eventHandler.process(payload);
        }
        catch(JSONException ex)
        {
            System.out.println("[Gateway Client]: Exception when processing the " +
                    "gateway message " + message + " as Event Payload " + ex.getMessage());
        }

    }

    @Override
    public void onClose(int code, String reason, boolean remote)
    {
        System.out.println("[Gateway Client]: On close called " + code + " " + reason);

        heartbeatController.cancelHeartbeat();

        if(!authService.authenticated())
        {
            voiceService.persistAllAndClearCache();
        }

        if(code == 4004 || code >= 4010 && code <= 4014)
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
                Thread.sleep(6500);
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
