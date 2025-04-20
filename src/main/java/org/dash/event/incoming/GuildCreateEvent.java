package org.dash.event.incoming;

import org.dash.event.annotations.GatewayEvent;
import org.dash.model.EventPayload;
import org.dash.service.VoiceSessionService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@GatewayEvent(type = IncomingEvents.GUILD_CREATE)
public class GuildCreateEvent implements IncomingEvent
{
    VoiceSessionService voiceService;

    public GuildCreateEvent(@Autowired VoiceSessionService service)
    {
        this.voiceService = service;
    }

    @Override
    public void process(EventPayload payload)
    {
        var data = (JSONObject) payload.getData();

        String id = data.getString("id");

        if(data.optBoolean("unavailable")) {
            System.out.println("[Guild Create Event] Guild " + id + " is unavailable");
            return;
        }

        JSONArray states = (JSONArray) data.opt("voice_states");

        voiceService.cacheExistingSessions(states);

    }

}
