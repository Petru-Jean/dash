package org.dash.service;

import java.util.*;

import org.dash.client.WebSocketSubscriber;
import org.dash.event.annotations.GatewayEvent;
import org.dash.event.incoming.*;
import org.dash.model.EventPayload;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

@Service
public class EventHandlerService extends WebSocketSubscriber
{
    Map<String, IncomingEvent>  dispatchEvents;
    Map<Integer, IncomingEvent> opcodeEvents;

    HeartbeatService heartbeatService;

    @Autowired
    public EventHandlerService(List<IncomingEvent> events, HeartbeatService heartbeatService)
    {
        this.heartbeatService = heartbeatService;
        this.dispatchEvents = new HashMap<>();
        this.opcodeEvents   = new HashMap<>();

        mapEventHandlers(events);
    }

    private void mapEventHandlers(List<IncomingEvent> events)
    {
        for(var event : events)
        {
            var gateway = event.getClass().getAnnotation(GatewayEvent.class);

            if(gateway == null) throw new IllegalStateException("[Event Handler] Unable to process IncomingEvent as " +
                    "it is not annotated with GatewayEvent.class " + event);

            gateway.type().name.ifPresentOrElse(name -> {
                if(dispatchEvents.containsKey(name)) throw new IllegalStateException("[Event Handler] Duplicate dispatch event " +
                        "handler registered for " + event);

                dispatchEvents.put(name, event);
            }, () -> {
                int opcode = gateway.type().opcode;

                if(opcodeEvents.containsKey(opcode)) throw new IllegalStateException("[Event Handler] Duplicate opcode event " +
                        "handler registered for " + event);

                opcodeEvents.put(opcode, event);
            });
        }

    }

    public void process(EventPayload payload)
    {
        int op      = payload.getOpcode();
        String name = payload.getName();

        if(payload.getSequence() != null) {
            heartbeatService.setSequence(payload.getSequence());
        }

        IncomingEvent event = (op == 0) ? dispatchEvents.get(name) : opcodeEvents.get(op);

        if(event == null)
        {
            System.out.println("[Event Handle] Event " + payload.toString() + " not processed as no handler is registered");
            return;
        }

        String dispatchText = name + " [s=" + payload.getSequence() + "]";
        String opcodeText = "opcode " + op;

        System.out.println("[Event Handler] Processed payload " + (name != null ? dispatchText:opcodeText )+ " as " + event);

        event.process(payload);
    }


}
