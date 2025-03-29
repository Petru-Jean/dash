package org.dash.service;

import org.dash.event.outgoing.OutgoingEvent;
import org.java_websocket.client.WebSocketClient;

public class ClientResponseBus
{
    WebSocketClient webSocketClient;

    public ClientResponseBus(WebSocketClient client)
    {
        this.webSocketClient = client;
    }

    public void send(OutgoingEvent event)
    {
        System.out.println("[Client Response Bus] Sending outgoing event to WebSocket client " + event.getClass().getName() + " " + event.getPayload().getJson().toString());
        webSocketClient.send(event.getPayload().getJson().toString());
    }

}
