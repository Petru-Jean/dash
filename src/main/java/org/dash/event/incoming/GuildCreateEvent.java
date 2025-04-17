package org.dash.event.incoming;

import org.dash.model.EventPayload;
import org.springframework.stereotype.Component;

@Component
@DispatchEvent(name = "GUILD_CREATE")
public class GuildCreateEvent implements IncomingEvent
{

    @Override
    public void process(EventPayload payload)
    {

    }

}
