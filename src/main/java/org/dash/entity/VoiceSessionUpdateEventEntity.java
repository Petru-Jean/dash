package org.dash.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;

@Entity
@Table(name = "voice_session")
public class VoiceSessionUpdateEventEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "session_start_timestamp")
    ZonedDateTime timestampStart;

    @Column(name = "session_end_timestamp")
    ZonedDateTime timestampEnd;

    @Column(name = "user_id")
    String userId;

    @Column(name = "channel_id")
    String channelId;

    @Column(name = "user_muted")
    boolean isUserMuted;

    @Column(name = "user_defeaned")
    boolean isUserDefeaned;

    public boolean isUserMuted() {
        return isUserMuted;
    }

    public void setUserMuted(boolean userMuted) {
        isUserMuted = userMuted;
    }

    public boolean isUserDefeaned() {
        return isUserDefeaned;
    }

    public void setUserDefeaned(boolean userDefeaned) {
        isUserDefeaned = userDefeaned;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ZonedDateTime getTimestampStart() {
        return timestampStart;
    }

    public void setTimestampStart(ZonedDateTime timestampStart) {
        this.timestampStart = timestampStart;
    }

    public ZonedDateTime getTimestampEnd() {
        return timestampEnd;
    }

    public void setTimestampEnd(ZonedDateTime timestampEnd) {
        this.timestampEnd = timestampEnd;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }
}
