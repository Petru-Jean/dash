package org.dash.event;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.function.Function;

import org.dash.event.incoming.*;
import org.dash.model.EventPayload;
import org.java_websocket.enums.Opcode;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class EventHandler
{
    Map<String, IncomingEvent>  dispatchEvents;
    Map<Integer, IncomingEvent> opcodeEvents;

    public EventHandler(@Autowired ApplicationContext ctx)
    {
        dispatchEvents = new HashMap<>();
        opcodeEvents   = new HashMap<>();

        var beans = ctx.getBeansWithAnnotation(OpcodeEvent.class);
        var ops   = ctx.getBeansWithAnnotation(DispatchEvent.class);

        for(var bean : beans.entrySet())
        {
            if(bean != null)
                System.out.println(bean.getKey() + " " + bean.getValue());
        }

        for(var bean : ops.entrySet())
        {
            if(bean != null)
                System.out.println(bean.getKey() + " " + bean.getValue());
        }

    }

    private void mapEventHandlers()
    {

    }

    public void process(EventPayload payload)
    {
        int op      = payload.getOpcode();
        String name = payload.getName();

        IncomingEvent event = (op == 0) ? dispatchEvents.get(name) : opcodeEvents.get(op);

        if(event == null)
        {
            System.out.print("[Event Handle] Event " + ((name !=null) ? name:("op" + op)) +" not processed as no handler is registered ");
            return;
        }

        event.process(payload);
    }

}
