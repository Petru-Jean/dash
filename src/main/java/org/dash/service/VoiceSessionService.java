package org.dash.service;

import org.dash.client.VoiceSessionCache;
import org.dash.client.WebSocketSubscriber;
import org.dash.entity.VoiceSessionEntity;
import org.dash.repository.VoiceRepository;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.Objects;

@Service
public class VoiceSessionService extends WebSocketSubscriber
{
    private VoiceSessionCache cache;
    private VoiceRepository   repository;
    private AuthService authService;

    public VoiceSessionService(@Autowired VoiceRepository repository, AuthService authService)
    {
        this.cache = new VoiceSessionCache();
        this.repository = repository;
        this.authService = authService;
    }

    public void cacheExistingSessions(JSONArray sessions)
    {
        sessions.forEach(state->
        {
            process((JSONObject) state);
        });
    }

    @Override
    public void onClose(int code, String reason, boolean remote)
    {
        if(authService.authenticated()) return;

        for(var entry : cache.getAll())
        {
            persistSession(entry.getKey(), entry.getValue());
        }

        cache.clear();
    }

    public void handleStateUpdate(JSONObject json)
    {
        process(json);
    }

    private void process(JSONObject data)
    {
        try
        {
            String userId    = data.getString("user_id");
            String channelId = data.optString("channel_id", null);

            boolean mute = data.getBoolean("mute") || data.getBoolean("self_mute");
            boolean deaf = data.getBoolean("deaf") || data.getBoolean("self_deaf");

            OffsetDateTime sessionEndDate = OffsetDateTime.now();

            VoiceSessionCache.VoiceSession session = cache.get(userId);

            if(session == null)
            {
                if(channelId == null) {
                    System.out.println("[Voice State Update Event]: Discarded " + userId + " voice session as the session start date could not be determined");
                    return;
                }

                cache.add(userId, channelId, mute, deaf);
                return;
            }

            // Conditions:
            // 1. When the user joins a channel or changes the current voice channel, persist the voice session and start new one
            //      and create a new one
            // 2. When the user leaves a voice channel, persist and delete the voice session
            // 3. When the user either mutes or defeans or unmutes/undefeans, persist the current session and start a new one;

            // User has left the voice channel
            if(channelId == null)
            {
                persistSession(userId, session);

                cache.delete(userId);
                return;
            }

            // User has either: joined a new channel or changed their mute/defean status
            if(!Objects.equals(channelId, session.channelId) || mute != session.userMuted || deaf != session.userDefeaned)
            {
                persistSession(userId, session);

                cache.delete(userId);
                cache.add(userId, channelId, mute, deaf);
            }

        }
        catch(JSONException exception)
        {
            System.out.println("[Voice State Update Event]: Exception when processing event " + exception.getMessage() + " " + exception);
        }

    }

    private String dateDiffToString(ZonedDateTime start, ZonedDateTime end)
    {
        Duration duration = Duration.between(start, end);

        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();
        long seconds = duration.toSecondsPart();

        return hours + " hours " + minutes + " minutes " + seconds + " seconds";
    }

    private void persistSession(String userId, VoiceSessionCache.VoiceSession session)
    {
        ZonedDateTime now = ZonedDateTime.now();

        try {
            var entity = new VoiceSessionEntity();
            entity.setChannelId(session.channelId);
            entity.setUserId(userId);
            entity.setTimestampStart(session.sessionStartDate);
            entity.setTimestampEnd(now);
            entity.setUserMuted(session.userMuted);
            entity.setUserDefeaned(session.userDefeaned);

            System.out.println("[Voice State Update Event]: User "+ userId +" has logged a " + dateDiffToString(session.sessionStartDate, now) + " voice session");

            repository.save(entity);
        }
        catch(Exception ex)
        {
            System.out.println("[Voice State Update Event] Exception when processing event " + ex.toString());
        }
    }

}
