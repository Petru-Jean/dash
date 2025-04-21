package org.dash.client;

import org.dash.DiscordAPIUtils;
import org.dash.model.EventPayload;
import org.dash.service.*;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GatewayClient extends WebSocketClient
{
    List<WebSocketSubscriber> subscribers;
    EventHandlerService eventHandlerService;
    AuthService authService;

    @Autowired
    public GatewayClient(List<WebSocketSubscriber> subscribers, EventHandlerService handler,
                         AuthService authService)
    {
        super(DiscordAPIUtils.getGatewayURI());
        this.subscribers = subscribers;
        this.eventHandlerService = handler;
        this.authService = authService;

        this.connect();

    }

    @Override
    public void onOpen(ServerHandshake handshake)
    {
        subscribers.forEach(sub -> sub.onOpen(handshake));
    }

    int retries = 0;

    @Override
    public void onMessage(String message)
    {
        try {
            EventPayload payload = EventPayload.fromJson(message);

            eventHandlerService.process(payload);
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
        subscribers.forEach(sub -> sub.onClose(code, reason, remote));

        System.out.println("[Gateway Client]: On close called " + code + " " + reason);

        if(retries++ >= 100)
        {
            System.out.println("Maximum re-try attempts reached: " + retries);
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
            try {
                Thread.sleep(6500);
            } catch (InterruptedException e) {

            }

            this.uri = authService.authenticated() ?
                    authService.getResumeURI() : DiscordAPIUtils.getGatewayURI();

            System.out.println("[Gateway Client] Set URI to " + this.uri);

            reconnect();
        }).start();
    }

    @Override
    public void onError(Exception ex)
    {
        subscribers.forEach(sub -> sub.onError(ex));
    }

}
