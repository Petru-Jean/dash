package org.dash.service;

import org.dash.DiscordAPIUtils;
import org.springframework.stereotype.Service;

import java.net.URI;

@Service
public class AuthService
{
    private URI    resumeURI;
    private String sessionId;

    private boolean authenticated;

    public AuthService()
    {
        resumeURI = DiscordAPIUtils.getGatewayURI();
    }

    public String getSessionId()
    {
        return sessionId;
    }

    public void setSessionId(String sessionId)
    {
        this.sessionId = sessionId;
    }

    public boolean authenticated()
    {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated)
    {
        this.authenticated = authenticated;
    }

    public URI getResumeURI()
    {
        return resumeURI;
    }

    public void setResumeURI(URI resumeURI)
    {
        this.resumeURI = resumeURI;
    }
}
