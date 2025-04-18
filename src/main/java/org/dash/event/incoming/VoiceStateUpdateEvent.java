package org.dash.event.incoming;

import org.dash.entity.VoiceSessionUpdateEventEntity;
import org.dash.model.EventPayload;
import org.dash.service.VoiceSessionCache;
import org.dash.service.VoiceSessionCache.VoiceSession;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.Objects;

@Component
@DispatchEvent(name = "VOICE_STATE_UPDATE")
public class VoiceStateUpdateEvent implements  IncomingEvent
{
    VoiceSessionCache cache;

    public VoiceStateUpdateEvent()
    {
        this.cache = new VoiceSessionCache();
    }

    @Override
    public void process(EventPayload payload)
    {
        try
        {
            JSONObject obj = (JSONObject) payload.getData();

            String userId    = obj.getString("user_id");
            String channelId = obj.optString("channel_id", null);

            boolean mute = obj.getBoolean("mute") || obj.getBoolean("self_mute");
            boolean deaf = obj.getBoolean("deaf") || obj.getBoolean("self_deaf");

            OffsetDateTime sessionEndDate = OffsetDateTime.now();

            // Voice state has changed, check if there is any active session
            VoiceSession session = cache.get(userId);

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

    private void persistSession(String userId, VoiceSession session)
    {
        ZonedDateTime now = ZonedDateTime.now();

        try {
            var entity = new VoiceSessionUpdateEventEntity();
            entity.setChannelId(session.channelId);
            entity.setUserId(userId);
            entity.setTimestampStart(session.sessionStartDate);
            entity.setTimestampEnd(now);
            entity.setUserMuted(session.userMuted);
            entity.setUserDefeaned(session.userDefeaned);

            //repository.persist(entity);
            System.out.println("[Voice State Update Event]: User "+ userId +" has logged a " + dateDiffToString(session.sessionStartDate, now) + " voice session");
        }
        catch(Exception ex)
        {
            System.out.println("[Voice State Update Event] Exception when processing event " + ex.toString());
        }

    }


}
