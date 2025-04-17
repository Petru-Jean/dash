package org.dash.service;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

public class VoiceSessionCache
{
    public class VoiceSession
    {
        public String             channelId;
        public ZonedDateTime sessionStartDate;

        public boolean userMuted;
        public boolean userDefeaned;

        public VoiceSession(String channelId, ZonedDateTime sessionStartDate, boolean muted, boolean defeaned)
        {
            this.channelId = channelId;
            this.sessionStartDate = sessionStartDate;
            this.userMuted = muted;
            this.userDefeaned = defeaned;
        }

    }

    private final Map<String, VoiceSession> activeSessions = new HashMap<>();

    public VoiceSession get(String userId)
    {
        return activeSessions.get(userId);
    }

    public void delete(String userId)
    {
        activeSessions.remove(userId);
    }

    public void add(String userId, String channelId, boolean muted, boolean defeaned)
    {
        if(activeSessions.containsKey(userId)) {
            return;
        }

        activeSessions.put(userId, new VoiceSession(channelId, ZonedDateTime.now(), muted, defeaned));
    }


}
