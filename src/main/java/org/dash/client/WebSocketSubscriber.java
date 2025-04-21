package org.dash.client;

import org.dash.model.EventPayload;
import org.java_websocket.handshake.ServerHandshake;

public abstract class WebSocketSubscriber
{

    public void onOpen(ServerHandshake handshake) {}
    public void onError(Exception ex) {}
    public void onClose(int code, String reason, boolean remote) {}


}
