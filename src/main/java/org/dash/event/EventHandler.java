package org.dash.event;

import java.util.InvalidPropertiesFormatException;
import java.util.Map;
import java.util.MissingFormatArgumentException;
import java.util.Optional;

import org.dash.event.incoming.*;
import org.dash.model.EventPayload;
import org.dash.service.ClientResponseBus;
import org.java_websocket.client.WebSocketClient;
import org.json.JSONException;
import org.json.JSONObject;

public class EventHandler
{
    ClientResponseBus clientResponseBus;
    WebSocketClient   webSocketClient;

    Map<String, IncomingEvent> dispatchEvents = Map.of(
            "HELLO",          new HelloEvent(),
            "MESSAGE_CREATE", new MessageCreateEvent()
    );

    Map<Integer, IncomingEvent> opcodeEvents = Map.of(
            10, new HelloEvent(),
            11, new HeartbeatAckEvent()
    );

    public EventHandler(WebSocketClient client)
    {
        webSocketClient   = client;
        clientResponseBus = new ClientResponseBus(client);
    }

    public Optional<IncomingEvent> process(JSONObject json)
    {
        System.out.println("[Event Handler] Event received: " + json.toString());

        Optional<IncomingEvent> event = Optional.ofNullable(null);

        try
        {
            int op = json.getInt("op");

            event = Optional.ofNullable( (op == 0) ? dispatchEvents.get(json.getString("t")) : opcodeEvents.get(op));

            event.ifPresentOrElse(e -> {
                System.out.println("[Event Handler] Processing event " + json.toString() + " as " + e.getClass().getName());

                var data     = Optional.ofNullable(json.opt("d"));
                var sequence = Optional.ofNullable(json.optIntegerObject("s",null));
                var name     = Optional.ofNullable(json.optString("t",null));

                e.process(new EventPayload(op, data, sequence, name), clientResponseBus);
                },
                () -> {
                   System.out.println("[Event Handler] Unable to process event as there is no registered event handler " + json);
                });

        }
        catch(Exception ex)
        {
            System.out.println("[Event Handler] Exception when trying to process event: " + ex.getMessage());
        }

        return event;
    }


}
