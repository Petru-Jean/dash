package org.dash.event.outgoing;

import org.dash.model.EventPayload;
import org.json.JSONObject;

import java.util.Optional;

public class ResumeEvent implements OutgoingEvent {

    EventPayload payload;

    public ResumeEvent(String token, String sessionId, int seq)
    {
        int opcode = 6;

        var json = new JSONObject();

        var data = new JSONObject();
        data.put("token", token);
        data.put("session_id", sessionId);
        data.put("seq", seq);

        json.put("d", data);

        payload = new EventPayload.EventPayloadBuilder(opcode).
                data(data).
                build();
    }

    @Override
    public EventPayload getPayload()
    {
        return payload;
    }

}
