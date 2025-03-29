package org.dash.service;

import org.dash.DiscordAPIUtils;
import org.dash.event.incoming.IncomingEvent;
import org.dash.event.EventHandler;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;

import java.util.Optional;

public class GatewayClient extends WebSocketClient
{
    EventHandler eventHandler;

    public GatewayClient()
    {
        super(DiscordAPIUtils.getGatewayURI());
    }

    public void start()
    {
        eventHandler = new EventHandler(this);
        connect();
    }

    @Override
    public void onOpen(ServerHandshake handshake) {}

    @Override
    public void onMessage(String message)
    {
        Optional<IncomingEvent> event = eventHandler.process(new JSONObject(message));
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        // The close codes are documented in class org.java_websocket.framing.CloseFrame
        System.out.println(
                "Connection closed by " + (remote ? "remote peer" : "us") + " Code: " + code + " Reason: "
                        + reason);

    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
        // if the error is fatal then onClose will be called additionally
    }


}
