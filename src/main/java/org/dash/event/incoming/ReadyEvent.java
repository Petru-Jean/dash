package org.dash.event.incoming;

import org.dash.model.EventPayload;
import org.dash.service.ClientResponseBus;

public class ReadyEvent implements  IncomingEvent
{

    @Override
    public void process(EventPayload payload, ClientResponseBus bus)
    {

    }

}
