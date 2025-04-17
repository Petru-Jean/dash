package org.dash.event.incoming;

import org.dash.model.EventPayload;

public interface IncomingEvent
{
    public void process(EventPayload payload);


}
