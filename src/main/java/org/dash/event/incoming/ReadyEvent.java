package org.dash.event.incoming;

import org.dash.DiscordAPIUtils;
import org.dash.model.EventPayload;
import org.dash.service.AuthService;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.net.URI;

@Component
@DispatchEvent(name = "READY")
public class ReadyEvent implements  IncomingEvent
{
    AuthService authService;

    public ReadyEvent(AuthService authService)
    {
        this.authService = authService;
    }

    @Override
    public void process(EventPayload payload)
    {
        authService.setAuthenticated(true);

        try
        {
            var obj = (JSONObject) payload.getData();

            String resumeUrl = obj.optString("resume_gateway_url");
            String sessionId = obj.getString("session_id");

            authService.setResumeURI(URI.create(resumeUrl));
            authService.setSessionId(sessionId);
        }
        catch(JSONException | IllegalArgumentException exception)
        {
            System.out.println("[Ready Event]: Unable to process event data " + exception.getMessage() + " " + exception);
            authService.setResumeURI(DiscordAPIUtils.getGatewayURI());
        }

    }

}
