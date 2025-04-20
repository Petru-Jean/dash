package org.dash.event.incoming;

import org.dash.event.annotations.GatewayEvent;
import org.dash.model.EventPayload;
import org.dash.service.VoiceSessionService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@GatewayEvent(type = IncomingEvents.VOICE_STATE_UPDATE)
public class VoiceStateUpdateEvent implements  IncomingEvent
{
    VoiceSessionService voiceService;

    @Autowired
    public VoiceStateUpdateEvent(VoiceSessionService service)
    {
        this.voiceService = service;
    }

    @Override
    public void process(EventPayload payload)
    {
        voiceService.handleStateUpdate((JSONObject) payload.getData());
    }


}
